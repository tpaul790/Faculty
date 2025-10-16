package com.ppdm.backend.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>, JpaSpecificationExecutor<UserEntity> {

    UserEntity findByUsername(String username);

    List<UserEntity> findAllByOrderById();

    boolean existsByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.active = true, u.lastActive = CURRENT_TIMESTAMP WHERE u.id = :id")
    void activateUser(@Param("id") Long id);
}
