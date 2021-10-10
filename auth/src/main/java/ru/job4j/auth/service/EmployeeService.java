package ru.job4j.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    private static final String API = "http://localhost:8080/person/";
    private static final String API_ID = "http://localhost:8080/person/{id}";

    private final EmployeeRepository repository;

    @Autowired
    private RestTemplate rest;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> findAll() {
        List<Employee> employees = (List<Employee>) repository.findAll();
        List<Person> persons = rest.exchange(
                API,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Person>>() {
                }
        ).getBody();
        employees.forEach(employee -> persons.stream()
                .filter(person -> person.getEmployee().equals(employee))
                .forEach(employee::addAccount));
        return employees;
    }

    public Person createAccount(Person person) {
        Employee employee = repository.findById(person.getEmployee().getId())
                .orElse(new Employee());
        person.setEmployee(employee);
        return rest.postForObject(API, person, Person.class);
    }

    public void updateAccount(Person person) {
        rest.put(API, person);
    }

    public void deleteAccount(int id) {
        rest.delete(API_ID, id);
    }
}
