package org.web.projekat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.projekat.models.Author;
import org.web.projekat.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;
@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> findAuthorById(Long id) {
        return authorRepository.findById(id.intValue());
    }

    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    public void deleteAuthorById(Long id) {
        authorRepository.deleteById(id.intValue());
    }

    public List<Author> findAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

    public boolean findAuthorsByNameBool(String name) {
        return authorRepository.existsByNameContainingIgnoreCase(name);
    }
}
