package com.cringland.ni.repository.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
public class ReviewKey implements Serializable {

    private String id;
    private String bucketItemSlug;
    
    @DynamoDBHashKey
    public String getId() {
        return id;
    }
    
    @DynamoDBRangeKey
    public String getBucketItemSlug() {
        return bucketItemSlug;
    }
}

