package hu.bme.aut.viauma06.language_learning.controller;

import hu.bme.aut.viauma06.language_learning.model.dto.request.WordPairRequest;
import hu.bme.aut.viauma06.language_learning.model.dto.response.WordPairResponse;
import hu.bme.aut.viauma06.language_learning.service.WordPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wordPair")
public class WordPairController {

    @Autowired
    private WordPairService wordPairService;

    @GetMapping("/words/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<List<WordPairResponse>> getWordPairsByCourseId(@PathVariable("id") Integer id) {
        List<WordPairResponse> wordPairs = wordPairService.getWordPairsByCourseId(id);

        return new ResponseEntity(wordPairs, HttpStatus.OK);
    }

    @PutMapping("/words/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<WordPairResponse>> updateWordPairsByCourseId(@PathVariable("id") Integer id, @RequestBody List<WordPairRequest> wordPairsRequest) {
        List<WordPairResponse> wordPairs = wordPairService.updateWordPairs(id, wordPairsRequest);

        return new ResponseEntity(wordPairs, HttpStatus.OK);
    }

    @PostMapping("/save/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity saveWordPair(@PathVariable("id") Integer id) {
        wordPairService.saveWordPair(id);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity deleteWordPair(@PathVariable("id") Integer id) {
        wordPairService.deleteWordPair(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
