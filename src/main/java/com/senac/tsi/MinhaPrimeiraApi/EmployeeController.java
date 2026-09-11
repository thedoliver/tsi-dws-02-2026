package com.senac.tsi.MinhaPrimeiraApi;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {


    private final EmployeeRepository repository;

    //padrao de projeto chamado FACADE
    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employee")
    public CollectionModel<EntityModel<Employee>> getAll() {
        var employees = repository.findAll()
                .stream()
                .map(employee -> EntityModel.of(employee,
                            linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel(),
                            linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees")))
                .toList();

        return CollectionModel.of(
                employees,
                linkTo(methodOn(EmployeeController.class).getAll()).withSelfRel());
    }

    @PostMapping("/employee")
    public Employee createEmployee(@RequestBody Employee newEmploye){
        return repository.save(newEmploye);
    }

    @GetMapping("/employee/{id}")
    public EntityModel<Employee> getEmployeeById(@PathVariable long id){
        var employee = repository
                .findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(id));
        return EntityModel.of( employee,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees"));
    }

    @PutMapping("/employee/{id}")
    public Employee updateOrCreateEmployee(@RequestBody Employee newEmploye, @PathVariable long id){
        return repository.findById(id).map(employee -> {
            employee.setName(newEmploye.getName());
            employee.setRole(newEmploye.getRole());
            return repository.save(employee);
        }).orElseGet(() -> {
            newEmploye.setId(id);
            return repository.save(newEmploye);
        });
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable long id){
        return repository.findById(id).map(
                employee -> {
                    repository.deleteById(id);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());

    }

}
