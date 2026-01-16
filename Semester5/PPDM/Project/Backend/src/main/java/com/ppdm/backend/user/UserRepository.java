package com.ppdm.backend.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    Page<UserEntity> findByUsernameContainingIgnoreCase(Pageable pageable, String username);

    Page<UserEntity> findByActiveTrue(Pageable pageable);

    Page<UserEntity> findByUsernameContainingIgnoreCaseAndActiveTrue(Pageable pageable, String username);

}
