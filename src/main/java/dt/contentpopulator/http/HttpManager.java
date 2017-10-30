package dt.contentpopulator.http;

import java.util.stream.Stream;

/**
 * Keep the interface agnostic from HTTP client objects to prevent specifics
 * from 'bleeding' into the main content populator client.
 */
public interface HttpManager {

	Stream<HttpResult> processDirectory();

}
