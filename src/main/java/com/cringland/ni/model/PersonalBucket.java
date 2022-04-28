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
public class PersonalBucket {

    String slug;
    String title;
    String location;
    String photoId;
    Set<String> tags;
    String author;
    String completedDate;
    double rating;
    
    public boolean isCompleted() {
        return completedDate != null;
    }
}
