package com.cringland.ni.model;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.cringland.ni.repository.model.ReviewItem;
import com.cringland.ni.repository.model.ReviewKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class ReviewRequest {

    @NotNull
    private String bucketItemSlug;
    @NotNull
    private String author;
    @NotNull
    private String title;
    @NotNull
    private String text;
    private int rating;
    
    public ReviewItem toItem() {
        return ReviewItem.builder()
                .key(new ReviewKey(UUID.randomUUID().toString(), this.bucketItemSlug))
                .author(this.author)
                .title(this.title)
                .text(this.text)
                .rating(this.rating)
                .build();
    }
}
