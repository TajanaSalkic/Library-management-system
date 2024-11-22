package org.web.projekat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.web.projekat.models.Author;

import java.util.List;

@RepositoryRestResource
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findByNameContainingIgnoreCase(String name);
    boolean existsByNameContainingIgnoreCase(String name);
}
