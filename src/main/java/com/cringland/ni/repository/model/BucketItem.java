package com.cringland.ni.repository.model;

import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@DynamoDBTable(tableName = "Bucket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
public class BucketItem {

    private String slug;
    private String title;
    private String location;
    private String author;
    private String photoId;
    private Set<String> tags;

    @DynamoDBHashKey
    public String getSlug() {
        return slug;
    }
}
