package com.example.th02.service;

import com.example.th02.models.Category;
import com.example.th02.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Lấy toàn bộ danh sách danh mục
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Tìm danh mục theo ID
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Thêm mới danh mục
     */
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Cập nhật danh mục
     */
    public Category updateCategory(Category category) {
        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalStateException("Category with ID " + category.getId() + " does not exist."));
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    /**
     * Xóa danh mục theo ID
     */
    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalStateException("Category with ID " + id + " does not exist.");
        }
        categoryRepository.deleteById(id);
    }
}
