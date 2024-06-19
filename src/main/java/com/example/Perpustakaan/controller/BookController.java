package com.example.Perpustakaan.controller;

import com.example.Perpustakaan.model.Book;
import com.example.Perpustakaan.model.Feedback;
import com.example.Perpustakaan.service.FeedbackService;
import com.example.Perpustakaan.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public String viewBooks(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Book> books;
        if (search != null && !search.isEmpty()) {
            books = bookService.searchBooksByTitle(search);
        } else {
            books = bookService.getAllBooks();
        }

        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        feedbacks.forEach(feedback -> {
            if (feedback.getBook() != null) { // Tambahkan pemeriksaan null di sini
                feedback.setBookTitle(feedback.getBook().getTitle());
            } else {
                feedback.setBookTitle("Unknown Book");
            }
        });

        model.addAttribute("books", books);
        model.addAttribute("feedbacks", feedbacks);
        return "books/list";
    }

    @GetMapping("/category/{category}")
    public String viewBooksByCategory(@PathVariable String category, Model model) {
        List<Book> books = bookService.getBooksByCategory(category);
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        feedbacks.forEach(feedback -> {
            if (feedback.getBook() != null) { // Tambahkan pemeriksaan null di sini
                feedback.setBookTitle(feedback.getBook().getTitle());
            } else {
                feedback.setBookTitle("Unknown Book");
            }
        });

        model.addAttribute("books", books);
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("category", category);
        return "books/listByCategory";
    }



    @GetMapping("/pdf/{id}")
    public ResponseEntity<PathResource> downloadPdf(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book == null || book.getPdfFileName() == null) {
            return ResponseEntity.notFound().build();
        }
        Path path = bookService.getBookPdfPath(book.getPdfFileName());
        PathResource resource = new PathResource(path);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + book.getTitle() + ".pdf")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(resource);
    }

    @GetMapping("/readText/{id}")
    public String readBookText(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "error";
        }
        String bookText = bookService.getBookText(id);
        model.addAttribute("book", book);
        model.addAttribute("bookText", bookText);

        List<Feedback> feedbackList = feedbackService.getFeedbacksByBookId(id);
        model.addAttribute("feedbackList", feedbackList);

        return "books/readText";
    }

    @GetMapping("/image/{imageFileName}")
    public ResponseEntity<PathResource> getImage(@PathVariable String imageFileName) {
        Path path = bookService.getBookImagePath(imageFileName);
        PathResource resource = new PathResource(path);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(resource);
    }

    @PostMapping("/feedback")
    public String submitFeedback(@RequestParam Long bookId,
                                @RequestParam String question1,
                                @RequestParam String question2,
                                @RequestParam String question3) {
        Feedback feedback = new Feedback();
        feedback.setId(bookId);
        feedback.setQuestion1(question1);
        feedback.setQuestion2(question2);
        feedback.setQuestion3(question3);

        feedbackService.saveFeedback(feedback);

        return "redirect:/books/readText/" + bookId;
    }

    @GetMapping("/detail/{id}")
    public String viewBookDetail(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            System.out.println("Book with id " + id + " not found.");
            return "error";
        }
        model.addAttribute("book", book);
        return "books/detail";
    }

}
