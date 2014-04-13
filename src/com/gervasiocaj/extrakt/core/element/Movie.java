package com.gervasiocaj.extrakt.core.element;

import java.util.Arrays;

public class Movie extends Content {
	
	public long released;
	
	@Override
	public String toString() {
		return "Movie [title=" + title + ", runtime=" + runtime + ", year="
				+ year + ", released=" + released + ", url=" + url
				+ ", overview=" + overview + ", poster=" + poster + ", fanart="
				+ fanart + ", genres=" + Arrays.toString(genres) + "]";
	}
}
