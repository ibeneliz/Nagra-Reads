package com.nagra.nagrareads.repository;

import com.nagra.nagrareads.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}