package hu.bme.aut.viauma06.language_learning.model.dto.request;

public class CourseRequest {
    private String name;

    public CourseRequest() {
    }

    public CourseRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
