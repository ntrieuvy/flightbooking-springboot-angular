package com.bm.travelcore.strategy.stripe;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.model.CartData;
import com.bm.travelcore.model.Order;
import com.bm.travelcore.model.PaymentAccount;
import com.bm.travelcore.model.User;
import com.bm.travelcore.model.abstracts.AbstractOrderModel;
import com.bm.travelcore.model.enums.PaymentStatus;
import com.bm.travelcore.model.enums.PaymentType;
import com.bm.travelcore.repository.OrderRepository;
import com.bm.travelcore.repository.PaymentAccountRepository;
import com.bm.travelcore.service.UserService;
import com.bm.travelcore.strategy.PaymentGatewayStrategy;
import com.bm.travelcore.strategy.stripe.constants.StripePaymentConstants;
import com.bm.travelcore.strategy.stripe.utils.StripePaymentUtils;
import com.bm.travelcore.utils.constants.AppConstants;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.param.ChargeListParams;
import com.stripe.param.CustomerSearchParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import javax.crypto.KeyGenerator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service("stripe")
@RequiredArgsConstructor
public class StripeGateway implements PaymentGatewayStrategy {

    private final ApplicationProperties properties;
    private static final Logger LOG = LoggerFactory.getLogger(StripeGateway.class);
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final PaymentAccountRepository paymentAccountRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private final String INTENT_UNCAPTURE = "requires_capture";
    private final String FILTER_VALUE_CHARACTER = "'";
    private final String INTENT_CANCEL = "canceled";

    private KeyGenerator keyGenerator;

