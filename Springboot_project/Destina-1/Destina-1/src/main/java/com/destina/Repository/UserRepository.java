package com.destina.Repository;



import com.destina.model.AccountStatus;
import com.destina.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRoleAndAccountStatus(String role, AccountStatus status);
    List<User> findByAccountStatus(AccountStatus status);
    Optional<User> findByResetToken(String resetToken);
}
