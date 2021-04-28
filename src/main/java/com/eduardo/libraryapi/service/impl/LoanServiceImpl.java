package com.eduardo.libraryapi.service.impl;

import com.eduardo.libraryapi.api.dto.LoanFilterDTO;
import com.eduardo.libraryapi.exception.BusinessException;
import com.eduardo.libraryapi.model.entity.Loan;
import com.eduardo.libraryapi.model.repository.LoanRepository;
import com.eduardo.libraryapi.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
        return repository.findById(id);
    }

    @Override
    public Loan  update(Loan loan) {

        return  repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable) {
        return repository.findByBookIsbnOrCustomer(filterDTO.getIsbn(),filterDTO.getCustomer(),pageable);
    }
}
