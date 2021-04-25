package com.eduardo.libraryapi.service.impl;

import com.eduardo.libraryapi.model.entity.Loan;
import com.eduardo.libraryapi.model.repository.LoanRepository;
import com.eduardo.libraryapi.service.LoanService;

public class LoanServiceImpl implements LoanService {
    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {

        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return this.repository.save(loan);
    }
}
