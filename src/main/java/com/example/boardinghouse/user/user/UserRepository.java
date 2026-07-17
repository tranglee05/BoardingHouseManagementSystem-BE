package com.example.boardinghouse.user.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    
    // Tìm danh sách khách thuê thuộc sự quản lý của một chủ trọ
    List<User> findByLandlordId(Long landlordId);
}
