package kroryi.demo.Service;

import kroryi.demo.domain.Department;
import kroryi.demo.domain.Employee;
import kroryi.demo.repository.DepartmentRepository;
import kroryi.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    // 특정 부서에 포함된 직원 목록
    public List<Employee> getEmployeesByDepartment(String departmentName) {
        Department department = departmentRepository.findDepartmentByDepartName(departmentName);
        // department 에는 id=1, depart_name=회계부
        return employeeRepository.findByDepartment(department);
    }

    // 모든 직원 조회
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    // 모든 부서 조회
    public List<Department> getAllDepartments(){
        return departmentRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {

        return employeeRepository.findById(id).orElseThrow();
    }


}
