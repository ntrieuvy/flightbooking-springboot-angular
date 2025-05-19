package com.bm.travelcore.service.impl;

import com.bm.travelcore.dto.CommissionDTO;
import com.bm.travelcore.model.enums.Roles;
import com.bm.travelcore.populator.impl.CommissionModelPopulator;
import com.bm.travelcore.populator.impl.CommissionResPopulator;
import com.bm.travelcore.repository.AirlineRepository;
import com.bm.travelcore.repository.RoleRepository;
import com.bm.travelcore.service.FlightCacheService;
import com.bm.travelcore.model.*;
import com.bm.travelcore.repository.AirportRepository;
import com.bm.travelcore.repository.CommissionRepository;
import com.bm.travelcore.service.CommissionService;
import com.bm.travelcore.service.UserService;
import com.bm.travelcore.strategy.datacom.data.*;
import com.bm.travelcore.strategy.datacom.data.FlightData;
import com.bm.travelcore.utils.constant.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommissionServiceImpl implements CommissionService {

    private final CommissionRepository commissionRepository;
    private final UserService userService;
    private final AirportRepository airportRepository;

    private final Map<String, AirportGroup> airportGroupCache = new ConcurrentHashMap<>();
    private final Map<String, Commission> commissionCache = new ConcurrentHashMap<>();
    private final AirlineRepository airlineRepository;
    private final FlightCacheService flightCacheService;
    private final CommissionResPopulator commissionResPopulator;
    private final CommissionModelPopulator commissionModelPopulator;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public FlightSearchResData applyCommission(FlightSearchResData datacomResDTO) {
        if (shouldSkipCommissionProcessing(datacomResDTO)) {
            return datacomResDTO;
        }

        User tempUser = userService.getCurrentAccount();
        if (tempUser == null || tempUser.getAgency() == null) {
            tempUser = userService.getSysAccount();
        }
        final User user = tempUser;

        Map<String, Commission> agencyCommissions = loadAllCommissions(user);
        Set<String> airportCodes = collectAllAirportCodes(datacomResDTO);
        preloadAirportsAndGroups(airportCodes);

        datacomResDTO.getListGroup().forEach(group ->
                processGroupFares(group, user, agencyCommissions)
        );

        return datacomResDTO;
    }

    @Override
    public void processCommissionFeeForOrderDetail(Long agencyId, OrderDetail orderDetail, Flight flight) {
        Airport airport = airportRepository.findByCode(flight.getDeparture());
        if (airport == null) {
            return;
        }
        Commission commission = commissionRepository.findByAgencyIdAndAirlineCodeAndAirportGroupId(agencyId, flight.getAirline(), airport.getAirportGroup().getId());

        if (commission == null) {
            return;
        }

        orderDetail.setCommissionAdt(orderDetail.getCommissionAdt() + commission.getSelfServiceFeeAdt());
        orderDetail.setCommissionChd(orderDetail.getCommissionChd() + commission.getSelfServiceFeeChd());
        orderDetail.setCommissionInf(orderDetail.getCommissionInf() + commission.getSelfServiceFeeInf());
        orderDetail.setSystemCommissionAdt(orderDetail.getSystemCommissionAdt() + commission.getServiceFeeAdt());
        orderDetail.setSystemCommissionChd(orderDetail.getSystemCommissionChd() + commission.getServiceFeeChd());
        orderDetail.setSystemCommissionInf(orderDetail.getSystemCommissionInf() + commission.getServiceFeeInf());
    }

    @Override
    public List<Commission> findAllByUser(User user) {
        return userService.isSysUser(user) ? commissionRepository.findAll() : commissionRepository.findByUserId(user.getId());
    }

    @Override
    public List<Commission> addOrUpdateCommissions(User user, List<CommissionDTO> commissionListReqDTO) {
        List<Commission> commissions = new ArrayList<>();

        commissionListReqDTO.forEach(commissionDTO -> {
            Commission commission;

            if (commissionDTO.getId() != null) {
                Optional<Commission> optionalCommission = commissionRepository.findById(commissionDTO.getId());
                commission = optionalCommission.orElseGet(Commission::new);
            } else {
                commission = new Commission();
            }

            if (userService.isSysUser(user) || commission.getUser() != null && commission.getUser().getId().equals(user.getId())) {
                commissionModelPopulator.populate(commissionDTO, commission);
                commissions.add(commission);
            }
        });

        commissionRepository.saveAll(commissions);
        commissionRepository.flush();

        return commissions;
    }

    @Override
    public void deleteByUser(User user, Long id) {
        Commission commission = commissionRepository.findById(id).orElse(null);
        if (commission != null && commission.getUser().getId().equals(user.getId())) {
            commissionRepository.deleteById(id);
        }
    }



    private boolean shouldSkipCommissionProcessing(FlightSearchResData datacomResDTO) {
        return datacomResDTO == null || !datacomResDTO.isSuccess()
                || datacomResDTO.getListGroup() == null || datacomResDTO.getListGroup().isEmpty();
    }

    private Set<String> collectAllAirportCodes(FlightSearchResData datacomResDTO) {
        final int MAX_AIRPORTS = 1000;
        Set<String> codes = new HashSet<>(MAX_AIRPORTS * 2);

        datacomResDTO.getListGroup().parallelStream()
                .filter(Objects::nonNull)
                .takeWhile(group -> codes.size() < MAX_AIRPORTS)
                .flatMap(group -> Optional.ofNullable(group.getListAirOptionRes())
                        .orElse(Collections.emptyList())
                        .stream())
                .filter(Objects::nonNull)
                .takeWhile(airOptionRes -> codes.size() < MAX_AIRPORTS)
                .flatMap(airOptionRes -> Optional.ofNullable(airOptionRes.getListFlightOption())
                        .orElse(Collections.emptyList())
                        .stream())
                .filter(Objects::nonNull)
                .takeWhile(flightOption -> codes.size() < MAX_AIRPORTS)
                .flatMap(flightOption -> Optional.ofNullable(flightOption.getListFlightData())
                        .orElse(Collections.emptyList())
                        .stream())
                .filter(Objects::nonNull)
                .takeWhile(flightData -> codes.size() < MAX_AIRPORTS)
                .forEach(flightData -> {
                    if (flightData.getStartPoint() != null) codes.add(flightData.getStartPoint());
                    if (flightData.getEndPoint() != null) codes.add(flightData.getEndPoint());
                });

        return codes;
    }

    private void preloadAirportsAndGroups(Set<String> airportCodes) {
        if (airportCodes.isEmpty()) return;

        List<Airport> airports = airportRepository.findByCodeIn(airportCodes);
        airports.stream()
                .filter(airport -> airport.getAirportGroup() != null)
                .forEach(airport -> {
                    airportGroupCache.putIfAbsent(airport.getCode(), airport.getAirportGroup());
                });
    }

    private void processGroupFares(Group group, User user, Map<String, Commission> commissions) {
        if (group.getListAirOptionRes() == null || group.getListAirOptionRes().isEmpty()) {
            return;
        }

        group.getListAirOptionRes().forEach(airOptionRes ->
                processAirOptionFares(airOptionRes, user, commissions)
        );
    }

    private void processAirOptionFares(AirOptionRes airOptionRes, User user, Map<String, Commission> commissions) {
        if (airOptionRes.getListFareOption() == null || airOptionRes.getListFareOption().isEmpty() ||
                airOptionRes.getListFlightOption() == null || airOptionRes.getListFlightOption().isEmpty()) {
            return;
        }

        List<FlightData> flightData = airOptionRes.getListFlightOption().get(0).getListFlightData();
        if (flightData == null || flightData.isEmpty()) {
            return;
        }

        FlightData firstFlightData = flightData.get(0);
        AirportGroup airportGroup = getCachedAirportGroup(firstFlightData.getStartPoint(), firstFlightData.getEndPoint());

        if (airportGroup == null) {
            return;
        }

        airOptionRes.getListFareOption().forEach(fareOption ->
                applyCommissionToFareOption(fareOption, user, fareOption.getAirline(),
                        airportGroup, commissions)
        );
    }

    private AirportGroup getCachedAirportGroup(String startPoint, String endPoint) {
        String routeKey = startPoint + "-" + endPoint;
        AirportGroup cached = airportGroupCache.get(routeKey);
        if (cached != null) {
            return cached;
        }

        AirportGroup startGroup = airportGroupCache.get(startPoint);
        AirportGroup endGroup = airportGroupCache.get(endPoint);

        if (startGroup != null && startGroup.equals(endGroup)) {
            airportGroupCache.put(routeKey, startGroup);
            return startGroup;
        }

        AirportGroup group = flightCacheService.findAirportGroupForFlight(startPoint, endPoint);
        if (group != null) {
            airportGroupCache.put(routeKey, group);
        }
        return group;
    }

    private void applyCommissionToFareOption(FareOption fareOption, User user, String airlineCode,
                                             AirportGroup airportGroup, Map<String, Commission> commissions) {
        String commissionKey = createCommissionKey(user, airlineCode, airportGroup.getId());
        Commission commission = commissions.computeIfAbsent(commissionKey,
                k -> commissionCache.computeIfAbsent(commissionKey,
                        key -> flightCacheService.getCommission(user.getId(), airlineCode, airportGroup.getId())));

        if (commission != null && fareOption.getListFarePax() != null) {
            fareOption.getListFarePax().forEach(farePax ->
                    applyCommissionToPax(farePax, commission, user)
            );

            fareOption.setTotalFare(fareOption.getListFarePax().get(0).getTotalFare());
            fareOption.setBaseFare(fareOption.getListFarePax().get(0).getBaseFare());
            fareOption.setPriceAdt(fareOption.getTotalFare());
        }
    }

    private void applyCommissionToPax(FarePax farePax, Commission commission, User user) {
        String paxType = farePax.getPaxType().trim().toUpperCase();
        double commissionFee = user.getAgency() != null && user.getId() != 1
                ? getSelfServiceFee(paxType, commission)
                : getServiceFee(paxType, commission);

        if (commissionFee > 0) {
            farePax.setBaseFare(farePax.getBaseFare() + commissionFee);
            farePax.setTotalFare(farePax.getTotalFare() + commissionFee);
            farePax.getListFareItem().get(0).setAmount(farePax.getListFareItem().get(0).getAmount() + commissionFee);
        }
    }

    private Map<String, Commission> loadAllCommissions(User user) {
        if (commissionCache.keySet().stream().anyMatch(key -> key.startsWith(user.getId() + "-"))) {
            return commissionCache.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(user.getId() + "-"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        List<Commission> commissions = commissionRepository.findByUserId(user.getId());
        Map<String, Commission> commissionMap = new HashMap<>(commissions.size());
        commissions.forEach(c -> {
            String key = createCommissionKey(user, c.getAirline().getCode(), c.getAirportGroup().getId());
            commissionMap.put(key, c);
            commissionCache.putIfAbsent(key, c);
        });
        return commissionMap;
    }

    private String createCommissionKey(User user, String airlineCode, Long airportGroupId) {
        return user.getId() + "-" + airlineCode + "-" + airportGroupId;
    }

    private double getSelfServiceFee(String paxType, Commission commission) {
        return switch (paxType) {
            case "ADT" -> commission.getSelfServiceFeeAdt();
            case "CHD" -> commission.getSelfServiceFeeChd();
            case "INF" -> commission.getSelfServiceFeeInf();
            default -> 0;
        };
    }

    private double getServiceFee(String paxType, Commission commission) {
        return switch (paxType) {
            case "ADT" -> commission.getServiceFeeAdt();
            case "CHD" -> commission.getServiceFeeChd();
            case "INF" -> commission.getServiceFeeInf();
            default -> 0;
        };
    }
}