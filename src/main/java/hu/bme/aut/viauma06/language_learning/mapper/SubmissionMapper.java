package hu.bme.aut.viauma06.language_learning.mapper;

import hu.bme.aut.viauma06.language_learning.model.Submission;
import hu.bme.aut.viauma06.language_learning.model.dto.response.SubmissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubmissionMapper {
    SubmissionMapper INSTANCE = Mappers.getMapper(SubmissionMapper.class);

    SubmissionResponse submissionToSubmissionResponse(Submission submission);
}
