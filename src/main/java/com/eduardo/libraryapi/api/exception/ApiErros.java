package com.eduardo.libraryapi.api.exception;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class ApiErros {
    private List<String> erros;
    public ApiErros(BindingResult bindingResult) {
        this.erros = new ArrayList<>();
        bindingResult.getAllErrors().forEach(erro -> this.erros.add(erro.getDefaultMessage()));
    }

    public List<String> getErros() {
        return erros;
    }
}
