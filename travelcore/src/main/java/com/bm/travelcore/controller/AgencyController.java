package com.bm.travelcore.controller;

import com.bm.travelcore.dto.*;
import com.bm.travelcore.populator.impl.AgencyListResPopulator;
import com.bm.travelcore.service.AgencyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agencies")
@AllArgsConstructor
public class AgencyController {

    private final AgencyService agencyService;
    private final AgencyListResPopulator agencyResDTOPopulator;

    @GetMapping
    public List<AgencyResDTO> getAllAgencies(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int page) {

        return agencyService.getAll(page, pageSize)
                .stream()
                .map(agency -> {
                    AgencyResDTO agencyDTO = new AgencyResDTO();
                    agencyResDTOPopulator.populate(agency, agencyDTO);
                    return agencyDTO;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<AgencyResDTO> createAgency(@RequestBody AgencyReqDTO request) {
        AgencyResDTO response = new AgencyResDTO();
        agencyResDTOPopulator.populate(agencyService.create(request), response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{code}")
    public ResponseEntity<AgencyResDTO> getAgenciesByCode(@PathVariable String code) {
        AgencyResDTO response = new AgencyResDTO();
        agencyResDTOPopulator.populate(agencyService.getByCode(code), response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable Long id) {
        agencyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAgency(@PathVariable Long id, @Valid @RequestBody AgencyReqDTO request) {
        agencyService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/assign/{agencyId}")
    public ResponseEntity<String> assignAgencyToUser(@PathVariable Long userId,
                                                     @PathVariable Long agencyId) {
        agencyService.assignAgencyToUser(userId, agencyId);
        return ResponseEntity.ok("Agency assigned to user successfully");
    }
}
