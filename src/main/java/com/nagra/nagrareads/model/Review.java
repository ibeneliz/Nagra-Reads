package com.nagra.nagrareads.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity()
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_book_review", columnNames = {"book_isbn", "nagra_user_username"})})
public class Review {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "book_isbn", referencedColumnName = "isbn")
    private Book book;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "nagra_user_username", referencedColumnName = "username")
    private NagraUser nagraUser;

    @NotBlank(message = "Invalid comment: Empty comment")
    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private int rating;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime reviewDate;

}
