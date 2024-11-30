package org.web.projekat.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Getter
@Setter
@Table(name = "book")
public class Book {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty(message="Obavezno Polje!")
    @Size(min = 5, max = 100, message = "Naslov mora biti između 5 i 100 karaktera")
    private String title;

    @NotNull
    @NotEmpty(message="Obavezno Polje!")
    @Size(min = 5, max = 200, message = "Opis mora biti između 5 i 200 karaktera")
    @Column(columnDefinition = "TEXT")
    private String description;



    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )

    @NotEmpty(message = "Izaberi ili unesi novog autora")

    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )

    @NotEmpty(message = "Izaberi najmanje jednu kategoriju")
    private Set<Category> categories = new HashSet<>();

}
