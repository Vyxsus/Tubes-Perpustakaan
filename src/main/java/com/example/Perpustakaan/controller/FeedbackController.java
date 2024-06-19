package com.example.Perpustakaan.controller;

import com.example.Perpustakaan.model.Book;
import com.example.Perpustakaan.model.Feedback;
import com.example.Perpustakaan.service.BookService;
import com.example.Perpustakaan.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private BookService bookService;

    @GetMapping("/{bookId}")
    public String feedbackForm(@PathVariable Long bookId, Model model) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            return "error"; // Handle book not found
        }
        Feedback feedback = new Feedback();
        feedback.setBook(book); // Set the book here
        model.addAttribute("feedback", feedback);
        return "feedback/form";
    }

    @PostMapping("/{bookId}")
    public String submitFeedback(@PathVariable Long bookId, @ModelAttribute Feedback feedback) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            return "error"; // Handle book not found
        }
        feedback.setBook(book); // Set the book before saving the feedback
        feedbackService.saveFeedback(feedback);
        return "redirect:/books/readText/" + bookId;
    }
}

