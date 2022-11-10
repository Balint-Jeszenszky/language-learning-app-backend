package hu.bme.aut.viauma06.language_learning.model.dto.request;

import java.util.Date;
import java.util.List;

public class CourseRequest {
    private String name;
    private String description;
    private Date deadline;

    private List<String> metadata;

    public CourseRequest() {
    }

    public CourseRequest(String name, String description, Date deadline, List<String> metadata) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.metadata = metadata;
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

    public List<String> getMetadata() {
        return metadata;
    }
}
