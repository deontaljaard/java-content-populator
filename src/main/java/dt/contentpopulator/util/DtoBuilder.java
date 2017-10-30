package dt.contentpopulator.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dt.contentpopulator.dto.VocabPackageDto;

public class DtoBuilder {

	private final Logger LOGGER = Logger.getLogger(DtoBuilder.class.getSimpleName());

//	public List<VocabPackageDto> buildDtosFromPath(String directory) {
//		return getFilesToProcess(directory)
//		.parallelStream()
//		.map(path -> {
//			File fileToUpload = path.toFile();
//			final String fileName = fileToUpload.getName();
//			String[] fileParts = fileName.split("_");
//
//			if (fileParts.length != 3) {
//				LOGGER.log(Level.SEVERE, String.format(
//						"File '%s' is not in the expected format. Example 'and_conjunction_1.<extension>'. Skipping.", fileName));
//			}
//
//			String idPart = fileParts[2];
//			final Long demoId = Long.parseLong(idPart.substring(0, 1));
//			final String demoDescription = fileParts[0];
//			
//			return new VocabPackageDto(demoId, demoDescription);
//		})
//		.collect(Collectors.toList());
//	}
//
//	private List<Path> getFilesToProcess(String directory) {
//		try (Stream<Path> filePathStream = Files.walk(Paths.get(directory))) {
//			List<Path> filesToProcess = new ArrayList<>();
//
//			filePathStream.forEach(filePath -> {
//				if (Files.isRegularFile(filePath)) {
//					filesToProcess.add(filePath);
//				}
//			});
//
//			return filesToProcess;
//		} catch (IOException ex) {
//			ex.printStackTrace();
//			return Collections.emptyList();
//		}
//	}
}
