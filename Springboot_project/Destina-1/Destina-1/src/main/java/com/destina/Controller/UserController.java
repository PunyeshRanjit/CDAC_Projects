package com.destina.Controller;


import com.destina.model.User;
import com.destina.model.AccountStatus;
import com.destina.Service.UserService;
import com.destina.Service.NotificationService;
import com.destina.Dto.EmailDto;
import com.destina.Dto.LoginDto;
import com.destina.Dto.LoginResponse;
import com.destina.Dto.ResetPasswordDto;
import com.destina.Dto.UserDto;
import com.destina.Service.EmailService;
import com.destina.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final NotificationService notificationService;

    public UserController(UserService userService, EmailService emailService, JwtService jwtService, NotificationService notificationService) {
        this.userService = userService;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.notificationService = notificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
    	System.out.println(user);
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User with this email already exists.");
        }

        user.setCreatedOn(LocalDateTime.now());
        user.setRole(user.getRole().toUpperCase());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        // Set account status based on role
        if (user.getRole().equals("CUSTOMER")) {
            user.setAccountStatus(AccountStatus.ACTIVE);
        } else {
            user.setAccountStatus(AccountStatus.UNAPROOVED);
        }

        userService.save(user);

        // Define email subject and body
        String subject = "Account Created";
        String message = user.getRole().equals("CUSTOMER")
                ? "Your account has been activated. You can now log in with your email and password."
                : "We have sent an approval request to the admin. Once your request is approved, you will receive a confirmation email, and you will be able to log in to your account.";

        String body = "<html><body><h1>Hello, " + user.getFirstName() + " " + user.getLastName() + "</h1>"
                + "<h2>Your account has been successfully created.</h2><p>" + message + "</p>"
                + "<h3>Thank you for your patience.</h3><p>If you have any questions or need assistance, feel free to contact our support team.</p>"
                + "<h4>Best Regards, Destina</h4></body></html>";

        try {
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error sending email: " + ex.getMessage());
        }

        notificationService.saveNotification(subject, user.getId(), 1L);

        return ResponseEntity.ok("Thank you for registering. Your account has been sent for approval. Once it is approved, you will receive an email.");
    }

    @PostMapping(value="/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
    	
        Optional<User> optionalUser = userService.findByEmail(loginDto.getEmail());
        System.out.println(optionalUser.get().getEmail());
        if (optionalUser.isEmpty() || !optionalUser.get().getRole().equalsIgnoreCase(loginDto.getRole())) {
            return ResponseEntity.status(401).body("Invalid email, role, or password.");
        }

        User user = optionalUser.get();

        if (user.getAccountStatus() == AccountStatus.UNAPROOVED) {
            return ResponseEntity.ok("Account is unapproved");
        } else if (user.getAccountStatus() == AccountStatus.BLOCKED) {
            return ResponseEntity.ok("Account is Blocked");
        } else if (!BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }

        String token = jwtService.generateToken(user);
        
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginResponse("Login successful",token, user.getId() ));

    }


    
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<User> users = userService.findByRoleAndAccountStatus("AGENT", AccountStatus.ACTIVE);
        users.addAll(userService.findByRoleAndAccountStatus("CUSTOMER", AccountStatus.ACTIVE));
        users.addAll(userService.findByRoleAndAccountStatus("CUSTOMER", AccountStatus.BLOCKED));
        users.addAll(userService.findByRoleAndAccountStatus("AGENT", AccountStatus.BLOCKED));
        
        // Log user data to check if it contains non-null values
        users.forEach(user -> {
            System.out.println("User ID: " + user.getId());
            System.out.println("User First Name: " + user.getFirstName());
            // Log other fields as needed
        });

        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getMobileNumber(),
                        user.getAddress(),
                        user.getRole(),
                        user.getAccountStatus(),
                        user.getCreatedOn()
                ))
                .collect(Collectors.toList());
                //.collect(Collectors.toList());
                

        // Log the DTOs to verify they are populated correctly
        userDtos.forEach(dto -> {
            System.out.println("DTO ID: " + dto.getId());
            System.out.println("DTO First Name: " + dto.getFirstName());
            // Log other fields as needed
        });

        return ResponseEntity.ok(userDtos);
    }


    @GetMapping("/unapproved")
    public ResponseEntity<List<UserDto>> getUnapprovedUsers() {
        List<User> users = userService.findUnapprovedUsers();

        if (users.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(user.getId(), 
                		user.getFirstName(), 
                		user.getLastName(), 
                		user.getEmail(), 
                		user.getPassword(),
                        user.getMobileNumber(), 
                        user.getAddress(), 
                        user.getRole(), 
                        user.getAccountStatus(), 
                        user.getCreatedOn()))
                //.toList();
        .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        Optional<User> user = userService.findById(id);
        
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        
        UserDto userDto = new UserDto(user.get().getId(), 
        							user.get().getFirstName(), 
        							user.get().getLastName(), 
        							user.get().getEmail(), 
        							user.get().getPassword(),
        							user.get().getMobileNumber(), 
        							user.get().getAddress(), 
        							user.get().getRole(), 
        							user.get().getAccountStatus(), 
        							user.get().getCreatedOn());
                        
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        Optional<User> existingUserOpt = userService.findById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        User existingUser = existingUserOpt.get();
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setMobileNumber(user.getMobileNumber());
        existingUser.setAddress(user.getAddress());
        existingUser.setRole(user.getRole().toUpperCase());
        

        userService.save(existingUser);

        return ResponseEntity.ok("User updated successfully!!!!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        Optional<User> user = userService.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        userService.delete(user.get());

        return ResponseEntity.ok("User deleted Successfully!!!!");
    }
    
    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approveUser(@PathVariable("id") Long id) {
        Optional<User> userOpt = userService.findById(id);
        System.out.println(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        User user = userOpt.get();
        user.setAccountStatus(AccountStatus.ACTIVE);
        userService.save(user);

        String subject = "Account Activated";
        String body = "<html><body><h1>Hello, " + user.getFirstName() + " " + user.getLastName() + "</h1>"
                + "<h2>Your account has been activated. You can now log in with your email and password.</h2>"
                + "<h3>Thanks</h3></body></html>";

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.status(500).body("User email is null or empty.");
        }

        try {
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error sending email: " + ex.getMessage());
        }

        notificationService.saveNotification(subject, user.getId(), 1L);

        return ResponseEntity.ok("Account is now active.");
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<String> blockUser(@PathVariable("id") Long id) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        User user = userOpt.get();
        user.setAccountStatus(AccountStatus.BLOCKED);
        userService.save(user);

        String subject = "Account Blocked";
        String body = "<html><body><h1>Hello, " + user.getFirstName() + " " + user.getLastName() + "</h1>"
                + "<h2>Your account has been blocked. You won't be able to log in to your account.</h2>"
                + "<h3>Thanks</h3></body></html>";

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.status(500).body("User email is null or empty.");
        }

        try {
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error sending email: " + ex.getMessage());
        }

        notificationService.saveNotification(subject, user.getId(), 1L);

        return ResponseEntity.ok("Account is now blocked.");
    }
    
    @PostMapping(value="/forget-password",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> forgetPassword(@RequestBody EmailDto emailDto) {
        System.out.println(emailDto.getEmail());
    	Optional<User> userOptional = userService.findByEmail(emailDto.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User with this email does not exist.");
        }

        User user = userOptional.get();
        String resetToken = UUID.randomUUID().toString();

        // Save the reset token to the user entity
        user.setResetToken(resetToken);
        userService.save(user);

        // Create the reset link
//        String resetLink = "/api/user/reset-password?token=" + resetToken;
        String subject = "Password Reset Request";
        String body = "<html><body><h1>Hello, " + user.getFirstName() + " " + user.getLastName() + "</h1>"
                + "<p>You requested to reset your password. Click the link below to reset it:</p>"
                + "<p>this is your reset token use this token to reset your password " + resetToken + "</p>"
                + "<p>This link will expire in 1 hour.</p>"
                + "<h4>Best Regards, Destina</h4></body></html>";

        try {
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error sending email: " + ex.getMessage());
        }

        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }
    
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        Optional<User> userOptional = userService.findByResetToken(resetPasswordDto.getToken());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(400).body("Invalid password reset token.");
        }
        
        User user = userOptional.get();
        user.setPassword(BCrypt.hashpw(resetPasswordDto.getNewPassword(), BCrypt.gensalt()));
        user.setResetToken(null); // Clear the reset token

        userService.save(user);

        return ResponseEntity.ok("Password has been successfully reset.");
    }


}

