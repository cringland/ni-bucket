package com.cringland.ni.model;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo {

    private String id;
    private Set<String> tags;
    private List<PersonalBucket> bucketList;
}
