package dt.contentpopulator.populator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;

import dt.contentpopulator.http.HttpClientFactory;
import dt.contentpopulator.http.HttpManager;
import dt.contentpopulator.http.HttpResult;

public class JavaContentPopulator {

	private static final Logger LOGGER = Logger.getLogger(JavaContentPopulator.class.getSimpleName());
	private static final int DIR_TO_PROCESS_IDX = 0;
	private static final int ENDPOINT_TO_CALL_IDX = 1;
	private static final int API_KEY_IDX = 2;
	private static final int JSON_TEMPLATE_FILE_IDX = 3;
	private static final int HTTP_CLIENT_MANAGER_IDX = 4;
	private static final String HTTP_RESULT_MSG = "Processing of path '%s' was '%s'";

	public static void main(String... args) throws JsonProcessingException, IOException {
		if (args.length != 5) {
			System.out.println("This client requires 4 arguments to function correctly. Namely;\n"
					+ "1. Directory to process\n" + "2. Endpoint to call\n" + "3. API key\n"
					+ "4. JSON template to use for DTO\n" + "5. HTTP Client (apache, java8, java9)");
			System.exit(1);
		}

		final String directory = args[DIR_TO_PROCESS_IDX];
		final String endpoint = args[ENDPOINT_TO_CALL_IDX];
		final String apiKey = args[API_KEY_IDX];
		final String jsonTemplatePath = args[JSON_TEMPLATE_FILE_IDX];
		final String httpClientManager = args[HTTP_CLIENT_MANAGER_IDX];

		process(directory, endpoint, apiKey, jsonTemplatePath, httpClientManager);
	}

	private static void process(final String directory, final String endpoint, final String apiKey,
			final String jsonTemplatePath, final String httpClientManager) {
		try {
			HttpManager httpManager = HttpClientFactory.getHttpClientManager(directory, endpoint,
					apiKey, jsonTemplatePath, httpClientManager);
			Stream<HttpResult> httpResults = httpManager.processDirectory();
			logHttpResult(httpResults);
			LOGGER.log(Level.INFO, "Done!");
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Unable to process your request. Please investigate...", ex);
		}
	}

	private static void logHttpResult(Stream<HttpResult> httpResults) {
		httpResults.forEach(httpResult -> {
			LOGGER.log(Level.INFO, String.format(HTTP_RESULT_MSG, httpResult.getFileProcessed(),
					httpResult.isSuccessful() ? "successful" : "unsuccessful"));
		});
	}
}
