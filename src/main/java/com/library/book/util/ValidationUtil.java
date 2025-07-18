package com.library.book.util;

import com.library.book.model.Book;

public class ValidationUtil {
    public static void validateBook(Book book) {
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN is required");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required");
        }
        int currentYear = java.time.LocalDate.now().getYear();
        if (book.getPublicationYear() < 1 || book.getPublicationYear() > currentYear) {
            throw new IllegalArgumentException("Publication year must be between 1 and " + currentYear);
        }
    }
}
