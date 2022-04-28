package com.cringland.ni.model;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketRequest {

    @NotNull
    String title;
    @NotNull
    String location;
    @NotNull
    Set<String> tags;
    @NotNull
    String author;
    boolean addToList;
}
