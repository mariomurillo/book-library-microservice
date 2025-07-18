package com.library.book.service;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.UUID;

import com.library.book.model.Book;
import com.library.book.util.ValidationUtil;

@ApplicationScoped
public class BookService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("libraryPU");
    
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Book createBook(Book book) {
        EntityManager em = getEntityManager();
        try {
            // Generate ISBN if not provided
            if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
                book.setIsbn(generateISBN());
            }
            ValidationUtil.validateBook(book);
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
            return book;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private String generateISBN() {
        // Generate a simple ISBN-like format using UUID
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // Take first 13 characters and format like ISBN-13
        String digits = uuid.replaceAll("[^0-9]", "").substring(0, Math.min(12, uuid.replaceAll("[^0-9]", "").length()));
        // Pad with random digits if needed
        while (digits.length() < 12) {
            digits += (int)(Math.random() * 10);
        }
        return "978-" + digits.substring(0, 1) + "-" + digits.substring(1, 3) + "-" + digits.substring(3, 9) + "-" + digits.substring(9, 12);
    }

    public List<Book> getAllBooks() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Book> query = em.getCriteriaBuilder().createQuery(Book.class);
            query.select(query.from(Book.class));
            return em.createQuery(query).getResultList();
        } finally {
            em.close();
        }
    }

    public Book getBookById(String isbn) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Book.class, isbn);
        } finally {
            em.close();
        }
    }

    public void updateBook(Book book) {
        EntityManager em = getEntityManager();
        try {
            ValidationUtil.validateBook(book);
            em.getTransaction().begin();
            em.merge(book);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteBook(String isbn) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Book book = em.find(Book.class, isbn);
            if (book != null) {
                em.remove(book);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
