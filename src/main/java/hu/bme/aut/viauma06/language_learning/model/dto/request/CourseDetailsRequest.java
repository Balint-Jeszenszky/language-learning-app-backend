package hu.bme.aut.viauma06.language_learning.model.dto.request;

import java.util.Date;
import java.util.List;

public class CourseDetailsRequest {
    private Integer id;
    private String name;
    private Date deadline;
    private List<String> studentEmails;

    public CourseDetailsRequest() {
    }

    public CourseDetailsRequest(Integer id, String name, Date deadline, List<String> studentEmails) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.studentEmails = studentEmails;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public List<String> getStudentEmails() {
        return studentEmails;
    }
}
