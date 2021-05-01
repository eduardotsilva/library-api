package com.eduardo.libraryapi.service;

import com.eduardo.libraryapi.api.dto.LoanFilterDTO;
import com.eduardo.libraryapi.model.entity.Book;
import com.eduardo.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface LoanService  {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan  update(Loan loan);

    Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);

    List<Loan> getAllLateLoans();

}
