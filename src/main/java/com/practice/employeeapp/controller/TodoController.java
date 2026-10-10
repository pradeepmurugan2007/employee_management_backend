package com.practice.employeeapp.controller;

import com.practice.employeeapp.models.Employee;
import com.practice.employeeapp.models.User;
import com.practice.employeeapp.models.Todo;
import com.practice.employeeapp.repository.*;
import com.practice.employeeapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Todo")

public class TodoController {
    @Autowired
    private TodoService todoservice;

    @Autowired
    private JavaMailSender mailSender;
    @GetMapping("/get")
    String gettodo(){

        return "ToDo";
    }

    @GetMapping("/{id}")
    ResponseEntity<Todo> gettodobyid(@PathVariable int id) {

        try {
            Todo get = todoservice.gettodoid(id);
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
    ResponseEntity<List<Todo>> gettodos(){
        return new ResponseEntity<>(todoservice.gettodo(),HttpStatus.OK);
    }
    @PostMapping("/create")
    ResponseEntity createuser(@RequestBody Todo tod) {
        try {
            Todo create = todoservice.createtodo(tod);
            return new ResponseEntity(create, HttpStatus.CREATED);
        }catch (RuntimeException e){
            return new ResponseEntity<>((Object) null, HttpStatus.NOT_FOUND);
        }

    }


    @PutMapping
    ResponseEntity<Todo> update(@RequestBody Todo tod ){
        return new ResponseEntity<>(todoservice.update(tod),HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    void delete(@PathVariable int id){
        todoservice.delete(id);
    }
    @PostMapping("/login")
    public String login(@RequestBody User user){

        User found =
                todoservice.checklogin(
                        user.getUsername()
                );

        if(found != null &&
                found.getPassword().equals(user.getPassword())){

            return "Login Success";
        }

        return "Invalid Username or Password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        User found = todoservice.checklogin(username);
        if (found != null) {
            try {
                jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
                org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(message, true, "UTF-8");
                
                helper.setFrom("pradeepmurugan.d2007@gmail.com");
                helper.setTo(username);
                helper.setSubject("EmpLy - Your Password Recovery");
                
                String resetLink = "http://localhost:5173/reset-password?email=" + username;
                String htmlMsg = "<div style='font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto; border: 1px solid #e2e8f0; border-radius: 12px; padding: 30px; text-align: center; color: #1e293b;'>"
                        + "<h1 style='margin-bottom: 5px;'><span style='color: #093c44;'>Emp</span><span style='color: #0d9488;'>Ly</span></h1>"
                        + "<p style='color: #64748b; font-size: 14px; margin-top: 0;'>Employee Management</p>"
                        + "<hr style='border: none; border-top: 1px solid #e2e8f0; margin: 20px 0;' />"
                        + "<h2 style='color: #0f172a; font-size: 20px;'>Password Recovery</h2>"
                        + "<p style='font-size: 15px; line-height: 1.5;'>Hello, we received a request to recover the password for your account.</p>"
                        + "<p style='font-size: 15px; margin-bottom: 25px;'>Click the button below to reset your password:</p>"
                        + "<a href='" + resetLink + "' style='background-color: #0d9488; color: white; text-decoration: none; padding: 12px 25px; border-radius: 6px; font-weight: bold; font-size: 16px; display: inline-block;'>Reset Password</a>"
                        + "<p style='font-size: 13px; color: #64748b; margin-top: 30px;'>If you didn't request this, please safely ignore this email.</p>"
                        + "</div>";
                
                helper.setText(htmlMsg, true);
                mailSender.send(message);
                
                return "Recovery email sent";
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed to send email: " + e.getMessage();
            }
        }
        return "User not found";
    }
    @DeleteMapping
  void deleterec(@RequestBody Todo tod){
       todoservice.deleteby(tod);
    }
    @GetMapping("/Employee")
    public List<Employee> getEmployees(){

        return todoservice.getEmployees();
    }

    @PostMapping("/Employee")
    public Employee addEmployee(
            @RequestBody Employee emp){

        return todoservice.createEmployee(emp);
    }

    @DeleteMapping("/Employee/{id}")
    public void deleteEmployee(
            @PathVariable int id){

        todoservice.deleteEmployee(id);
    }

    @PutMapping("/Employee")
    public Employee updateEmployee(
            @RequestBody Employee emp){

        return todoservice.updateEmployee(emp);
    }
    @PostMapping("/signup")
    public User signup(
            @RequestBody User user){

        return todoservice.createUser(user);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String newPassword = body.get("password");
        User found = todoservice.checklogin(username);
        if (found != null) {
            found.setPassword(newPassword);
            todoservice.createUser(found);
            
            try {
                jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
                org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom("pradeepmurugan.d2007@gmail.com");
                helper.setTo(username);
                helper.setSubject("EmpLy - Password Reset Successful");
                
                String htmlMsg = "<div style='font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto; border: 1px solid #e2e8f0; border-radius: 12px; padding: 30px; text-align: center; color: #1e293b;'>"
                        + "<h1 style='margin-bottom: 5px;'><span style='color: #093c44;'>Emp</span><span style='color: #0d9488;'>Ly</span></h1>"
                        + "<p style='color: #64748b; font-size: 14px; margin-top: 0;'>Employee Management</p>"
                        + "<hr style='border: none; border-top: 1px solid #e2e8f0; margin: 20px 0;' />"
                        + "<h2 style='color: #16a34a; font-size: 20px;'>Password Reset Successful</h2>"
                        + "<p style='font-size: 15px; line-height: 1.5;'>Your EmpLy account password has been successfully updated.</p>"
                        + "<p style='font-size: 13px; color: #64748b; margin-top: 30px;'>If you did not make this change, please contact your administrator immediately.</p>"
                        + "</div>";
                
                helper.setText(htmlMsg, true);
                mailSender.send(message);
            } catch (Exception e) {}
            return "Password Reset Successful";
        }
        return "User not found";
    }

}
