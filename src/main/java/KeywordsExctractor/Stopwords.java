package KeywordsExctractor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class Stopwords {
	private ArrayList<String> stopsWords;

	public Stopwords(Langage langage) {
		String jsonStopwords = "";
		try {
			jsonStopwords = new String(getClass().getClassLoader().getResourceAsStream("stopwords/stopwords-"+langage+".json").readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Type listType = new TypeToken<ArrayList<String>>() {}.getType();
		this.stopsWords = new Gson().fromJson(jsonStopwords, listType);
	}

	public ArrayList<String> getStopsWords() {
		return stopsWords;
	}

	public void setStopsWords(ArrayList<String> stopsWords) {
		this.stopsWords = stopsWords;
	}

	@Override
	public String toString() {
		return "Stopwords{" + "stopsWords=" + Arrays.deepToString(stopsWords.toArray()) + '}';
	}
}
