package hu.bme.aut.viauma06.language_learning.model.dto.request;

import java.util.List;

public class WordPairRequest {
    private Integer id;
    private String word;
    private String translation;
    private List<String> metadata;

    public WordPairRequest() {
    }

    public WordPairRequest(Integer id, String word, String translation, List<String> metadata) {
        this.id = id;
        this.word = word;
        this.translation = translation;
        this.metadata = metadata;
    }

    public Integer getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    public List<String> getMetadata() {
        return metadata;
    }
}
