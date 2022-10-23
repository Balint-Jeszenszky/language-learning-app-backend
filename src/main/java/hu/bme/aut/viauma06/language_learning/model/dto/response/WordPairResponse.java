package hu.bme.aut.viauma06.language_learning.model.dto.response;

import java.util.List;

public class WordPairResponse {
    private Integer id;
    private String word;
    private String translation;
    private List<String> metadata;

    public WordPairResponse() {
    }

    public WordPairResponse(Integer id, String word, String translation, List<String> metadata) {
        this.id = id;
        this.word = word;
        this.translation = translation;
        this.metadata = metadata;
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

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }
}
