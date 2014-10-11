package com.storymakers.sandbox.app;

import java.util.Date;

import com.parse.LocationCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("TGPost")
public class TGPost extends ParseObject{
	public enum PostType {
		METADATA, NOTE, LOCATION, PHOTO
	}
	PostType type;
	private String note;
	private Date create_time;
	private ParseGeoPoint location;
	private String photo_url;
	private ParseFile photo;
	
	public TGPost() {
	}
	
	public static TGPost createNewPost(TGStory story, PostType type) {
		final TGPost post = new TGPost();
		post.type = type;
		post.setNote("");
		post.setPhoto_url("");
		
		post.setCreate_time(new Date(System.currentTimeMillis()));
		story.setLocation(new ParseGeoPoint(37.3526928, -121.97021484));
		
		return post;
	}

	public String getNote() {
		return getString("note");
	}

	public void setNote(String note) {
		put("note", note);
	}

	public String getCreate_time() {
		return getString("create_time");
	}

	public void setCreate_time(Date create_time) {
		put("create_time", create_time);
	}

	public ParseGeoPoint getLocation() {
		return (ParseGeoPoint) get("location");
	}

	public void setLocation(ParseGeoPoint location) {
		put("location", location);
	}

	public String getPhoto_url() {
		return getString("photo_url");
	}

	public void setPhoto_url(String photo_url) {
		put("photo_url", photo_url);
	}

	public ParseFile getPhoto() {
		return (ParseFile) get("photo_img");
	}

	public void setPhoto(ParseFile photo) {
		put("photo_img", photo);
	}
	
}