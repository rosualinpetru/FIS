package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.exceptions.MissingDepartmentException;
import ro.go.redhomeserver.tom.exceptions.SignUpException;
import ro.go.redhomeserver.tom.exceptions.UsedEmailException;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.HolidayRequest;
import ro.go.redhomeserver.tom.repositories.DepartmentRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.HolidayRequestRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class HRService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final HolidayRequestRepository holidayRequestRepository;

    @Autowired
    public HRService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, HolidayRequestRepository holidayRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.holidayRequestRepository = holidayRequestRepository;
    }

    public void checkIfEmailIsAvailable(Map<String, String> params) throws SignUpException {
        if (employeeRepository.findByEmail(params.get("email")).isPresent())
            throw new UsedEmailException("The email is already used!");
    }

    public String addEmployee(Map<String, String> params) throws MissingDepartmentException {
        Date date;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(params.get("emp-date"));
        } catch (ParseException e) {
            date = new Date();
        }

        Optional<Department> departmentOptional = departmentRepository.findById(params.get("departmentId"));
        if (departmentOptional.isPresent()) {
            Employee newEmployee = new Employee(
                    params.get("name"),
                    params.get("address"),
                    params.get("phone"),
                    Integer.parseInt(params.get("salary")),
                    params.get("email"),
                    date,
                    departmentOptional.get()
            );
            employeeRepository.save(newEmployee);
            return newEmployee.getId();
        }
        throw new MissingDepartmentException("Department could not be found!");
    }

    public List<HolidayRequest> loadRequestsOfDepartment(String departmentId) {
        return holidayRequestRepository.findAllByRequester_Employee_Department_Id(departmentId);
    }

}
