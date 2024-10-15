package com.destina;

import com.destina.model.User;
import com.destina.model.AccountStatus;
import com.destina.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Check if the user already exists to avoid duplicate entries
        if (userService.findByEmail("punyeshranjit@gmail.com").isEmpty()) {
            User user = new User();
            user.setFirstName("Punyesh");
            user.setLastName("Ranjit");
            user.setEmail("punyeshranjit@gmail.com");
            user.setPassword(BCrypt.hashpw("Pass@123", BCrypt.gensalt())); // Hash the password
            user.setMobileNumber("9834993186");
            user.setRole("ADMIN");
            user.setAddress("Mumbai");
            user.setCreatedOn(LocalDateTime.now());
            user.setAccountStatus(AccountStatus.ACTIVE);

            userService.save(user);
        }
    }
}
// localhost:8082/api/