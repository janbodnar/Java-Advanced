package com.zetcode;

import com.zetcode.model.Employee;
import com.zetcode.model.Task;
import com.zetcode.repository.EmployeeRepository;
import com.zetcode.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Component
public class MyRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyRunner.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        logger.info("Saving data");

        var t1 = new Task("Task 1");
        var t2 = new Task("Task 2");
        var t3 = new Task("Task 3");
        var t4 = new Task("Task 4");
        var t5 = new Task("Task 5");

        var e1 = new Employee("John", "Doe");
        var e2 = new Employee("Bobby", "Brown");
        var e3 = new Employee("Lucy", "Smith");

        e1.setTasks(Set.of(t1, t2, t3));
        e2.setTasks(Set.of(t4));
        e3.setTasks(Set.of(t5));

        taskRepository.saveAll(List.of(t1, t2, t3, t4, t5));
        employeeRepository.saveAll(List.of(e1, e2, e3));

        employeeRepository.findById(1L).ifPresent(employee ->
                logger.info("{} {} has tasks: {}", employee.getFirstName(),
                employee.getLastName(), employee.getTasks()));

        employeeRepository.findById(2L).ifPresent(employee ->
                logger.info("{} {} has tasks: {}", employee.getFirstName(),
                        employee.getLastName(), employee.getTasks()));

    }
}
