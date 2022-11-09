package hu.bme.aut.viauma06.language_learning.model.dto.response;

import java.util.List;

public class WordPairResponse {
    private Integer id;
    private String word;
    private String translation;

    public WordPairResponse() {
    }

    public WordPairResponse(Integer id, String word, String translation) {
        this.id = id;
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
}
