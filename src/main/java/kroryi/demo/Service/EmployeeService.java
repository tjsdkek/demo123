package kroryi.demo.Service;

import kroryi.demo.domain.Employee;
import kroryi.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findDepartment(String department) {

        return employeeRepository.findByDepartment(department);
    }


}
