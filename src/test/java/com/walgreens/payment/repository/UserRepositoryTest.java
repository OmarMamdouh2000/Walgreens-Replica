package com.walgreens.payment.repository;

import com.walgreens.payment.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveMethod(){
        // Create User
        User user = new User();
        user.setFirstName("Karim");
        user.setLastName("Abouzeid");

        //Save User
        User savedUser = userRepository.save(user);

        //Display user info
         System.out.println(savedUser.toString());
    }

    @Test
    void updateUsingSaveMethod(){
        // find or retrieve an entity by id
        UUID uuid = UUID.fromString("7f8df2bd-ab57-4018-8e77-ee704c354ee4");
        User user = userRepository.findById(uuid).get();

        // update entity information
        user.setFirstName("Omar");
        user.setLastName("3atchef");

        // save the updated entity
        userRepository.save(user);

    }

    @Test
    void saveAllMethod() {
        // Create User
        User user = new User();
        user.setFirstName("Karim");
        user.setLastName("Abouzeid");

        // Create User2
        User user2 = new User();
        user2.setFirstName("Maram");
        user2.setLastName("Fetteha");

        userRepository.saveAll(List.of(user, user2));

    }


}