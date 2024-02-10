package com.main.writeRoom.repository;

import com.main.writeRoom.domain.User.ExistedEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExistedEmailRespository extends JpaRepository<ExistedEmail, Long> {
    ExistedEmail findByResetToken(String resetToken);
}
