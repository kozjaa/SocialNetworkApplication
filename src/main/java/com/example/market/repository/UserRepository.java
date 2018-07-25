package com.example.market.repository;

import com.example.market.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT u FROM User u WHERE u.username = :username")
    User getUserByName(@Param("username") String username);
}
