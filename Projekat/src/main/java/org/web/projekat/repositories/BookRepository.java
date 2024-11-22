package org.web.projekat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.web.projekat.models.Book;



@RepositoryRestResource
public interface BookRepository extends JpaRepository<Book, Integer> {
}