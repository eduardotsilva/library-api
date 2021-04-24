package com.eduardo.libraryapi.service.impl;

import com.eduardo.libraryapi.api.model.entity.Book;
import com.eduardo.libraryapi.exception.BusinessException;
import com.eduardo.libraryapi.model.repository.BookRepository;
import com.eduardo.libraryapi.service.BookService;
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
        
    }

    @Override
    public Book update(Book book) {
        return null;
    }
}