    @Override
    public void chargeForOrder(Order orderModel) throws StripeException {
        RequestOptions options = buildRequestOptions();
        PaymentIntent paymentIntent = PaymentIntent.retrieve(orderModel.getPaymentIntentId(), options);
        paymentIntent.capture(options);
        orderModel.setPaymentStatus(PaymentStatus.PAID);

        // refresh paymentIntent
        final int STRIPE_TOTAL_RETRIEVE_TIME = Integer.parseInt(AppConstants.STRIPE_TOTAL_RETRIEVE_TIME);
        int retrieveTime = 0;
        Boolean isHasStripeErr = Boolean.FALSE;
        StripeException stripeEx = null;

        while (retrieveTime < STRIPE_TOTAL_RETRIEVE_TIME)
        {
            try
            {
                paymentIntent = PaymentIntent.retrieve(orderModel.getPaymentIntentId(), options);
                int retrieveBalanceTransaction = 0;
                while (retrieveBalanceTransaction < STRIPE_TOTAL_RETRIEVE_TIME)
                {
                    try{
                        String balanceTransactionId = getBalanceTransactionId(paymentIntent, options);
                        if(StringUtils.isNotEmpty(balanceTransactionId)) {
                            BalanceTransaction balanceTransaction = BalanceTransaction.retrieve(balanceTransactionId, options);
                            orderModel.setPaymentServiceFees(String.valueOf(Double.valueOf(balanceTransaction.getFee()) / 100));
                            orderModel.setTotalPriceLessFees(String.valueOf(Double.valueOf(balanceTransaction.getNet()) / 100));
                            Double totalPrice = Double.valueOf(orderModel.getTotalPriceLessFees()) + Double.valueOf(orderModel.getPaymentServiceFees());
                            orderModel.setTotalPrice(new BigDecimal(totalPrice).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        }
                        break;
                    }catch (StripeException strEx)
                    {
                        stripeEx = strEx;
                        LOG.error("Try to retrieve Balance Transaction for Payment Intent ID {} of Order {} for {} time ", orderModel.getPaymentIntentId(), orderModel.getId(), retrieveBalanceTransaction + 1);
                    }
                    finally {
                        retrieveBalanceTransaction++;
                        if(retrieveBalanceTransaction == STRIPE_TOTAL_RETRIEVE_TIME)
                        {
                            isHasStripeErr = Boolean.TRUE;
                            LOG.error("Could not retrieve Balance Transaction Payment Intent ID {} to update Order {}, need to check !", orderModel.getPaymentIntentId(), orderModel.getId());
                        }
                    }
                }
                break;
            }catch (StripeException ex)
            {
                stripeEx = ex;
                LOG.error("Try to retrieve Payment Intent ID {} of Order {} for {} time ", orderModel.getPaymentIntentId(), orderModel.getId(), retrieveTime + 1);
            }
            finally {
                retrieveTime++;
                if(retrieveTime == STRIPE_TOTAL_RETRIEVE_TIME)
                {
                    isHasStripeErr = Boolean.TRUE;
                    LOG.error("Could not retrieve Payment Intent ID {} to update Order {}, need to check !", orderModel.getPaymentIntentId(), orderModel.getId());
                }
            }
        }

        if(stripeEx != null && isHasStripeErr)
        {
            throw stripeEx;
        }
        orderRepository.save(orderModel);
        entityManager.refresh(orderModel);
    }

    @Override
    public void updatePaymentIntentForOrder(Order orderModel) throws StripeException {
        RequestOptions options = buildRequestOptions();
        Map<String, Object> params = new HashMap<>();
        if (!userService.isSysUser(orderModel.getUser())) {
            Map<String, String> metaDataParams = new HashMap<>();
            metaDataParams.put(StripePaymentConstants.STRIPE_PAYMENT_MOBILE_NUMBER, orderModel.getUser().getAgency().getPhone());
            String agentAccountID = orderModel.getAgency().getCode();
            metaDataParams.put(StripePaymentConstants.STRIPE_PAYMENT_AGENT_ACCOUNT_ID, agentAccountID);
            metaDataParams.put(StripePaymentConstants.STRIPE_PAYMENT_ORDER_ID, String.valueOf(orderModel.getId()));
            params.put(StripePaymentConstants.STRIPE_PAYMENT_META_DATA, metaDataParams);
        }

        PaymentIntent intent = PaymentIntent.retrieve(orderModel.getPaymentIntentId(), options);
        if(intent != null){
            intent.update(params, options);
        }
    }

    @Override
    public String updatePaymentIntentForCart(CartData cartData) throws StripeException {
        RequestOptions options = buildRequestOptions();
        Map<String, Object> params = new HashMap<>();
        Double totalPrice = cartData.getTotalPrice();

        params.put(StripePaymentConstants.STRIPE_PAYMENT_AMOUNT, Math.round(totalPrice * 100));
        PaymentIntent intent = PaymentIntent.retrieve(cartData.getPaymentIntentId(), options);
        if(Boolean.TRUE.equals(isIntentUnCapture(cartData)))
        {
            cancelPaymentIntent(cartData);
            return createPaymentIntent(cartData);
        }
        if(intent != null){
            intent.update(params, options);
            return intent.getClientSecret();
        }

        return StringUtils.EMPTY;
    }

    @Override
    public void cancelPaymentIntent(AbstractOrderModel abstractOrderModel) throws StripeException {
        RequestOptions options = buildRequestOptions();
        if (abstractOrderModel == null || StringUtils.isEmpty(abstractOrderModel.getPaymentIntentId())) {
            return;
        }
        final int STRIPE_TOTAL_CANCEL_TIME = Integer.parseInt(StripePaymentConstants.STRIPE_CANCEL_TIME);
        int cancelTime = 0;
        while (cancelTime < STRIPE_TOTAL_CANCEL_TIME) {
            try {
                PaymentIntent intent = PaymentIntent.retrieve(abstractOrderModel.getPaymentIntentId(), options);
                if (intent == null) {
                    LOG.error("Intent is null with ID {} - Order code {} ", abstractOrderModel.getPaymentIntentId(), abstractOrderModel.getId());
                }
                if (intent != null && !INTENT_CANCEL.equals(intent.getStatus())) {
                    intent.cancel(options);
                    LOG.error("Intent with ID {} cancel successfully ", abstractOrderModel.getPaymentIntentId());
                    break;
                }
            } catch (Exception ex) {
                LOG.error("Could not retrieve Payment Intent ID {} of Order {} ", abstractOrderModel.getPaymentIntentId(), abstractOrderModel.getId());
            } finally {
                cancelTime++;
            }
        }
        abstractOrderModel.setPaymentIntentId(StringUtils.EMPTY);
        if (abstractOrderModel instanceof Order) {
            Order order = (Order) abstractOrderModel;
            orderRepository.save(order);
        }
    }

    @Override
    public void renewPaymentIntent(CartData cartData) {
        try {
            if(isIntentUnCapture(cartData))
            {
                cancelPaymentIntent(cartData);
                createPaymentIntent(cartData);
            }
        } catch (StripeException e) {
            LOG.debug(StripePaymentConstants.STRIPE_PAYMENT_FAILED_MESSAGE,e);
        }
    }

    @Override
    public PaymentAccount createPaymentIntentForAccount(User user, String amount) throws StripeException {
        String stripeCustomerId = getStripeCustomerId(user);
        PaymentAccount paymentAccount = createPaymentAccount(amount, user);
        RequestOptions options = buildRequestOptions();
        String totalAmount = StripePaymentUtils.updateSurchargeFeeForAccountPayment(amount, paymentAccount);
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(Math.round(Double.valueOf(totalAmount) * 100))
                .setCurrency(StripePaymentConstants.STRIPE_PAYMENT_CURRENCY_VN)
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL)
                .setCustomer(stripeCustomerId)
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(createParams, options);
        paymentAccount.setIntentSecret(paymentIntent.getClientSecret());
        paymentAccount.setPaymentIntentId(paymentIntent.getId());
        paymentAccountRepository.save(paymentAccount);
        return paymentAccount;
    }

