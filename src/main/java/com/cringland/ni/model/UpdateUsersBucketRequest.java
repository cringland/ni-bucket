package com.cringland.ni.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateUsersBucketRequest {

    @NotNull
    String slug;
    String dateCompleted;
}
