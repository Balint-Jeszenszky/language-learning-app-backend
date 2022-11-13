package hu.bme.aut.viauma06.language_learning.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity(name = "Submission")
@Table(name = "submission")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Integer id;

    @ManyToOne
    private User student;

    @Column(name = "score")
    private Integer score;

    @Column(name = "submitted_at")
    private Date submittedAt = new Date();

    @ManyToOne
    private Course course;

    public Submission() {
    }

    public Submission(User student, Integer score, Course course) {
        this.student = student;
        this.score = score;
        this.course = course;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Submission that = (Submission) o;
        return Objects.equals(id, that.id) && Objects.equals(student, that.student) && Objects.equals(score, that.score) && Objects.equals(submittedAt, that.submittedAt) && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, student, score, submittedAt, course);
    }
}
