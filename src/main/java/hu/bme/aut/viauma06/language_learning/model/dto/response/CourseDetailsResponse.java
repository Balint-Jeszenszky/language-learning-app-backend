package hu.bme.aut.viauma06.language_learning.model.dto.response;

import hu.bme.aut.viauma06.language_learning.model.dto.StudentDto;

import java.util.Date;
import java.util.List;

public class CourseDetailsResponse {
    private Integer id;
    private String name;
    private String description;
    private Date deadline;
    private List<StudentDto> students;
    private List<String> metadata;

    public CourseDetailsResponse() {
    }

    public CourseDetailsResponse(Integer id, String name, String description, Date deadline, List<StudentDto> students, List<String> metadata) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.students = students;
        this.metadata = metadata;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public List<StudentDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDto> students) {
        this.students = students;
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }
}
