package hu.bme.aut.viauma06.language_learning.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "Course")
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "deadline")
    private Date deadline;

    @ManyToOne
    private User teacher;

    @ManyToMany
    @JoinTable(name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> students = new ArrayList();

    @OneToMany(mappedBy = "course")
    private List<CourseStudentAccess> courseAccess = new ArrayList();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private List<WordPair> words = new ArrayList();

    public Course() {
    }

    public Course(String name, Date deadline, User teacher) {
        this.name = name;
        this.deadline = deadline;
        this.teacher = teacher;
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public List<CourseStudentAccess> getCourseAccess() {
        return courseAccess;
    }

    public void setCourseAccess(List<CourseStudentAccess> courseAccess) {
        this.courseAccess = courseAccess;
    }

    public List<WordPair> getWords() {
        return words;
    }

    public void setWords(List<WordPair> words) {
        this.words = words;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && Objects.equals(deadline, course.deadline) && Objects.equals(teacher, course.teacher) && Objects.equals(students, course.students) && Objects.equals(courseAccess, course.courseAccess) && Objects.equals(words, course.words);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, deadline, teacher, students, courseAccess, words);
    }
}
