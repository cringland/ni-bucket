package com.cringland.ni.controller;

import java.io.IOException;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cringland.ni.model.ReviewRequest;
import com.cringland.ni.repository.ImageStorage;
import com.cringland.ni.repository.ReviewRepository;
import com.cringland.ni.repository.model.ReviewItem;
import com.cringland.ni.repository.model.ReviewKey;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewRepository reviewRepo;
    private final ImageStorage imageStorage;

    @Autowired
    public ReviewController(final ReviewRepository reviewRepo, final ImageStorage imageStorage) {
        this.reviewRepo = reviewRepo;
        this.imageStorage = imageStorage;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewItem createNewReview(@RequestBody ReviewRequest request) {
        var reviewItem = request.toItem();
        reviewItem = reviewRepo.save(reviewItem);
        LOG.info("Review Item \"" + reviewItem.getTitle() + "\" successfully created with ID " + reviewItem.getId());
        return reviewItem;
    }

    @PostMapping("/{slug}/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewItem addImageToReview(@RequestParam("image") final MultipartFile image,
                                       @PathVariable("slug") final String slug,
                                       @PathVariable("id") final String reviewId) throws IOException {
        var key = ReviewKey.builder()
                .id(reviewId)
                .bucketItemSlug(slug)
                .build();
        var reviewItem = reviewRepo.findById(key).orElseThrow(() -> new RuntimeException("Could not find review to attach photo to"));
        var list = reviewItem.getImageIds() == null ? new HashSet<String>() : reviewItem.getImageIds();

        var imageId = imageStorage.storeImage(reviewItem.getBucketItemSlug() + "/reviews", image);
        list.add(imageId);
        reviewItem = reviewItem.withImageIds(list);
        reviewItem = reviewRepo.save(reviewItem);

        LOG.info("Review Item \"" + reviewItem.getBucketItemSlug() + "/" + reviewId + "\" successfully updated with an image");
        return reviewItem;
    }
}
