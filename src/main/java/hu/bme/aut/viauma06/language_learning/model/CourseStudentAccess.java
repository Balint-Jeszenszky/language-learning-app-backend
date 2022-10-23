package hu.bme.aut.viauma06.language_learning.model;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "CourseStudents")
@Table(name = "course_students")
public class CourseStudentAccess {
    @EmbeddedId
    CourseStudentAccessKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    Course course;

    @Column(name = "access_key")
    String accessKey;

    public CourseStudentAccess() {
    }

    public CourseStudentAccess(User user, Course course, String accessKey) {
        this.user = user;
        this.course = course;
        this.accessKey = accessKey;
    }

    public CourseStudentAccessKey getId() {
        return id;
    }

    public void setId(CourseStudentAccessKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseStudentAccess that = (CourseStudentAccess) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(course, that.course) && Objects.equals(accessKey, that.accessKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, accessKey);
    }
}
