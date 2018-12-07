package app.entities;


import app.annotations.Column;
import app.annotations.Entity;
import app.annotations.Id;

import java.time.LocalDate;
import java.util.Date;

@Entity(name = "users")
public class User {

    @Id(name = "id")
    private int id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "age")
    private int age;
    @Column(name = "registration_date")
    private LocalDate registrationData;

    public User() {
    }

    public User(String userName, String password, int age, LocalDate registrationData) {
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.registrationData = registrationData;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getRegistrationData() {
        return this.registrationData;
    }

    public void setRegistrationData(LocalDate registrationData) {
        this.registrationData = registrationData;
    }

    @Override
    public String toString() {
        return String.format("%d %s %s %d %s", id, userName, password, age, registrationData);
    }
}
