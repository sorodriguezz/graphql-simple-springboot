package org.api.graphqlsimplespringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class BookResolver {

    @Autowired
    private BookRepository bookRepository;

    @QueryMapping
    public List<Book> allBooks() {
        return bookRepository.findAll();
    }

    @QueryMapping
    public Optional<Book> bookById(@Argument Long id) {
        return bookRepository.findById(id);
    }

    @MutationMapping
    public Book createBook(@Argument String title, @Argument String author) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        return bookRepository.save(book);
    }
}