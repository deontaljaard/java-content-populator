package dt.contentpopulator.http;

public final class HttpResult {

	private final String fileProcessed;
	private final boolean successful;

	public HttpResult(String fileProcessed, boolean successful) {
		this.fileProcessed = fileProcessed;
		this.successful = successful;
	}

	public String getFileProcessed() {
		return fileProcessed;
	}

	public boolean isSuccessful() {
		return successful;
	}
}
