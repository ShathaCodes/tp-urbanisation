package com.TheBookShop.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.TheBookShop.model.Employee;
import com.TheBookShop.repository.EmployeeRepository;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("employees")
public class EmployeeController {

	@Autowired
	EmployeeRepository repository;

	@GetMapping("")
	public Iterable<Employee> getEmployees() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Employee getEmployee(@PathVariable int id) {
		return repository.findById(id).get();
	}
	
	@GetMapping("/{id}/salary/dollar")
	public double getSalaryDollars(@PathVariable int id) {
		Employee  employee =repository.findById(id).get();
		return employee.getSalary();
		
	}
	
	@GetMapping("/{id}/salary/euro")
	public double getSalaryEuro(@PathVariable int id) {
		double COVERSION_RATE = 0.97;
		Employee  employee =repository.findById(id).get();
		return employee.getSalary()*COVERSION_RATE;
		
	}

	@PostMapping("")
	public Employee create(@RequestBody Employee employee) {
		return repository.save(employee);

	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public class InvalidRequestException extends RuntimeException {
		public InvalidRequestException(String s) {
			super(s);
		}
	}

	@PutMapping()
	public Employee update(@RequestBody Employee employee) throws InvalidRequestException {

		if (employee == null) {
			throw new InvalidRequestException("Employee or ID must not be null!");
		}
		Optional<Employee> optionalEmployee = repository.findById(employee.getId());
		if (optionalEmployee == null || optionalEmployee.isEmpty()) {
			throw new InvalidRequestException("Employee with ID " + employee.getId() + " does not exist.");
		}
		Employee existingEmployee = optionalEmployee.get();

		existingEmployee.setName(	employee.getName());
		existingEmployee.setSalary(employee.getSalary());

		return repository.save(existingEmployee);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) throws InvalidRequestException {

		Optional<Employee> optionalEmployee = repository.findById(id);
		if (optionalEmployee == null || optionalEmployee.isEmpty()) {
			throw new InvalidRequestException("Employee with ID " + id + " does not exist.");
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}