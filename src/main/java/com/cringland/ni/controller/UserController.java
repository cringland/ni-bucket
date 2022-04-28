package com.cringland.ni.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.cringland.ni.repository.ReviewRepository;
import com.cringland.ni.repository.model.BucketItem;
import com.cringland.ni.repository.model.ReviewItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminConfirmSignUpRequest;
import com.cringland.ni.exception.NotFoundException;
import com.cringland.ni.model.PersonalBucket;
import com.cringland.ni.model.UpdateUsersBucketRequest;
import com.cringland.ni.model.UserCreateRequest;
import com.cringland.ni.model.UserInfo;
import com.cringland.ni.repository.BucketRepository;
import com.cringland.ni.repository.UserRepository;
import com.cringland.ni.repository.model.UserItem;
import com.cringland.ni.service.BucketItemService;

import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/user")
@Log
public class UserController {

    private final UserRepository userRepository;
    private final AWSCognitoIdentityProvider cognito;
    private final BucketItemService bucketItemService;
    private final BucketRepository bucketRepository;
    private final String userPoolId;

    @Autowired
    public UserController(final UserRepository userRepository,
                          final AWSCognitoIdentityProvider cognito,
                          final BucketItemService bucketItemService,
                          final BucketRepository bucketRepository,
                          @Value("${amazon.cognito.userpool.id}") final String userPoolId) {

        this.userRepository = userRepository;
        this.cognito = cognito;
        this.bucketItemService = bucketItemService;
        this.bucketRepository = bucketRepository;
        this.userPoolId = userPoolId;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfo addNewUser(@RequestBody UserCreateRequest request) {
        var username = request.getUsername();
        var cognitoRequest = new AdminConfirmSignUpRequest()
                .withUserPoolId(userPoolId)
                .withUsername(username);
        cognito.adminConfirmSignUp(cognitoRequest);

        userRepository.save(new UserItem(username, request.getTags(), null));
        log.info(username + " user profile successfully created in DB and cognito");
        return new UserInfo(username, request.getTags(), null);
    }

    @GetMapping(path = "/{username}")
    public UserInfo getUserByUsername(@PathVariable("username") String username) {
        return userRepository.findById(username).map(user -> {
            log.info("Reading user with id " + username + " from database.");
            return getUserInfo(user);
        }).orElseThrow(() -> {
            log.info("User did not exist when retrieving their info " + username);
            return new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        });
    }

    @PutMapping(path = "/{username}/bucket")
    public UserInfo addBucketItem(@PathVariable("username") String username, @RequestBody UpdateUsersBucketRequest request) {
        return userRepository.findById(username).map(user -> {
            var newUser = getNewUser(request, user);
            userRepository.save(newUser);
            return getUserInfo(newUser);
        }).orElseThrow(() -> new NotFoundException("The user with the id " + username + " couldn't be found in the database."));
    }

    @GetMapping(path = "/{username}/suggestions")
    public List<BucketItem> getSuggestions(@PathVariable("username") String username) {
        return userRepository.findById(username).map(bucketRepository::getSuggestions)
                .orElseThrow(() -> new NotFoundException("The user with the id " + username + " couldn't be found in the database."));
    }


    private UserInfo getUserInfo(UserItem user) {
        var bucketIds = user.getBucketList().stream()
                .map(map -> map.get("slug"))
                .collect(Collectors.toList());
        var bucketItems = bucketRepository.findAllById(bucketIds);

        var personalBuckets = StreamSupport.stream(bucketItems.spliterator(), false)
                .map(item -> {
                    var slug = item.getSlug();
                    var usersEntry = user.getBucketList().stream()
                            .filter(map -> map.get("slug").equals(slug)).findFirst()
                            .get();
                    var completionDate = usersEntry.get("completedDate");
                    return PersonalBucket.builder()
                            .slug(slug)
                            .title(item.getTitle())
                            .photoId(item.getPhotoId())
                            .completedDate(completionDate)
                            .author(item.getAuthor())
                            .tags(item.getTags())
                            .rating(bucketItemService.calcRating(slug))
                            .build();
                }).collect(Collectors.toList());

        return UserInfo.builder()
                .id(user.getId())
                .bucketList(personalBuckets)
                .build();
    }

    private UserItem getNewUser(final UpdateUsersBucketRequest request, final UserItem user) {
        var newItem = newItemMap(request);
        final List<Map<String, String>> newList;
        if (user.getBucketList() == null) {
            newList = List.of(newItem);
        } else {
            newList = Stream.concat(user.getBucketList().stream()
                            .filter(map -> !map.get("slug").equals(request.getSlug())), Stream.of(newItem))
                    .collect(Collectors.toList());
        }
        return user.withBucketList(newList);
    }

    private Map<String, String> newItemMap(final UpdateUsersBucketRequest request) {
        var newItem = new HashMap<String, String>();
        newItem.put("slug", request.getSlug());
        if (request.getDateCompleted() != null)
            newItem.put("completedDate", request.getDateCompleted());
        return newItem;
    }
}
