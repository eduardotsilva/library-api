package com.eduardo.libraryapi.api.resource;

import com.eduardo.libraryapi.api.dto.BookDTO;
import com.eduardo.libraryapi.api.exception.ApiErros;
import com.eduardo.libraryapi.model.entity.Book;
import com.eduardo.libraryapi.exception.BusinessException;
import com.eduardo.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
    public BookDTO create (@RequestBody @Validated BookDTO dto){

        Book entity = modelMapper.map(dto,Book.class);
        entity = service.save(entity);

        return modelMapper.map(entity, BookDTO.class);
    }

    @GetMapping("/{id}")
    public BookDTO get(@PathVariable Long id){

        return service
                .getById(id)
                .map( (book) -> modelMapper.map(book, BookDTO.class) )
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        service.delete(book);
    }

    @PutMapping("/{id}")
    public BookDTO update(@PathVariable Long id, BookDTO dto){
       return  service.getById(id).map( book -> {
            book.setAuthor(dto.getAuthor());
            book.setTitle(dto.getTitle());
            service.update(book);
            return modelMapper.map(book, BookDTO.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


    }

    @GetMapping
    public Page<BookDTO> find(BookDTO dto, Pageable pageRequest){
        Book filter = modelMapper.map(dto, Book.class);
        Page<Book> result = service.find(filter, pageRequest);
        List<BookDTO> list = result.getContent().stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        return new ApiErros(bindingResult);

    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handlerBusinessException(BusinessException ex) {

        return new ApiErros(ex);
    }

}
