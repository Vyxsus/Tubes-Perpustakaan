package com.example.Perpustakaan.service;

import com.example.Perpustakaan.model.Book;
import com.example.Perpustakaan.model.Feedback;
import com.example.Perpustakaan.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private BookService bookService;

    public List<Feedback> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        for (Feedback feedback : feedbacks) {
            Book book = feedback.getBook();
            if (book != null) {
                feedback.setBookTitle(book.getTitle());
            } else {
                feedback.setBookTitle("Unknown Book");
            }
        }
        return feedbacks;
    }

    public List<Feedback> getFeedbacksByBookId(Long bookId) {
        List<Feedback> feedbacks = feedbackRepository.findByBookId(bookId);
        for (Feedback feedback : feedbacks) {
            Book book = feedback.getBook();
            if (book != null) {
                feedback.setBookTitle(book.getTitle());
            } else {
                feedback.setBookTitle("Unknown Book");
            }
        }
        return feedbacks;
    }

    public void saveFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }
}

