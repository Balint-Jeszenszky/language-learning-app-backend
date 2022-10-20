package hu.bme.aut.viauma06.language_learning.mapper;

import hu.bme.aut.viauma06.language_learning.model.WordPair;
import hu.bme.aut.viauma06.language_learning.model.dto.response.WordPairResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WordPairMapper {
    WordPairMapper INSTANCE = Mappers.getMapper(WordPairMapper.class);

    WordPairResponse wordPairToWordPairResponse(WordPair í);
}
