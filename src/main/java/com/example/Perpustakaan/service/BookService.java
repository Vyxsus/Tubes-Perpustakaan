package com.example.Perpustakaan.service;

import com.example.Perpustakaan.model.Book;
import com.example.Perpustakaan.repository.BookRepository;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class BookService {

    private final String uploadDir = "uploads/";

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public void addBook(Book book, MultipartFile pdfFile, MultipartFile imageFile) {
        if (!pdfFile.isEmpty()) {
            try {
                String pdfFileName = pdfFile.getOriginalFilename();
                Path pdfPath = Paths.get(uploadDir + pdfFileName);
                Files.createDirectories(pdfPath.getParent());
                Files.write(pdfPath, pdfFile.getBytes());
                book.setPdfFileName(pdfFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!imageFile.isEmpty()) {
            try {
                String imageFileName = imageFile.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir + imageFileName);
                Files.createDirectories(imagePath.getParent());
                Files.write(imagePath, imageFile.getBytes());
                book.setImageFileName(imageFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bookRepository.save(book);
    }

    public void updateBook(Long id, Book updatedBook, MultipartFile pdfFile, MultipartFile imageFile) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setDescription(updatedBook.getDescription());
            if (!pdfFile.isEmpty()) {
                try {
                    String pdfFileName = pdfFile.getOriginalFilename();
                    Path pdfPath = Paths.get(uploadDir + pdfFileName);
                    Files.createDirectories(pdfPath.getParent());
                    Files.write(pdfPath, pdfFile.getBytes());
                    book.setPdfFileName(pdfFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!imageFile.isEmpty()) {
                try {
                    String imageFileName = imageFile.getOriginalFilename();
                    Path imagePath = Paths.get(uploadDir + imageFileName);
                    Files.createDirectories(imagePath.getParent());
                    Files.write(imagePath, imageFile.getBytes());
                    book.setImageFileName(imageFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bookRepository.save(book);
        }
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Path getBookPdfPath(String pdfFileName) {
        return Paths.get(uploadDir + pdfFileName);
    }

    public Path getBookImagePath(String imageFileName) {
        return Paths.get(uploadDir + imageFileName);
    }

    public String getBookText(Long id) {
        Book book = getBookById(id);
        if (book != null && book.getPdfFileName() != null) {
            Path pdfPath = getBookPdfPath(book.getPdfFileName());
            try (PDDocument document = PDDocument.load(new File(pdfPath.toString()))) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                return pdfStripper.getText(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
