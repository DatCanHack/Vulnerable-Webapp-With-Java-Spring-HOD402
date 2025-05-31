package com.example.webapp.controller;

import com.example.webapp.model.Product;
import com.example.webapp.model.User;
import com.example.webapp.repository.ProductRepository;
import com.example.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "products/list";
    }

    @GetMapping("/manage")
    public String manageProducts(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        boolean isSeller = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SELLER"));
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // Admin: show all products
            model.addAttribute("products", productRepository.findAll());
        } else if (isSeller) {
            // Seller: show only their products
            User seller = userService.getCurrentUser(authentication);
            if (seller == null) {
                return "redirect:/login";
            }
            model.addAttribute("products", productRepository.findBySeller(seller));
        } else {
            return "redirect:/login";
        }
        return "products/manage";
}


    @GetMapping("/add")
    public String showAddForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SELLER"))) {
            return "redirect:/login";
        }
        
        model.addAttribute("product", new Product());
        return "products/form";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, 
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        try {
            User seller = userService.getCurrentUser(authentication);
            if (seller == null) {
                redirectAttributes.addFlashAttribute("error", "You must be logged in to add products");
                return "redirect:/login";
            }

            product.setSeller(seller);
            productRepository.save(product);
            
            redirectAttributes.addFlashAttribute("success", "Product added successfully");
            return "redirect:/products/manage";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding product: " + e.getMessage());
            return "redirect:/products/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/products/manage";
        }

        User currentUser = userService.getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Check if user is admin or the seller of the product
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
            product.getSeller().getId().equals(currentUser.getId())) {
            model.addAttribute("product", product);
            return "products/form";
        }

        return "redirect:/products/manage";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, 
                              @ModelAttribute Product product, 
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            Product existingProduct = productRepository.findById(id).orElse(null);
            if (existingProduct == null) {
                redirectAttributes.addFlashAttribute("error", "Product not found");
                return "redirect:/products/manage";
            }

            User currentUser = userService.getCurrentUser(authentication);
            if (currentUser == null) {
                return "redirect:/login";
            }

            // Check if user is admin or the seller of the product
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                existingProduct.getSeller().getId().equals(currentUser.getId())) {
                
                // Keep the original seller
                product.setId(id);
                product.setSeller(existingProduct.getSeller());
                productRepository.save(product);
                
                redirectAttributes.addFlashAttribute("success", "Product updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "You don't have permission to edit this product");
            }
            
            return "redirect:/products/manage";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating product: " + e.getMessage());
            return "redirect:/products/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, 
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            Product product = productRepository.findById(id).orElse(null);
            if (product == null) {
                redirectAttributes.addFlashAttribute("error", "Product not found");
                return "redirect:/products/manage";
            }

            User currentUser = userService.getCurrentUser(authentication);
            if (currentUser == null) {
                return "redirect:/login";
            }

            // Check if user is admin or the seller of the product
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                product.getSeller().getId().equals(currentUser.getId())) {
                
                productRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Product deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "You don't have permission to delete this product");
            }
            
            return "redirect:/products/manage";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
            return "redirect:/products/manage";
        }
    }
} 