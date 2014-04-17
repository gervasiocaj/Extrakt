package com.gervasiocaj.extrakt.core.element;

import java.util.Arrays;

public class TVShow extends Content {
	
    public long first_aired;
	public long completed;

	@Override
	public String toString() {
		return "TVShow [title=" + title + ", year=" + year + ", runtime="
				+ runtime + ", url=" + url + ", overview=" + overview
				+ ", poster=" + poster + ", fanart=" + fanart + ", imdb_id="
				+ imdb_id + ", genres=" + Arrays.toString(genres)
				+ ", posterImg=" + posterImg + ", first_aired=" + first_aired
				+ "]";
	}
}
