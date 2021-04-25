package com.eduardo.libraryapi.api.exception;

import com.eduardo.libraryapi.exception.BusinessException;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErros {
    private List<String> erros;
    public ApiErros(BindingResult bindingResult) {
        this.erros = new ArrayList<>();
        bindingResult.getAllErrors().forEach(erro -> this.erros.add(erro.getDefaultMessage()));
    }

    public ApiErros(BusinessException ex) {
        this.erros = Arrays.asList(ex.getMessage());
    }

    public List<String> getErros() {
        return erros;
    }

    public ApiErros(ResponseStatusException ex) {
        this.erros = Arrays.asList(ex.getReason());
    }


}
