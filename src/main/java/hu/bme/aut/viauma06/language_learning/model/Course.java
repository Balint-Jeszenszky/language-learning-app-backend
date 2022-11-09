package hu.bme.aut.viauma06.language_learning.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "deadline")
    private Date deadline;

    @ManyToOne
    private User teacher;

    @ManyToMany
    @JoinTable(name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> students = new ArrayList();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private List<WordPair> words = new ArrayList();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "submission_id")
    private List<Submission> submissions = new ArrayList();

    @ElementCollection
    @CollectionTable(name = "course_metadata", joinColumns = @JoinColumn(name = "course_id"))
    @JoinColumn(name = "course_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "metadata")
    private List<String> metadata = new ArrayList();

    public Course() {
    }

    public Course(String name, String description, Date deadline, User teacher, List<String> metadata) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.teacher = teacher;
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

    public List<WordPair> getWords() {
        return words;
    }

    public void setWords(List<WordPair> words) {
        this.words = words;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && Objects.equals(description, course.description) && Objects.equals(deadline, course.deadline) && Objects.equals(teacher, course.teacher) && Objects.equals(students, course.students) && Objects.equals(words, course.words) && Objects.equals(submissions, course.submissions) && Objects.equals(metadata, course.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, deadline, teacher, students, words, submissions, metadata);
    }
}
