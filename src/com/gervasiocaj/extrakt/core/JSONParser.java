package com.gervasiocaj.extrakt.core;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.content.Context;

import com.gervasiocaj.extrakt.core.element.Content;
import com.gervasiocaj.extrakt.core.element.Movie;
import com.gervasiocaj.extrakt.core.element.TVShow;

public class JSONParser {
	
	public static List<Content> getContentRecomendations(Context context, boolean isMovie) {
		List<Content> result = new LinkedList<Content>();
		JSONArray list = JSONTalker.fillContentRecomendations(context, isMovie), tempGenres;
		JSONObject temp, tempImages;
		Content content;
		
		for (int i = 0; i < list.size(); i++) {
			temp = (JSONObject) list.get(i);
			
			if (isMovie) {
				content = new Movie();
				((Movie) content).released = (long) temp.get("released");
			} else {
				content = new TVShow();
				((TVShow) content).first_aired = (long) temp.get("first_aired");
			}
			
			content.title = (String) temp.get("title");
			content.year = (long) temp.get("year");
			content.url = (String) temp.get("url");
			content.runtime = (long) temp.get("runtime");
			content.overview = (String) temp.get("overview");
			
			tempImages = (JSONObject) temp.get("images");
			content.poster = (String) tempImages.get("poster");
			content.fanart = (String) tempImages.get("fanart");
			
			int x = 0;
			tempGenres =  (JSONArray) temp.get("genres");
			content.genres = new String[tempGenres.size()];
			for (Object string : tempGenres)
				content.genres[x++] = (String) string;
			
			//movie.downloadImg(context.getResources()); // XXX download in paralell
			result.add(content);
		}
		
		return result;
	}

}
