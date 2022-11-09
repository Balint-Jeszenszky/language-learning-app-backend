package hu.bme.aut.viauma06.language_learning.model.dto.request;

import java.util.List;

public class WordPairRequest {
    private Integer id;
    private String word;
    private String translation;


    public WordPairRequest() {
    }

    public WordPairRequest(Integer id, String word, String translation) {
        this.id = id;
        this.word = word;
        this.translation = translation;

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
}
