package com.example.Perpustakaan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.Perpustakaan.model.Transaction;
import com.example.Perpustakaan.service.UserService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/new")
    public String newTransactionForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transactions/form";
    }
    
    @PostMapping
    public String saveTransaction(Transaction transaction) {
        userService.saveTransaction(transaction);
        return "redirect:/transactions";
    }
}