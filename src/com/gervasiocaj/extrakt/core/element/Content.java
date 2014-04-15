package com.gervasiocaj.extrakt.core.element;

import java.util.Arrays;

import android.graphics.drawable.Drawable;

public class Content {
	
	public String title;
    public long year, runtime;
    public String url, overview, poster, fanart, imdb_id;
    public String[] genres;
    public Drawable posterImg = null;

    @Override
    public String toString() {
    	return "Content [title=" + title + ", year=" + year + ", runtime="
    			+ runtime + ", url=" + url + ", overview=" + overview
    			+ ", poster=" + poster + ", fanart=" + fanart + ", imdb_id="
    			+ imdb_id + ", genres=" + Arrays.toString(genres) + "]";
    }
}
