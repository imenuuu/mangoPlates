package com.example.demo.src.user.model;

import com.example.demo.src.user.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserListRes {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String profileImgUrl;
    private String agreeLocation;
    private String agreeMarketing;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String isHolic;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;


    public GetUserListRes(Users entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.password = entity.getPassword();
        this.profileImgUrl = entity.getProfileImgUrl();
        this.agreeLocation = entity.getAgreeLocation();
        this.agreeMarketing = entity.getAgreeMarketing();
        this.latitude = entity.getLatitude();
        this.longitude = entity.getLongitude();
        this.isHolic = entity.getIsHolic();
        this.status = entity.getStatus();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }



}
