package com.eduardo.libraryapi.service;

import com.eduardo.libraryapi.model.entity.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService  {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan  update(Loan loan);
}
