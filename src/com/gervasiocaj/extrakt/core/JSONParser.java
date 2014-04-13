package com.gervasiocaj.extrakt.core;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.content.Context;

import com.gervasiocaj.extrakt.core.element.Movie;

public class JSONParser {
	
	public static List<Movie> getMovieRecomendations(Context context) {
		List<Movie> result = new LinkedList<Movie>();
		JSONArray list = JSONTalker.fillMovieRecomendations(context), tempGenres;
		JSONObject temp, tempImage;
		Movie movie;
		
		for (int i = 0; i < list.size(); i++) {
			temp = (JSONObject) list.get(i);
			
			movie = new Movie();
			movie.title = (String) temp.get("title");
			movie.year = (long) temp.get("year");
			movie.released = (long) temp.get("released");
			movie.url = (String) temp.get("url");
			movie.runtime = (long) temp.get("runtime");
			movie.overview = (String) temp.get("overview");
			
			tempImage = (JSONObject) temp.get("images");
			movie.poster = (String) tempImage.get("poster");
			movie.fanart = (String) tempImage.get("fanart");
			
			int x = 0;
			tempGenres =  (JSONArray) temp.get("genres");
			movie.genres = new String[tempGenres.size()];
			for (Object string : tempGenres)
				movie.genres[x++] = (String) string;
			
			//movie.downloadImg(context.getResources()); // XXX download in paralell
			result.add(movie);
		}
		
		return result;
	}

}
