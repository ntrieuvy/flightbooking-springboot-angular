package com.bm.travelcore.dto;

import com.bm.travelcore.constant.IdentifyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String identifier;
    private String password;
    private IdentifyType identifyType;
}
