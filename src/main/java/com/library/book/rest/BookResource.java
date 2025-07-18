package com.library.book.rest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import com.library.book.model.Book;
import com.library.book.service.BookService;

@Path("/books")
public class BookResource {
    @Inject
    private BookService bookService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(Book book) {
        try {
            Book created = bookService.createBook(book);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return Response.ok(books).build();
    }

    @GET
    @Path("/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn) {
        Book book = bookService.getBookById(isbn);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("isbn") String isbn, Book book) {
        try {
            // Ensure the book has the correct ISBN from the path parameter
            book.setIsbn(isbn);
            bookService.updateBook(book);
            return Response.ok(book).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("isbn") String isbn) {
        bookService.deleteBook(isbn);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @OPTIONS
    public Response handleOptions() {
        return Response.ok().build();
    }

    @OPTIONS
    @Path("/{isbn}")
    public Response handleOptionsWithPath() {
        return Response.ok().build();
    }
}
