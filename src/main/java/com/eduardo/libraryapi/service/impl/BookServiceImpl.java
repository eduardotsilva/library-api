package com.eduardo.libraryapi.service.impl;

import com.eduardo.libraryapi.model.entity.Book;
import com.eduardo.libraryapi.exception.BusinessException;
import com.eduardo.libraryapi.model.repository.BookRepository;
import com.eduardo.libraryapi.service.BookService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Isbn já cadastrado.");

        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
       return this.repository.findById(id);
    }

    @Override
    public void delete(Book book) {
        if (book == null ||book.getId() == null ){
            throw new IllegalArgumentException("Book não pode ser null");
        }
        this.repository.delete(book);
    }

    @Override
    public Book update(Book book) {
        if (book == null ||book.getId() == null ){
            throw new IllegalArgumentException("Book não pode ser null");
        }
        return this.repository.save(book);
    }

    @Override
    public Page<Book> find(Book filter, Pageable pageRequest) {
        Example<Book> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING )
        )               ;
        return repository.findAll(example, pageRequest);
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return this.repository.findByIsbn(isbn);
    }
}
