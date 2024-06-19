package com.example.Perpustakaan.controller;

import com.example.Perpustakaan.model.Book;
import com.example.Perpustakaan.model.Feedback;
import com.example.Perpustakaan.service.BookService;
import com.example.Perpustakaan.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookService bookService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/home")
    public String adminHome(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Book> books;
        if (search != null && !search.isEmpty()) {
            books = bookService.searchBooksByTitle(search);
        } else {
            books = bookService.getAllBooks();
        }
        model.addAttribute("books", books);
        return "admin/home";
    }

    @GetMapping("/feedback")
    public String viewFeedbacks(Model model) {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        model.addAttribute("feedbacks", feedbacks);
        return "admin/feedback";
    }

    @GetMapping("/addBook")
    public ModelAndView addBookForm() {
        ModelAndView mav = new ModelAndView();
        Book book = new Book();
        mav.addObject("book", book);
        mav.setViewName("admin/addBook");
        return mav;
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book, @RequestParam("pdfFile") MultipartFile pdfFile, @RequestParam("imageFile") MultipartFile imageFile) {
        bookService.addBook(book, pdfFile, imageFile);
        return "redirect:/admin/home";
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "error"; // Handle book not found
        }
        model.addAttribute("book", book);
        return "admin/editBook";
    }

    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, @ModelAttribute Book book, @RequestParam("pdfFile") MultipartFile pdfFile, @RequestParam("imageFile") MultipartFile imageFile) {
        bookService.updateBook(id, book, pdfFile, imageFile);
        return "redirect:/admin/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/home";
    }
}
