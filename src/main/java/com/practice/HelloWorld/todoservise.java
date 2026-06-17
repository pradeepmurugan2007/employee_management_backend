package com.practice.HelloWorld;

import com.practice.HelloWorld.models.employee;
import com.practice.HelloWorld.models.todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.practice.HelloWorld.models.users;
import java.util.List;

@Service
public class todoservise {
    @Autowired
    private todorepos todorep;
    @Autowired
    private userrepos userrep;
    @Autowired
    private employeerepos employeerep;


    public todo createtodo(todo data ){
      return todorep.save(data);


    }
    public todo gettodoid(int data ){
        return todorep.getReferenceById(data);


    }
    public List<todo> gettodo(){
        return todorep.findAll();


    }
    public todo update(todo tod){
        return todorep.save(tod);


    }
    public void delete(int id){
        todorep.delete(gettodoid(id));


    }
    public void deleteby(todo tod){
       todorep.delete(tod);


    }
    public users checklogin(String name){

        return userrep.findByUsername(name);
    }
    public List<employee> getEmployees(){

        return employeerep.findAll();
    }

    public employee createEmployee(employee emp){

        return employeerep.save(emp);
    }

    public void deleteEmployee(int id){

        employeerep.deleteById(id);
    }
    public employee updateEmployee(employee emp){

        return employeerep.save(emp);
    }


    public users createUser(users user){

        return userrep.save(user);
    }

}
