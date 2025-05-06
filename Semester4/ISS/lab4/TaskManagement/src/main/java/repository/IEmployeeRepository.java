package repository;

import domain.Employee;

import java.util.Optional;

public interface IEmployeeRepository extends Repository<Integer, Employee> {
    Optional<Employee> findByUsername(String username);
}
