package com.example.demo.src.user.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Lob
    @Column(name = "password")
    private String password;

    @Column(name = "phoneNumber", length = 20)
    private String phoneNumber;

    @Lob
    @Column(name = "profileImgUrl")
    private String profileImgUrl;

    @Column(name = "agreeLocation", nullable = false, length = 45)
    private String agreeLocation;

    @Column(name = "agreeMarketing", nullable = false, length = 45)
    private String agreeMarketing;

    @Column(name = "latitude", precision = 18, scale = 10)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 18, scale = 10)
    private BigDecimal longitude;

    @Column(name = "isHolic", nullable = false, length = 45)
    private String isHolic;

    @Column(name = "status", nullable = false, length = 45)
    private String status;

    @Column(name = "createdAt", nullable = false)
    private Instant createdAt;

    @Column(name = "updatedAt", nullable = false)
    private Instant updatedAt;

    @Builder
    public Users(Long id, String name, String email,   String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


}