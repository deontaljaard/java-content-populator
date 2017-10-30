package dt.contentpopulator.http;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

import dt.contentpopulator.http.apache.ApacheHttpManager;

public final class HttpClientFactory {

	private static final String APACHE_HTTP_MANAGER = "apache";

	private HttpClientFactory() {
	}

	public static HttpManager getHttpClientManager(final String directory, final String endpoint, final String apiKey,
			final String jsonTemplatePath, final String httpClientManager) throws JsonProcessingException, IOException {
		switch (httpClientManager) {
			case APACHE_HTTP_MANAGER:
			default:
				return new ApacheHttpManager(directory, endpoint, apiKey, jsonTemplatePath);
		}
	}
}
