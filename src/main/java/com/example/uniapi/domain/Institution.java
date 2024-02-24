package com.example.uniapi.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Institution {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String location;

    //TODO: Review cascade type
    @OneToMany(mappedBy = "institution", cascade = CascadeType.REMOVE)
    @JsonManagedReference("institution-course")
    private List<Course> courses;

    public Institution(Long id, String name, String location, List<Course> courses) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.courses = courses;
    }

    public Institution() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
