package hu.bme.aut.viauma06.language_learning.model;

import javax.persistence.*;

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
}
