package com.cringland.ni.configuration;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.cringland.ni.repository.UserRepository;

@Configuration
@EnableDynamoDBRepositories(basePackageClasses = UserRepository.class)
public class AWSConfig {

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;
    
    private static final Regions REGION = Regions.EU_WEST_1;

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }

    @Bean
    public AWSCredentialsProvider amazonAWSCredentialsProvider(AWSCredentials awsCreds) {
        return new AWSStaticCredentialsProvider(awsCreds);
    }

    @Bean
    @Primary
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT);
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB(AWSCredentialsProvider awsCreds) {
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(awsCreds)
                .withRegion(REGION)
                .build();
    }
    
    @Bean
    public AmazonS3 s3Client(AWSCredentialsProvider awsCreds) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCreds)
                .withRegion(REGION)
                .build();
    }

    @Bean
    public AWSCognitoIdentityProvider cognitoIdentityClient(AWSCredentialsProvider awsCreds) {
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(awsCreds)
                .withRegion(REGION)
                .build();

    }
}
