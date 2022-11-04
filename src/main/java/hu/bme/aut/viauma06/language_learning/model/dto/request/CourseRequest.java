package hu.bme.aut.viauma06.language_learning.model.dto.request;

import java.util.Date;

public class CourseRequest {
    private String name;
    private String description;
    private Date deadline;

    public CourseRequest() {
    }

    public CourseRequest(String name, String description, Date deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDeadline() {
        return deadline;
    }
}
