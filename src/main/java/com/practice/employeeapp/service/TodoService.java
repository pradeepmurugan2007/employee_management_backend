package com.practice.employeeapp.service;


import com.practice.employeeapp.models.Employee;
import com.practice.employeeapp.models.Todo;
import com.practice.employeeapp.repository.*;
import com.practice.employeeapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.practice.employeeapp.models.User;
import java.util.List;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todorep;
    @Autowired
    private UserRepository UserRep;
    @Autowired
    private EmployeeRepository employeerep;


    public Todo createtodo(Todo data ){
      return todorep.save(data);


    }
    public Todo gettodoid(int data ){
        return todorep.getReferenceById(data);


    }
    public List<Todo> gettodo(){
        return todorep.findAll();


    }
    public Todo update(Todo tod){
        return todorep.save(tod);


    }
    public void delete(int id){
        todorep.delete(gettodoid(id));


    }
    public void deleteby(Todo tod){
       todorep.delete(tod);


    }
    public User checklogin(String name){

        return UserRep.findByUsername(name);
    }
    public List<Employee> getEmployees(){

        return employeerep.findAll();
    }

    public Employee createEmployee(Employee emp){

        return employeerep.save(emp);
    }

    public void deleteEmployee(int id){

        employeerep.deleteById(id);
    }
    public Employee updateEmployee(Employee emp){

        return employeerep.save(emp);
    }


    public User createUser(User user){

        return UserRep.save(user);
    }

}
