package com.senac.tsi.MinhaPrimeiraApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeeController {


    private EmployeeRepository repository;

    //padrao de projeto chamado FACADE
    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employee")
    public List<Employee> getAll() {
        return repository.findAll();
    }

}

