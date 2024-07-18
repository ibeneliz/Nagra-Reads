package com.nagra.nagrareads.repository;

import com.nagra.nagrareads.model.NagraUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NagraUserRepository extends JpaRepository<NagraUser, Long> {

    Optional<NagraUser> findByUsername(String username);
}