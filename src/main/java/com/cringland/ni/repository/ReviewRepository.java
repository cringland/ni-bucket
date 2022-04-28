package com.cringland.ni.repository;


import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.cringland.ni.repository.model.BucketItem;
import com.cringland.ni.repository.model.ReviewItem;
import com.cringland.ni.repository.model.ReviewKey;

@EnableScan
public interface ReviewRepository extends CrudRepository<ReviewItem, ReviewKey> {
    List<ReviewItem> findAllByBucketItemSlug(String s);
    List<ReviewItem> findAllByAuthor(String s);
}
