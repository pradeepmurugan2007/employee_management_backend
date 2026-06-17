package com.practice.HelloWorld;
import com.practice.HelloWorld.models.employee;
import com.practice.HelloWorld.models.users;
import com.practice.HelloWorld.models.todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://frontend-react-crud-ten.vercel.app"
})
public class todocontroller {
    @Autowired
    private todoservise todoservice;
    @GetMapping("/get")
    String gettodo(){

        return "ToDo";
    }

    @GetMapping("/{id}")
    ResponseEntity<todo> gettodobyid(@PathVariable int id) {

        try {
            todo get = todoservice.gettodoid(id);
            return new ResponseEntity(get, HttpStatus.OK);
        }catch (RuntimeException e){
            return null;
        }
    }
    @GetMapping("/")
    String gettodobyparam(@RequestParam(name="iniyan") int id) {

            return "your are request id:"+id;


    }

    @GetMapping
    ResponseEntity<List<todo>> gettodos(){
        return new ResponseEntity<>(todoservice.gettodo(),HttpStatus.OK);
    }
    @PostMapping("/create")
    ResponseEntity createuser(@RequestBody todo tod) {
        try {
            todo create = todoservice.createtodo(tod);
            return new ResponseEntity(create, HttpStatus.CREATED);
        }catch (RuntimeException e){
            return new ResponseEntity<>((Object) null, HttpStatus.NOT_FOUND);
        }

    }


    @PutMapping
    ResponseEntity<todo> update(@RequestBody todo tod ){
        return new ResponseEntity<>(todoservice.update(tod),HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    void delete(@PathVariable int id){
        todoservice.delete(id);
    }
    @PostMapping("/login")
    public String login(@RequestBody users user){

        users found =
                todoservice.checklogin(
                        user.getUsername()
                );

        if(found != null &&
                found.getPassword().equals(user.getPassword())){

            return "Login Success";
        }

        return "Invalid Username or Password";
    }
    @DeleteMapping
  void deleterec(@RequestBody todo tod){
       todoservice.deleteby(tod);
    }
    @GetMapping("/employee")
    public List<employee> getEmployees(){

        return todoservice.getEmployees();
    }

    @PostMapping("/employee")
    public employee addEmployee(
            @RequestBody employee emp){

        return todoservice.createEmployee(emp);
    }

    @DeleteMapping("/employee/{id}")
    public void deleteEmployee(
            @PathVariable int id){

        todoservice.deleteEmployee(id);
    }

    @PutMapping("/employee")
    public employee updateEmployee(
            @RequestBody employee emp){

        return todoservice.updateEmployee(emp);
    }
    @PostMapping("/signup")
    public users signup(
            @RequestBody users user){

        return todoservice.createUser(user);
    }

}
