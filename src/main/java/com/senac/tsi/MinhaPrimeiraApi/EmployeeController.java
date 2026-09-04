package com.senac.tsi.MinhaPrimeiraApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/employee")
    public Employee createEmployee(@RequestBody Employee newEmploye){
        return repository.save(newEmploye);
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployeeById(@PathVariable long id){
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(id));
    }

    @PutMapping("/employee/{id}")
    public Optional<Employee> updateOrCreateEmployee(@RequestBody Employee newEmploye, @PathVariable long id){
        return Optional.of(repository.findById(id).map(employee -> {
            employee.setName(newEmploye.getName());
            employee.setRole(newEmploye.getRole());
            return repository.save(employee);
        }).orElseGet(() ->
                repository.save(newEmploye)));
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity deleteEmployeeById(@PathVariable long id){
        return repository.findById(id).map(
                employee -> {
                    repository.deleteById(id);
                    return ResponseEntity.status(204).build();
                }).orElseGet(() -> ResponseEntity.status(404).build());

    }

}

