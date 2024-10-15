package com.destina.Service;



import com.destina.model.User;
import com.destina.model.AccountStatus;
import com.destina.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
    private final UserRepository userRepository;

    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmail(String email) {
    	System.out.println(email);
        return userRepository.findByEmail(email);
        
    }

    public List<User> findByRoleAndAccountStatus(String role, AccountStatus status) {
        return userRepository.findByRoleAndAccountStatus(role, status);
    }

    public List<User> findUnapprovedUsers() {
        return userRepository.findByAccountStatus(AccountStatus.UNAPROOVED);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
    
    public Optional<User> findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }
}
