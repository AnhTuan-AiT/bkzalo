package com.hust.bkzalo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SignUpIM {

    @NotNull
    private String phoneNumber;

    @NotNull
    private String password;

    @NotNull
    private UUID deviceId;
}
