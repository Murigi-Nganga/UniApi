package com.example.uniapi.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    @JsonBackReference("institution-course")
    private Institution institution;

    //TODO: Review cascade type
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    @JsonManagedReference("student-course")
    private List<Student> students;

    public Course(Long id, String name, String description, Institution institution, List<Student> students) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.institution = institution;
        this.students = students;
    }

    public Course() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
