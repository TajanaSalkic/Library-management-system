package org.web.projekat.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.web.projekat.models.Book;
import org.web.projekat.models.Category;
import org.web.projekat.services.BookService;
import org.web.projekat.services.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String getAllCategories(Model model) {
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "Categories/category_list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String getFormCategories(Model model) {
        model.addAttribute("category", new Category());
        return "Categories/add_category";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addCategory(@Valid @ModelAttribute("category") Category category,
                              BindingResult result, Model model) {


        if (result.hasErrors()) {
            model.addAttribute("category", category);
            return "Categories/add_category";
        }
        categoryService.saveCategory(category);

        return "redirect:/category/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.findCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("category", category);
        return "Categories/edit_category";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("category") Category category,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            return "Categories/edit_category"; // Ensure the template name is correct
        }

        category.setId(id);
        categoryService.saveCategory(category);
        return "redirect:/category/";
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        Category category = categoryService.findCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));


        for (Book book : category.getBooks()) {
            book.getCategories().remove(category);
            bookService.saveBook(book);
        }

        categoryService.deleteCategoryById(id);
        return "redirect:/category/";
    }


}
