package org.web.projekat.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message="Obavezno Polje!")
    @Size(min = 5, max = 100, message = "Veličina mora biti između 5 i 100 karaktera!")
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Book> books = new HashSet<>();

// Getteri i Setteri

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
