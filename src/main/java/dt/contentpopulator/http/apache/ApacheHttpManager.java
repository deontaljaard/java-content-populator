package dt.contentpopulator.http.apache;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import dt.contentpopulator.dto.VocabPackageDto;
import dt.contentpopulator.dto.WordDto;
import dt.contentpopulator.http.HttpFileUtil;
import dt.contentpopulator.http.HttpManager;
import dt.contentpopulator.http.HttpResult;
import dt.contentpopulator.json.JsonObjectMapper;

public final class ApacheHttpManager implements HttpManager {

	private static final Logger LOGGER = Logger.getLogger(ApacheHttpManager.class.getSimpleName());
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String ERR_PROCESS_DIR = "Error processing directory '%s'";
	private static final String ERR_CREATE_HTTP_POST = "Couldn't create HTTP Post request for path '%s'.";
	private static final String SEPERATOR = "----------------------------------------";
	private static final String INFO_EXECUTE_REQ = "Executing request: %s";
	private static final String INFO_STATUS_LINE = "Status line: %s";
	private static final String INFO_RESP_CONTENT_LENGTH = "Response content length: %s";

	private String directory;
	private String endpoint;
	private String apiKey;
	private VocabPackageDto jsonTemplate;

	public ApacheHttpManager(final String directory, final String endpoint, final String apiKey,
			final String jsonTemplatePath) throws JsonProcessingException, IOException {
		this.directory = directory;
		this.endpoint = endpoint;
		this.apiKey = apiKey;

		final String jsonTemplateAsString = HttpFileUtil.readPathAsString(jsonTemplatePath);
		this.jsonTemplate = JsonObjectMapper.readDtoFromString(jsonTemplateAsString);
	}

	@Override
	public Stream<HttpResult> processDirectory() {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

			Stream<HttpResult> httpResults = constructPostRequestsFromFilesInDirectory().parallel().map(pathAndPost -> {
				HttpResult httpResult = executeHttpPost(httpclient, pathAndPost);
				return httpResult;
			});

			return httpResults;
		} catch (IOException ex) {
			ex.printStackTrace();
			LOGGER.log(Level.SEVERE, String.format(ERR_PROCESS_DIR, directory), ex);
			return Stream.empty();
		}
	}

	private HttpResult executeHttpPost(CloseableHttpClient httpclient, PathAndHttpPost pathAndPost) {
		final HttpPost httpPost = pathAndPost.getPost();
		final String filePath = pathAndPost.getPath().toString();
		try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
			LOGGER.log(Level.INFO, "\n" + SEPERATOR);
			LOGGER.log(Level.INFO, String.format(INFO_EXECUTE_REQ, httpPost.getRequestLine()));
			LOGGER.log(Level.INFO, String.format(INFO_STATUS_LINE, response.getStatusLine()));
			HttpEntity resEntity = response.getEntity();
			boolean successful = false;
			if (resEntity != null) {
				successful = true;
				LOGGER.log(Level.INFO, String.format(INFO_RESP_CONTENT_LENGTH, resEntity.getContentLength()));
			}
			EntityUtils.consume(resEntity);
			LOGGER.log(Level.INFO, SEPERATOR);

			return new HttpResult(filePath, successful);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new HttpResult(filePath, false);
		}

	}

	private Stream<PathAndHttpPost> constructPostRequestsFromFilesInDirectory() {
		List<PathAndHttpPost> pathAndPosts = new ArrayList<>();

		HttpFileUtil.getFilesToProcessFromDirectory(directory).parallelStream().forEach(path -> {
			try {
				pathAndPosts.add(createHttpPost(path));
			} catch (JsonProcessingException ex) {
				LOGGER.log(Level.WARNING, String.format(ERR_CREATE_HTTP_POST, path, ex.getMessage()), ex);
			}
		});

		return pathAndPosts.stream();
	}

	private PathAndHttpPost createHttpPost(Path path) throws JsonProcessingException {
		HttpPost post = createHttpPostWithHeader();

		VocabPackageDto vocabPackageDto = createVocabPackageDto(path);

		addFileAndDtoToUploadToHttpPost(path, vocabPackageDto, post);

		return new PathAndHttpPost(path, post);
	}

	private HttpPost createHttpPostWithHeader() {
		HttpPost post = new HttpPost(endpoint);

		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Authorization", "Bearer " + apiKey);

		return post;
	}

	private VocabPackageDto createVocabPackageDto(Path path) {
		Optional<WordDto> maybeWordDto = HttpFileUtil.getWordDtoFromPath(path);

		VocabPackageDto vocabPackageDto = new VocabPackageDto();
		vocabPackageDto.setId(jsonTemplate.getId());

		if (maybeWordDto.isPresent()) {
			vocabPackageDto.setWord(maybeWordDto.get());
		}

		return vocabPackageDto;
	}

	private HttpPost addFileAndDtoToUploadToHttpPost(Path path, VocabPackageDto vocabPackageDto, HttpPost post)
			throws JsonProcessingException {
		StringBody json = new StringBody(JsonObjectMapper.writeDtoToJsonString(vocabPackageDto),
				ContentType.APPLICATION_JSON);

		FileBody fileToUpload = new FileBody(path.toFile());
		HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("File", fileToUpload).addPart("dto", json)
				.build();

		post.setEntity(reqEntity);

		return post;
	}

	private final class PathAndHttpPost {

		private final Path path;
		private final HttpPost post;

		public PathAndHttpPost(final Path path, final HttpPost post) {
			this.path = path;
			this.post = post;
		}

		public Path getPath() {
			return path;
		}

		public HttpPost getPost() {
			return post;
		}

	}

}
