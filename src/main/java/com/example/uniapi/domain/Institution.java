package com.example.uniapi.domain;

import com.example.uniapi.domain.enums.InstitutionType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Institution {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InstitutionType type;

    @Column(nullable = false)
    private String location;

    //TODO: Review cascade type
    @OneToMany(mappedBy = "institution", cascade = CascadeType.REMOVE)
    @JsonManagedReference("institution-course")
    private List<Course> courses;

    public Institution(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Institution() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InstitutionType getType() {
        return type;
    }

    public void setType(InstitutionType type) {
        this.type = type;
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
