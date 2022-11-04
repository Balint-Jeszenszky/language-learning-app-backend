package hu.bme.aut.viauma06.language_learning.model.dto.response;

import java.util.Date;

public class CourseResponse {
    private Integer id;
    private String name;
    private String description;
    private Date deadline;
    private PublicUserDetailsResponse teacher;

    public CourseResponse() {
    }

    public CourseResponse(Integer id, String name, String description, Date deadline, PublicUserDetailsResponse teacher) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.teacher = teacher;
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

    public PublicUserDetailsResponse getTeacher() {
        return teacher;
    }

    public void setTeacher(PublicUserDetailsResponse teacher) {
        this.teacher = teacher;
    }
}
