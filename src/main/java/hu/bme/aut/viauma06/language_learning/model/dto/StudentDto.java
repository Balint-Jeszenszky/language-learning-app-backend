package hu.bme.aut.viauma06.language_learning.model.dto;

public class StudentDto {
    private Integer id;
    private String name;
    private String email;
    private Integer score;

    public StudentDto() {
    }

    public StudentDto(Integer id, String name, String email, Integer score) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.score = score;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
