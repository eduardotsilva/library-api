package com.eduardo.libraryapi.api.resource;

import com.eduardo.libraryapi.api.dto.BookDTO;
import com.eduardo.libraryapi.api.model.entity.Book;
import com.eduardo.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    private BookService service;
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create (@RequestBody BookDTO dto){

        Book entity = modelMapper.map(dto,Book.class);
        entity = service.save(entity);

        return modelMapper.map(entity, BookDTO.class);
    }


}
