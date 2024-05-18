package com.web.springmvc.web_tin_tuc.repository;

import com.web.springmvc.web_tin_tuc.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByCode(String code);

    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Category findByName(String name);
}
