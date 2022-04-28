package com.cringland.ni.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cringland.ni.model.BucketRequest;
import com.cringland.ni.model.BucketResponse;
import com.cringland.ni.repository.BucketRepository;
import com.cringland.ni.repository.ImageStorage;
import com.cringland.ni.repository.ReviewRepository;
import com.cringland.ni.repository.model.BucketItem;
import com.cringland.ni.repository.model.ReviewItem;

@Component
public class BucketItemService {

    private final BucketRepository bucketRepo;
    private final ReviewRepository reviewRepo;
    private final ImageStorage imageStorage;

    @Autowired
    public BucketItemService(final BucketRepository bucketRepo, final ReviewRepository reviewRepo, final ImageStorage imageStorage) {
        this.bucketRepo = bucketRepo;
        this.reviewRepo = reviewRepo;
        this.imageStorage = imageStorage;
    }

    public BucketItem saveBucketItem(final MultipartFile image,
                                     final BucketRequest properties) throws IOException {
        var slug = URLEncoder
                .encode(properties.getTitle()
                        .replaceAll("[^a-zA-Z0-9 ]", "")
                        .replace(' ', '-'), UTF_8)
                .toLowerCase();
        var imageId = imageStorage.storeImage(slug, image);
        var bucketItem = BucketItem.builder()
                .slug(slug)
                .title(properties.getTitle())
                .location(properties.getLocation())
                .photoId(imageId)
                .tags(properties.getTags())
                .author(properties.getAuthor())
                .build();
        return bucketRepo.save(bucketItem);
    }

    public double calcRating(final String slug) {
        return reviewRepo.findAllByBucketItemSlug(slug).stream()
                .mapToInt(ReviewItem::getRating)
                .average()
                .orElse(0.0);
    }

    public BucketResponse getBucketResponse(final BucketItem item) {
        var rating = calcRating(item.getSlug());
        return BucketResponse.builder()
                .author(item.getAuthor())
                .location(item.getLocation())
                .photoId(item.getPhotoId())
                .slug(item.getSlug())
                .tags(item.getTags())
                .title(item.getTitle())
                .rating(rating)
                .build();


    }
}
