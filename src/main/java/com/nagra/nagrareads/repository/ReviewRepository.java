package com.nagra.nagrareads.repository;

import com.nagra.nagrareads.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByBookIsbnAndNagraUserUsername(String isbn, String username);

    List<Review> findAllByBookIsbn(String isbn);
}