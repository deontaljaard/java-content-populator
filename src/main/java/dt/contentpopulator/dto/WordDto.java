package dt.contentpopulator.dto;

import java.io.Serializable;

/**
 * This POJO represents the schema the file is expected to be in. For example:
 * 'and_conjunction_1.<extension>'. 'and' represents the word, 'conjunction'
 * represents the class of the word, and '1' represents the id for on the DB for
 * the word class.
 */
public final class WordDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String description;
	private String wordClass;
	private Long wordClassId;

	public WordDto() {
	}

	public WordDto(String description, String wordClass, Long wordClassId) {
		this.description = description;
		this.wordClass = wordClass;
		this.wordClassId = wordClassId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWordClass() {
		return wordClass;
	}

	public void setWordClass(String wordClass) {
		this.wordClass = wordClass;
	}

	public Long getWordClassId() {
		return wordClassId;
	}

	public void setWordClassId(Long wordClassId) {
		this.wordClassId = wordClassId;
	}

}