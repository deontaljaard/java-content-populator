package dt.contentpopulator.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dt.contentpopulator.dto.VocabPackageDto;

public final class JsonObjectMapper {

	private JsonObjectMapper() {
	}

	public static VocabPackageDto readDtoFromString(String jsonString) throws JsonProcessingException, IOException {
		return new ObjectMapper().readerFor(new TypeReference<VocabPackageDto>() {
		}).readValue(jsonString);
	}

	public static String writeDtoToJsonString(Object value) throws JsonProcessingException {
		return new ObjectMapper().writer().withoutRootName().writeValueAsString(value);
	}
}
