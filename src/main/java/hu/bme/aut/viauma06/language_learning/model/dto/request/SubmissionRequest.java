package hu.bme.aut.viauma06.language_learning.model.dto.request;

public class SubmissionRequest {
    private Integer courseId;
    private Integer score;

    public SubmissionRequest() {
    }

    public SubmissionRequest(Integer courseId, Integer score) {
        this.courseId = courseId;
        this.score = score;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public Integer getScore() {
        return score;
    }
}
