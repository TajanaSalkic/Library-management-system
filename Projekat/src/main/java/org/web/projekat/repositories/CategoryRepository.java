package org.web.projekat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.web.projekat.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
