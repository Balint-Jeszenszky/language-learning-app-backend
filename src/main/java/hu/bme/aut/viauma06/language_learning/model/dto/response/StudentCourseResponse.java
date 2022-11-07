package hu.bme.aut.viauma06.language_learning.model.dto.response;

import java.time.Instant;
import java.util.Date;

public class StudentCourseResponse {
    private Integer id;
    private String name;
    private String description;
    private Instant deadline;

    public StudentCourseResponse() {
    }

    public StudentCourseResponse(Integer id, String name, String description, Date deadline) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline.toInstant();
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
        return Date.from(deadline);
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline.toInstant();
    }
}
