package hu.bme.aut.viauma06.language_learning.model.dto.response;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class StudentCourseResponse {
    private Integer id;
    private String name;
    private String description;
    private Instant deadline;

    private List<String> metadata;

    public StudentCourseResponse() {
    }

    public StudentCourseResponse(Integer id, String name, String description, Date deadline, List<String> metadata) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline.toInstant();
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
        return Date.from(deadline);
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline.toInstant();
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }

    public List<String> getMetadata() {
        return metadata;
    }
}
