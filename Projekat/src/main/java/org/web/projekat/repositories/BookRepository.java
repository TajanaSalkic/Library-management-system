package org.web.projekat.repositories;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.web.projekat.models.Author;
import org.web.projekat.models.Book;



@RepositoryRestResource
public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsByTitleAndAuthorsContaining(@NotNull @NotEmpty(message="Unesite Naslov") @Size(min = 1, max = 200, message = "Naslov mora biti između 1 i 200 karaktera") String title, Author author);
}