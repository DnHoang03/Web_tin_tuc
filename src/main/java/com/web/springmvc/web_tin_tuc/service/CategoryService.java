package com.web.springmvc.web_tin_tuc.service;

import com.web.springmvc.web_tin_tuc.dto.CategoryDTO;
import com.web.springmvc.web_tin_tuc.exception.CategoryNotFoundException;
import com.web.springmvc.web_tin_tuc.model.Category;
import com.web.springmvc.web_tin_tuc.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.save(mapToEntity(categoryDTO));
        return categoryDTO;
    }

    public CategoryDTO getCategoryById(Integer id) {
        return mapToDTO(categoryRepository.findById(id).orElseThrow(()-> new CategoryNotFoundException("Not found category")));
    }

    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new CategoryNotFoundException("Not found category"));
        categoryRepository.deleteById(id);
    }


    private Category mapToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        if(categoryDTO.getId() != null) {
            category.setId(categoryDTO.getId());
        }
        category.setCode(categoryDTO.getCode());
        category.setName(categoryDTO.getName());
        return category;
    }
    private CategoryDTO mapToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setCode(category.getCode());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

}