    @Override
    public String updatePaymentIntentForAccount(User user, String amount, String paymentIntentId, PaymentAccount paymentAccount) throws StripeException {
        String stripeCustomerId = getStripeCustomerId(user);
        RequestOptions options = buildRequestOptions();
        String totalAmount = StripePaymentUtils.updateSurchargeFeeForAccountPayment(amount, paymentAccount);
        Map<String, Object> params = new HashMap<>();
        params.put(StripePaymentConstants.STRIPE_PAYMENT_AMOUNT, Math.round(Double.parseDouble(totalAmount) * 100));
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId, options);
        if (intent != null) {
            if (StringUtils.isEmpty(intent.getCustomer())) {
                intent.setCustomer(stripeCustomerId);
            }
            intent.update(params, options);
            paymentAccount.setAmount(amount);
            paymentAccount.setTransactionDate(StripePaymentUtils.getCurrentDate());
            paymentAccount.setIntentSecret(intent.getClientSecret());
            paymentAccountRepository.save(paymentAccount);
            return intent.getClientSecret();
        }
        return StringUtils.EMPTY;
    }

    @Override
    public PaymentAccount chargeForPaymentAccount(PaymentAccount paymentAccount) throws StripeException {
        RequestOptions options = buildRequestOptions();
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentAccount.getPaymentIntentId(), options);
            paymentIntent.capture(options);
            paymentIntent = PaymentIntent.retrieve(paymentAccount.getPaymentIntentId(), options);
            try{
                String balanceTransactionId = getBalanceTransactionId(paymentIntent, options);
                if(StringUtils.isNotEmpty(balanceTransactionId)) {
                    BalanceTransaction balanceTransaction = BalanceTransaction.retrieve(balanceTransactionId, options);
                    paymentAccount.setPaymentServiceFees(String.valueOf(Double.valueOf(balanceTransaction.getFee()) / 100));
                    paymentAccount.setTotalPriceLessFees(String.valueOf(Double.valueOf(balanceTransaction.getNet()) / 100));
                    double totalPrice = Double.parseDouble(paymentAccount.getTotalPriceLessFees()) + Double.parseDouble(paymentAccount.getPaymentServiceFees());
                    paymentAccount.setAmount(String.valueOf(new BigDecimal(totalPrice).setScale(2, RoundingMode.HALF_UP).doubleValue()));
                }
            }catch (StripeException strEx)
            {
                LOG.error("Try to retrieve Balance Transaction for Payment Intent ID {} of Order {} ", paymentAccount.getPaymentIntentId(), paymentAccount.getPaymentAccountId());
            }

        } catch (StripeException e) {
            LOG.error(e.getMessage());
            renewPaymentIntentForPaymentAccount(paymentAccount);
            throw e;
        }
        paymentAccount.setPaymentStatus(PaymentStatus.PAID);
        paymentAccountRepository.save(paymentAccount);
        paymentAccountRepository.flush();
        entityManager.refresh(paymentAccount);
        return paymentAccount;
    }

    protected void renewPaymentIntentForPaymentAccount(PaymentAccount paymentAccount) throws StripeException {
        cancelPaymentIntentForPaymetAccount(paymentAccount);
        RequestOptions options = buildRequestOptions();
        String totalAmount = StripePaymentUtils.updateSurchargeFeeForAccountPayment(paymentAccount.getAmount(), paymentAccount);
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(Math.round(Double.parseDouble(totalAmount) * 100))
                .setCurrency(StripePaymentConstants.STRIPE_PAYMENT_CURRENCY_VN)
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL)
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(createParams, options);
        paymentAccount.setIntentSecret(paymentIntent.getClientSecret());
        paymentAccount.setPaymentIntentId(paymentIntent.getId());
        paymentAccountRepository.save(paymentAccount);
    }

    private void cancelPaymentIntentForPaymetAccount(PaymentAccount paymentAccount) {
        RequestOptions options = buildRequestOptions();
        final int STRIPE_TOTAL_CANCEL_TIME = Integer.parseInt(StripePaymentConstants.STRIPE_CANCEL_TIME);
        int cancelTime = 0;
        while (cancelTime < STRIPE_TOTAL_CANCEL_TIME) {
            try {
                PaymentIntent intent = PaymentIntent.retrieve(paymentAccount.getPaymentIntentId(), options);
                if (intent == null) {
                    LOG.error("Intent is null with ID {} - Order code {} ", paymentAccount.getPaymentIntentId(), paymentAccount.getPaymentAccountId());
                }
                if (intent != null && !INTENT_CANCEL.equals(intent.getStatus())) {
                    intent.cancel(options);
                    LOG.error("Intent with ID {} cancel successfully ", paymentAccount.getPaymentIntentId());
                    break;
                }
            } catch (Exception ex) {
                LOG.error("Could not retrieve Payment Intent ID {} of PaymmentAccount {} ", paymentAccount.getPaymentIntentId(), paymentAccount.getPaymentAccountId());
            } finally {
                cancelTime++;
            }
        }
    }

    private String getBalanceTransactionId(PaymentIntent paymentIntent, RequestOptions options) throws StripeException {
        ChargeListParams params = ChargeListParams.builder()
                .setPaymentIntent(paymentIntent.getId())
                .setLimit(1L)
                .build();

        ChargeCollection charges = Charge.list(params, options);

        return charges.getData().stream()
                .findFirst()
                .map(Charge::getBalanceTransaction)
                .orElse(StringUtils.EMPTY);
    }

    @Override
    public boolean isIntentUnCapture(AbstractOrderModel abstractOrderModel) throws StripeException {
        RequestOptions options = buildRequestOptions();
        PaymentIntent intent = PaymentIntent.retrieve(abstractOrderModel.getPaymentIntentId(), options);
        if(intent.getStatus().equals(INTENT_UNCAPTURE)){
            return true;
        }
        return false;
    }

    @Override
    public String createPaymentIntent(CartData cartData) throws StripeException {
        RequestOptions options = buildRequestOptions();
        double totalPrice = cartData.getTotalPrice();

        String stripeCustomerId = getStripeCustomerId(cartData.getUser());
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(Math.round(totalPrice * 100))
                .setCurrency(cartData.getCurrency())
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL)
                .setCustomer(stripeCustomerId)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(createParams, options);
        cartData.setPaymentIntentId(paymentIntent.getId());
        return  paymentIntent.getClientSecret();
    }

    private String getStripeCustomerId(User user) throws StripeException {
        return Boolean.FALSE.equals(isExistedStripeCustomer(String.valueOf(user.getId())))
                ? createStripeCustomer(user)
                : String.valueOf(user.getId());
    }

    public boolean isExistedStripeCustomer(String customerId) throws StripeException {
        RequestOptions options = buildRequestOptions();
        String query = "name:";
        String formatCustomerId = FILTER_VALUE_CHARACTER.concat(customerId).concat(FILTER_VALUE_CHARACTER);
        query = query.concat(formatCustomerId);

        CustomerSearchParams params = CustomerSearchParams.builder().setQuery(query).build();
        CustomerSearchResult result = Customer.search(params, options);

        if (result != null && result.getData() != null && !result.getData().isEmpty()) {
            return result.getData().stream()
                    .anyMatch(customer -> customer.getId().equals(customerId));
        }

        return false;
    }

    @Override
    public SetupIntent createSetupIntent(User user) throws StripeException {
        RequestOptions options = buildRequestOptions();
        String stripeCustomerId = getStripeCustomerId(user);
        List<Object> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", paymentMethodTypes);
        params.put("customer",stripeCustomerId);

        return SetupIntent.create(params,options);
    }

    @Override
    public List<PaymentMethod> collectPaymentMethod(String customerId) throws StripeException {
        RequestOptions options = buildRequestOptions();
        Map<String, Object> params = new HashMap<>();

        if (!isExistedStripeCustomer(customerId)) {
            return Collections.emptyList();
        }

        params.put("customer", customerId);
        params.put("type", "card");

        PaymentMethodCollection paymentMethods = PaymentMethod.list(params,options);
        return  paymentMethods != null && paymentMethods.getData() != null && !paymentMethods.getData().isEmpty()
                ? paymentMethods.getData()
                : new ArrayList<>();
    }

    @Override
    public void removePaymentMethod(String paymentMethodId) throws StripeException {
        RequestOptions options = buildRequestOptions();
        PaymentMethod.retrieve(paymentMethodId, options).detach(options);
    }

    @Override
    public String getDefaultPaymentMethod(String customerId) throws StripeException {
        RequestOptions options = buildRequestOptions();
        Customer customer = Customer.retrieve(customerId, options);

        if(customer == null){
            return StringUtils.EMPTY;
        }

        String paymentMethodId = StringUtils.EMPTY;
        if(customer.getInvoiceSettings() != null){
            paymentMethodId = customer.getInvoiceSettings().getDefaultPaymentMethod();
        }

        if(StringUtils.isEmpty(paymentMethodId) && StringUtils.isNotEmpty(customer.getDefaultSource())){
            paymentMethodId = customer.getDefaultSource();
        }

        return paymentMethodId;
    }

    @Override
    public void setDefaultPaymentMethod(String customerId, String paymentMethodId) throws StripeException {
        RequestOptions options = buildRequestOptions();
        Customer customer = Customer.retrieve(customerId, options);

        CustomerUpdateParams.InvoiceSettings.Builder invoiceSettingsBuilder = CustomerUpdateParams.InvoiceSettings.builder();
        CustomerUpdateParams.InvoiceSettings invoiceSettings = invoiceSettingsBuilder.setDefaultPaymentMethod(paymentMethodId).build();

        CustomerUpdateParams.Builder customerUpdateParamsBuilder = CustomerUpdateParams.builder();
        customer.update(customerUpdateParamsBuilder.setInvoiceSettings(invoiceSettings).build(), options);
    }

    private String createStripeCustomer(User user) throws StripeException {
        RequestOptions options = buildRequestOptions();
        Map<String, Object> params = new HashMap<>();
        params.put("id", String.valueOf(user.getId()));
        params.put("name", String.valueOf(user.getId()));
        params.put("description", user.getName());
        // Update meta data
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("mobileNumber", user.getAgency() != null ? user.getAgency().getPhone() :  StringUtils.EMPTY);
        metadata.put("email", user.getAgency() != null ? user.getAgency().getEmail() : StringUtils.EMPTY);
        params.put("metadata", metadata);
        Customer customer = Customer.create(params, options);
        return  customer.getId();
    }

    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.STRIPE;
    }

    protected RequestOptions buildRequestOptions()
    {
        String apiKey = properties.getStripeSecretKey();
        return RequestOptions.builder().setApiKey(apiKey).build();
    }

    protected PaymentAccount createPaymentAccount(String amount, User user){
        PaymentAccount paymentAccount = PaymentAccount.builder().build();
        paymentAccount.setPaymentAccountId(generatePaymentAccountCode());
        paymentAccount.setAmount(amount);
        paymentAccount.setUser(user);
        paymentAccount.setPaymentStatus(PaymentStatus.NOTPAID);
        paymentAccount.setTransactionDate(StripePaymentUtils.getCurrentDate());
        paymentAccountRepository.save(paymentAccount);
        return paymentAccount;
    }

    protected String generatePaymentAccountCode()
    {
        final Object generatedValue = keyGenerator.generateKey();
        return String.valueOf(generatedValue);
    }
}
