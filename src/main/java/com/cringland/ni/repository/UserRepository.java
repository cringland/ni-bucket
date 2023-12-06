package com.cringland.ni.repository;


import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.cringland.ni.repository.model.UserItem;

@EnableScan
public interface UserRepository extends CrudRepository<UserItem, String> {

}
