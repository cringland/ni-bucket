package com.cringland.ni.repository;


import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.cringland.ni.repository.model.BucketItem;

@EnableScan
public interface BucketRepository extends CustomBucketRepository, CrudRepository<BucketItem, String> {
}
