package dt.contentpopulator.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import dt.contentpopulator.dto.WordDto;
import dt.contentpopulator.http.apache.ApacheHttpManager;

public final class HttpFileUtil {

	private static final Logger LOGGER = Logger.getLogger(ApacheHttpManager.class.getSimpleName());
	private static final String INFO_NUM_FILES = "Found %d files to process.";
	private static final String ERR_PROCESS_DIR = "Couldn't get files from directory '%s'.";
	private static final String ERR_FILE_FORMAT = "File '%s' is not in the expected format. Example 'and_conjunction_1.<extension>'.";

	private HttpFileUtil() {
	}

	public static String readPathAsString(String path) throws IOException {
		return readPathAsString(path, StandardCharsets.UTF_8);
	}

	public static String readPathAsString(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static List<Path> getFilesToProcessFromDirectory(String directory) {
		try (Stream<Path> filePathStream = Files.walk(Paths.get(directory))) {
			List<Path> filesToProcess = new ArrayList<>();

			filePathStream.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					filesToProcess.add(filePath);
				}
			});

			LOGGER.log(Level.INFO, String.format(INFO_NUM_FILES, filesToProcess.size()));

			return filesToProcess;
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, String.format(ERR_PROCESS_DIR, directory), ex);
			return Collections.emptyList();
		}
	}

	public static Optional<WordDto> getWordDtoFromPath(Path path) {
		final String fileName = path.toFile().getName();
		String[] fileParts = fileName.split("_");

		if (fileParts.length != 3) {
			LOGGER.log(Level.SEVERE, String.format(ERR_FILE_FORMAT, fileName));
			return Optional.empty();
		}

		final String word = fileParts[0];
		final String formOfSpeech = fileParts[1];
		final Long formOfSpeechId = getIdFromFilePart(fileParts[2]);

		return Optional.of(new WordDto(word, formOfSpeech, formOfSpeechId));
	}

	private static Long getIdFromFilePart(String filePart) {
		return Long.parseLong(filePart.substring(0, 1));
	}

}
