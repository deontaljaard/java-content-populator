package dt.contentpopulator.dto;

import java.io.Serializable;

/**
 * This DTO can be changed to have a collection of words - it depends on how
 * your service consumes this payload.
 */
public final class VocabPackageDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private WordDto word;

	public VocabPackageDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WordDto getWord() {
		return word;
	}

	public void setWord(WordDto word) {
		this.word = word;
	}

}