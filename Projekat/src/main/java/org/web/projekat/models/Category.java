package org.web.projekat.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@Setter
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message="Please enter a name")
    @Size(min = 5, max = 100, message = "size must be between 5 and 100")
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Book> books = new HashSet<>();

// Getters and Setters

    public Set<Book> getBooks() {
        return books;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
