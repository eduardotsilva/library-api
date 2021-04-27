package com.eduardo.libraryapi.service.impl;

import com.eduardo.libraryapi.exception.BusinessException;
import com.eduardo.libraryapi.model.entity.Loan;
import com.eduardo.libraryapi.model.repository.LoanRepository;
import com.eduardo.libraryapi.service.LoanService;

import java.util.Optional;

public class LoanServiceImpl implements LoanService {
    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {

        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if ( repository.existsByBookAndNotReturned(loan.getBook()) ) {
            throw new BusinessException("Livro já emprestado");
        }
        return this.repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void update(Loan loan) {

    }
}
