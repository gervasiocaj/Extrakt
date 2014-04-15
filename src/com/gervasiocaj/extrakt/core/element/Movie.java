package com.gervasiocaj.extrakt.core.element;

import java.util.Arrays;

public class Movie extends Content {
	
	public long released;

	@Override
	public String toString() {
		return "Movie [title=" + title + ", year=" + year + ", runtime="
				+ runtime + ", url=" + url + ", overview=" + overview
				+ ", poster=" + poster + ", fanart=" + fanart + ", imdb_id="
				+ imdb_id + ", genres=" + Arrays.toString(genres)
				+ ", released=" + released + "]";
	}
}
