package com.cringland.ni.repository;


import com.cringland.ni.repository.model.BucketItem;
import com.cringland.ni.repository.model.UserItem;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomBucketRepository {
    List<BucketItem> getSuggestions(UserItem user);
}
