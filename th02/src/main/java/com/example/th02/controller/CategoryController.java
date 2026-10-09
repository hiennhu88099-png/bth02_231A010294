package com.example.th02.controller;

import com.example.th02.models.Category;
import com.example.th02.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Trang 1: Hiển thị danh sách danh mục & các nút chức năng
     * URL: http://localhost:9999/categories
     */
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories/categories-list";
    }

    /**
     * Trang 2: Hiển thị form thêm mới danh mục
     * URL: http://localhost:9999/categories/add
     */
    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/add-category";
    }

    /**
     * Xử lý lưu form thêm mới danh mục
     */
    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") Category category, RedirectAttributes redirectAttributes) {
        categoryService.addCategory(category);
        redirectAttributes.addFlashAttribute("message", "Thêm danh mục thành công!");
        return "redirect:/categories";
    }

    /**
     * Trang 3: Hiển thị form chỉnh sửa danh mục
     * URL: http://localhost:9999/categories/edit/{id} hoặc /categories/update/{id}
     */
    @GetMapping({"/edit/{id}", "/update/{id}"})
    public String showUpdateCategoryForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        if (categoryOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy danh mục với ID: " + id);
            return "redirect:/categories";
        }
        model.addAttribute("category", categoryOpt.get());
        return "categories/update-category";
    }

    /**
     * Xử lý cập nhật danh mục
     */
    @PostMapping({"/update/{id}", "/edit/{id}", "/update"})
    public String updateCategory(@PathVariable(value = "id", required = false) Long pathId,
                                 @ModelAttribute("category") Category category,
                                 RedirectAttributes redirectAttributes) {
        if (pathId != null) {
            category.setId(pathId);
        }
        categoryService.updateCategory(category);
        redirectAttributes.addFlashAttribute("message", "Cập nhật danh mục thành công!");
        return "redirect:/categories";
    }

    /**
     * Xử lý xóa danh mục
     * URL: http://localhost:9999/categories/delete/{id}
     */
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategoryById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa danh mục này (có thể do danh mục đang chứa sản phẩm liên kết).");
        }
        return "redirect:/categories";
    }
}
