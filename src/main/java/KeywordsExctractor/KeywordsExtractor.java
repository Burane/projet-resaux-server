package KeywordsExctractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class KeywordsExtractor {
	private final Stopwords stopwords;

	public KeywordsExtractor(Langage langage) {
		this.stopwords = new Stopwords(langage);
	}

	public ArrayList<String> getKeywords(String input) {
		return Arrays.stream(input.split(" |'"))
				.filter(word -> !stopwords.getStopsWords().contains(word.toLowerCase(Locale.ROOT)))
				.collect(Collectors.toCollection(ArrayList::new));
	}

}
