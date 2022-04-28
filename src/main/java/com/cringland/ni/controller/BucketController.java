package com.cringland.ni.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cringland.ni.exception.NotFoundException;
import com.cringland.ni.model.BucketRequest;
import com.cringland.ni.model.BucketResponse;
import com.cringland.ni.model.UpdateUsersBucketRequest;
import com.cringland.ni.repository.BucketRepository;
import com.cringland.ni.repository.ReviewRepository;
import com.cringland.ni.repository.model.BucketItem;
import com.cringland.ni.repository.model.ReviewItem;
import com.cringland.ni.service.BucketItemService;

@RestController
@RequestMapping("/api/bucket")
public class BucketController {

    private static final Logger LOG = LoggerFactory.getLogger(BucketController.class);


    private final BucketItemService bucketService;
    private final BucketRepository bucketRepo;
    private final ReviewRepository reviewRepo;
    private final UserController userController;

    @Autowired
    public BucketController(final BucketItemService bucketService, final BucketRepository bucketRepo,
                            final ReviewRepository reviewRepo, final UserController userController) {
        this.bucketService = bucketService;
        this.bucketRepo = bucketRepo;
        this.reviewRepo = reviewRepo;
        this.userController = userController;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BucketItem addNewBucketItem(@RequestPart("image") final MultipartFile image,
                                       @RequestPart("properties") final BucketRequest properties) throws IOException {
        var bucketItem = bucketService.saveBucketItem(image, properties);
        if (properties.isAddToList()) {
            userController.addBucketItem(properties.getAuthor(), UpdateUsersBucketRequest.builder()
                    .slug(bucketItem.getSlug())
                    .build()
            );
        }
        LOG.info("Bucket Item \"" + properties.getTitle() + "\" successfully created");
        return bucketItem;
    }

    @GetMapping(path = "/{slug}")
    public BucketResponse getBucketItemBySlug(@PathVariable("slug") String slug) {
        return bucketRepo.findById(slug).map(user -> {
            LOG.info("Read bucket item \"" + slug + "\" from database.");
            return user;
        }).map(bucketService::getBucketResponse)
                .orElseThrow(() -> new NotFoundException("No bucket item with slug \"" + slug + "\" was found"));
    }

    @GetMapping(path = "/{slug}/reviews")
    public List<ReviewItem> getAllReviewsForItem(@PathVariable("slug") String slug) {
        return reviewRepo.findAllByBucketItemSlug(slug);
    }

    @GetMapping(path = "/all")
    public Iterable<BucketResponse> getBucketItems() {
        LOG.info("Request to retrieve all bucket items");
        return StreamSupport.stream(bucketRepo.findAll().spliterator(), false)
                .map(bucketService::getBucketResponse).collect(Collectors.toList());
    }
}
