package hu.bme.aut.viauma06.language_learning.model.dto.response;

import java.util.Date;

public class SubmissionResponse {
    private Integer id;
    private Integer score;
    private Date submittedAt;

    public SubmissionResponse() {
    }

    public SubmissionResponse(Integer id, Integer score, Date submittedAt) {
        this.id = id;
        this.score = score;
        this.submittedAt = submittedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }
}
