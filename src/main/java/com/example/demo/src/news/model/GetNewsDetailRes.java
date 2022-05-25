package com.example.demo.src.news.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetNewsDetailRes {
    private Long reviewId;
    private String profileImgUrl;
    private String name;
    private String isHolic;
    private int reviewCount;
    private int followCount;
    private String evaluation;
    private String storeName;
    private String review;
    private String reviewCreated;
    private List<String> imgUrl;
    private int reviewLikes;
    private int reviewComments;
}