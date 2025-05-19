package com.bm.travelcore.controller;
import com.bm.travelcore.dto.*;
import com.bm.travelcore.model.Commission;
import com.bm.travelcore.model.User;
import com.bm.travelcore.populator.impl.CommissionResPopulator;
import com.bm.travelcore.service.CommissionService;
import com.bm.travelcore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/commissions")
public class CommissionController {

    private final CommissionService commissionService;
    private final CommissionResPopulator commissionResPopulator;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<CommissionDTO>> getAllByUser() {
        User user = userService.getCurrentAccount();
        return ResponseEntity.ok(commissionService.findAllByUser(user).stream()
                .map(commission -> {
                    CommissionDTO commissionDTO = new CommissionDTO();
                    commissionResPopulator.populate(commission, commissionDTO);
                    return commissionDTO;
                })
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<List<CommissionDTO>> addOrUpdateCommissionsByUser(@RequestBody List<CommissionDTO> commissionListReqDTO) {
        User user = userService.getCurrentAccount();
        List<Commission> commissions = commissionService.addOrUpdateCommissions(user, commissionListReqDTO);
        List<CommissionDTO> response = new ArrayList<>();
        commissions.forEach(commission -> {
            CommissionDTO commissionResDTO = new CommissionDTO();
            commissionResPopulator.populate(commission, commissionResDTO);
            response.add(commissionResDTO);
        });
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByUser(@PathVariable Long id) {
        User user = userService.getCurrentAccount();
        commissionService.deleteByUser(user, id);
        return ResponseEntity.noContent().build();
    }
}
