package com.example.th02.controller;

import com.example.th02.models.Product;
import com.example.th02.service.CategoryService;
import com.example.th02.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    /**
     * Trang 1: Hiển thị danh sách sản phẩm & các nút chức năng
     * URL: http://localhost:9999/products
     */
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/products-list";
    }

    /**
     * Trang 2: Hiển thị form thêm mới sản phẩm
     * URL: http://localhost:9999/products/add
     */
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/add-product";
    }

    /**
     * Xử lý lưu form thêm mới sản phẩm
     */
    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
        productService.addProduct(product);
        redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm thành công!");
        return "redirect:/products";
    }

    /**
     * Trang 3: Hiển thị form chỉnh sửa sản phẩm
     * URL: http://localhost:9999/products/edit/{id} hoặc /products/update/{id}
     */
    @GetMapping({"/edit/{id}", "/update/{id}"})
    public String showUpdateProductForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm với ID: " + id);
            return "redirect:/products";
        }
        model.addAttribute("product", productOpt.get());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/update-product";
    }

    /**
     * Xử lý cập nhật sản phẩm
     */
    @PostMapping({"/update/{id}", "/edit/{id}", "/update"})
    public String updateProduct(@PathVariable(value = "id", required = false) Long pathId,
                                @ModelAttribute("product") Product product,
                                RedirectAttributes redirectAttributes) {
        if (pathId != null) {
            product.setId(pathId);
        }
        productService.updateProduct(product);
        redirectAttributes.addFlashAttribute("message", "Cập nhật sản phẩm thành công!");
        return "redirect:/products";
    }

    /**
     * Xử lý xóa sản phẩm
     * URL: http://localhost:9999/products/delete/{id}
     */
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProductById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa sản phẩm này: " + ex.getMessage());
        }
        return "redirect:/products";
    }
}
