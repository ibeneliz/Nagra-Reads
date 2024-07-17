package com.nagra.Nagra.Reads.repository;

import com.nagra.Nagra.Reads.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}