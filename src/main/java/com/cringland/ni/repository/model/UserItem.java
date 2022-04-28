package com.cringland.ni.repository.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@DynamoDBTable(tableName = "User")
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class UserItem {

    private String id;
    private Set<String> likedTags;
    private List<Map<String, String>> bucketList;

    @DynamoDBHashKey
    public String getId() {
        return id;
    }
}
