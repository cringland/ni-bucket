package com.cringland.ni.model;

import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserCreateRequest {

    @NotNull
    private String username;
    @NotNull
    private String email;
    private Set<String> tags;
}
