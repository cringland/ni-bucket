package com.cringland.ni.model;

import java.util.Set;

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
public class BucketResponse {

    private String slug;
    private String title;
    private String location;
    private String author;
    private String photoId;
    private Set<String> tags;
    private double rating;
}
