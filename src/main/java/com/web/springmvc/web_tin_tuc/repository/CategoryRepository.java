package com.web.springmvc.web_tin_tuc.repository;

import com.web.springmvc.web_tin_tuc.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
