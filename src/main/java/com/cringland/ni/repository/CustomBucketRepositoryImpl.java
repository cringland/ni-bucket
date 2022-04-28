package com.cringland.ni.repository;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.cringland.ni.repository.model.BucketItem;
import com.cringland.ni.repository.model.ReviewItem;
import com.cringland.ni.repository.model.UserItem;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class CustomBucketRepositoryImpl implements CustomBucketRepository {

    private final DynamoDBTemplate dynamoDBTemplate;
    private final ReviewRepository reviewRepo;

    @Autowired
    public CustomBucketRepositoryImpl(DynamoDBTemplate dynamoDBTemplate, ReviewRepository reviewRepo) {
        this.dynamoDBTemplate = dynamoDBTemplate;
        this.reviewRepo = reviewRepo;
    }

    @Override
    public List<BucketItem> getSuggestions(UserItem user) {
        var reviews = reviewRepo.findAllByAuthor(user.getId());

        var weightings = calcWeightings(reviews, user.getLikedTags());

        var ids = user.getBucketList().stream()
                .map(map -> map.get("slug")).map(slug -> ":" + slug)
                .collect(Collectors.joining(", "));
        var scanRequest = new DynamoDBScanExpression()
                .withFilterExpression("NOT (#slug IN (" + ids + ")");

        return dynamoDBTemplate.scan(BucketItem.class, scanRequest)
                .stream().sorted(Comparator.comparingInt(item -> {
                    //Get total weighting by combining bucket items tags weight values
                    return item.getTags().stream().map(tag -> {
                                var weight = weightings.get(tag);
                                return Objects.requireNonNullElse(weight, 0);
                            }).mapToInt(it -> it)
                            .sum();
                })).collect(Collectors.toList());
    }

    private Map<String, Integer> calcWeightings(List<ReviewItem> reviews, Set<String> likedTags) {
        int likedWeight = 5;
        var map = new HashMap<String, Integer>();
        for (String tag : likedTags) {
            map.put(tag, likedWeight);
        }
        //TODO Add weighting based on reviewed items
        return map;
    }
}
