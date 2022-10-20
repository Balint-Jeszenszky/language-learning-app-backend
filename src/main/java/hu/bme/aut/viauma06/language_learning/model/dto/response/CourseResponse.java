package hu.bme.aut.viauma06.language_learning.model.dto.response;

import java.util.Date;

public class CourseResponse {
    private Integer id;
    private String name;
    private Date deadline;
    private PublicUserDetailsResponse teacher;

    public CourseResponse() {
    }

    public CourseResponse(Integer id, String name, Date deadline, PublicUserDetailsResponse teacher) {
        this.id = id;
        this.name = name;
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
