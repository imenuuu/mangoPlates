package com.example.demo.src.user.repository;

import com.example.demo.src.user.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Query(nativeQuery = true,
            value="SELECT * from Users ORDER BY id DESC")
    List<Users> findAllDesc();

}