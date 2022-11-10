package hu.bme.aut.viauma06.language_learning.model.dto.response;

public class StudentWordPairResponse {
    private Integer id;
    private String word;
    private String translation;
    private Boolean saved;

    public StudentWordPairResponse() {
    }

    public StudentWordPairResponse(Integer id, String word, String translation, Boolean saved) {
        this.id = id;
        this.word = word;
        this.translation = translation;
        this.saved = saved;
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

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }
}
