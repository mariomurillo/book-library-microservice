package com.library.book;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.TypedQuery;
import java.util.List;
import java.lang.reflect.Field;

import com.library.book.service.BookService;
import com.library.book.model.Book;

public class BookServiceTest {

    private BookService bookService;
    private EntityManager mockEntityManager;
    private EntityManagerFactory mockEntityManagerFactory;
    private EntityTransaction mockTransaction;

    @Before
    public void setUp() throws Exception {
        mockEntityManager = Mockito.mock(EntityManager.class);
        mockEntityManagerFactory = Mockito.mock(EntityManagerFactory.class);
        mockTransaction = Mockito.mock(EntityTransaction.class);
        
        // Setup mock behavior
        Mockito.when(mockEntityManagerFactory.createEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);
        
        bookService = new BookService();
        // Inject mock EntityManagerFactory using reflection
        Field emfField = BookService.class.getDeclaredField("emf");
        emfField.setAccessible(true);
        emfField.set(bookService, mockEntityManagerFactory);
    }

    @Test
    public void testCreateBook_Success() {
        Book testBook = new Book("978-3-16-148410-0", "Test Book", "Test Author", 2023);
        bookService.createBook(testBook);
        Mockito.verify(mockEntityManager).persist(testBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBook_NullISBN() {
        Book invalidBook = new Book(null, "Test Book", "Test Author", 2023);
        bookService.createBook(invalidBook);
    }

    @Test
    public void testGetAllBooks() {
        // Mock query execution setup
        CriteriaBuilder builder = Mockito.mock(CriteriaBuilder.class);
        Mockito.when(mockEntityManager.getCriteriaBuilder()).thenReturn(builder);

        CriteriaQuery<Book> query = Mockito.mock(CriteriaQuery.class);
        Mockito.when(builder.createQuery(Book.class)).thenReturn(query);

        Root<Book> root = Mockito.mock(Root.class);
        Mockito.when(query.from(Book.class)).thenReturn(root);

        TypedQuery<Book> typedQuery = Mockito.mock(TypedQuery.class);
        Mockito.when(mockEntityManager.createQuery(query)).thenReturn(typedQuery);

        // Mock query result
        List<Book> mockBooks = List.of(new Book("ISBN1", "Mock Book", "Mock Author", 2023));
        Mockito.when(typedQuery.getResultList()).thenReturn(mockBooks);

        // Execute method and verify
        List<Book> result = bookService.getAllBooks();
        assertEquals(mockBooks, result);
    }
}
