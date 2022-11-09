package hu.bme.aut.viauma06.language_learning.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "WordPair")
@Table(name = "word_pair")
public class WordPair {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "translation", nullable = false)
    private String translation;

    @ManyToOne
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Course course;

    public WordPair() {
    }

    public WordPair(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
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
        WordPair wordPair = (WordPair) o;
        return Objects.equals(id, wordPair.id) && Objects.equals(word, wordPair.word) && Objects.equals(translation, wordPair.translation) && Objects.equals(course, wordPair.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, translation, course);
    }
}
