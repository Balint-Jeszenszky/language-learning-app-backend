package hu.bme.aut.viauma06.language_learning.model.dto.request;

import java.util.Date;

public class CourseRequest {
    private String name;
    private Date deadline;

    public CourseRequest() {
    }

    public CourseRequest(String name, Date deadline) {
        this.name = name;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public Date getDeadline() {
        return deadline;
    }
}
