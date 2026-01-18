package poly.edu.java5_asm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/category")
    public String category(Model model) {
        return "category";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        // TODO: Load product by id
        return "product-detail";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        return "checkout";
    }

    @GetMapping("/shipping")
    public String shipping(Model model) {
        return "shipping";
    }

    @GetMapping("/payment")
    public String payment(Model model) {
        return "payment";
    }

    @GetMapping("/favourite")
    public String favourite(Model model) {
        return "favourite";
    }

    @GetMapping("/add-new-card")
    public String addNewCard(Model model) {
        return "add-new-card";
    }

    @GetMapping("/reset-password")
    public String resetPassword() {
        return "reset-password";
    }

    @GetMapping("/reset-password-emailed")
    public String resetPasswordEmailed() {
        return "reset-password-emailed";
    }
}
