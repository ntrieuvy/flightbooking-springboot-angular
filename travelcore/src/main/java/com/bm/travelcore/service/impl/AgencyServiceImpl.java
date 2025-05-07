package com.bm.travelcore.service.impl;

import com.bm.travelcore.utils.constant.ExceptionMessages;
import com.bm.travelcore.dto.AgencyReqDTO;
import com.bm.travelcore.utils.handler.NotFoundException;
import com.bm.travelcore.model.Agency;
import com.bm.travelcore.model.User;
import com.bm.travelcore.repository.AgencyRepository;
import com.bm.travelcore.repository.UserRepository;
import com.bm.travelcore.service.AgencyService;
import com.bm.travelcore.service.CommissionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AgencyServiceImpl implements AgencyService {

    private final AgencyRepository agencyRepository;
    private final UserRepository userRepository;
    private final CommissionService commissionService;

    @Override
    public List<Agency> getAll(int page, int pageSize) {
        return agencyRepository.findAll(PageRequest.of(page, pageSize)).getContent();
    }

    @Override
    public Agency create(AgencyReqDTO request) {
        Agency agency = Agency.builder()
                .code(request.getCode())
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .accountBalance(0.0)
                .build();
        return agencyRepository.save(agency);
    }

    @Override
    public Agency getByCode(String code) {
        return agencyRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.AGENCY_NOT_FOUND_WITH_CODE, code)));
    }

    @Override
    public void delete(Long id) {
        if (!agencyRepository.existsById(id)) {
            throw new NotFoundException(ExceptionMessages.AGENCY_NOT_FOUND);
        }
        agencyRepository.deleteById(id);
    }

    @Override
    public void update(Long id, AgencyReqDTO request) {
        Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.AGENCY_NOT_FOUND));

        agency.setCode(request.getCode());
        agency.setName(request.getName());
        agency.setPhone(request.getPhone());
        agency.setEmail(request.getEmail());
        agency.setAddress(request.getAddress());

        agencyRepository.save(agency);
    }

    @Override
    @Transactional
    public void assignAgencyToUser(Long userId, Long agencyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new RuntimeException("Agency not found"));

        user.setAgency(agency);
        agency.setUser(user);

        userRepository.save(user);
    }
}
