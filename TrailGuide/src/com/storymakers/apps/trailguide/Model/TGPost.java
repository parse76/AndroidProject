package com.storymakers.apps.trailguide.model;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import org.parceler.Parcel;

@ParseClassName("TGPost") 
@Parcel
public class TGPost extends ParseObject {
	public enum PostType {
		METADATA(0), NOTE(1), LOCATION(2), PHOTO(3);

		private int numVal;

		PostType(int numVal) {
			this.numVal = numVal;
		}

		public int getNumVal() {
			return numVal;
		}
	}

	public interface PostListDownloadCallback {
		void done(List<TGPost> objs);

		void fail(String reason);
	}

	public static final String KEY_POST_SEQUENCE = "seq_id";
	public static final String KEY_POST_TYPE = "postType";

	int type;
	private String note;
	private Date create_time;
	private ParseGeoPoint location;
	private String photo_url;
	private ParseFile photo;
	private TGStory story;
	private long sequence_id;

	public TGPost() {
	}

	// Test constructor for location posts
	// Don't include in original code
	public TGPost(double lat, double lng) {
		setType(PostType.PHOTO);
		setPhoto_url("http://images6.fanpop.com/image/photos/33200000/cute-puppy-dogs-33237869-1024-768.jpg");
		// type = PostType.NOTE;
		// setNote("This is a note");
		setLocation(lat, lng);
	}

	@Override
	public String toString() {
		String retval = "Post (" + getObjectId() + ")";
		ParseGeoPoint loc = getLocation();
		if (loc != null) {
			retval += "type: " + getType().getNumVal() + ", location: " + loc.getLatitude() + ", "
					+ loc.getLongitude();
		} else {
			retval += "type: " + getType().getNumVal();
		}
		
		if (getType() == PostType.PHOTO) {
			retval = " " + getPhoto_url() + " " + retval;
		}
		return retval;
	}

	public static TGPost createNewPost(TGStory story, PostType type) {
		final TGPost post = new TGPost();
		post.seType(type);
		post.setNote("");
		post.setPhoto_url("");

		post.setCreate_time(new Date(System.currentTimeMillis()));
		post.setLocation(37.3526928, -121.97021484);
		post.setStory(story);

		return post;
	}

	private void seType(PostType t) {
		put(KEY_POST_TYPE, t.getNumVal());
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

	public void setLocation(float[] latlong) {
		if (latlong != null) {
			ParseGeoPoint gp = new ParseGeoPoint(latlong[0], latlong[1]);
			put("location", gp);
		}
	}

	public void setLocation(double latitude, double longitude) {
		float[] latlong = { (float) latitude, (float) longitude };
		setLocation(latlong);
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

	public void setPhotoFromUri(Context ctx, Uri photouri) {
		setPhoto(TGUtils.getBytesFromUri(ctx, photouri));
		setLocation(TGUtils.getGeoLocationFromPhoto(photouri.getPath()));
	}

	public void setPhoto(byte[] imageData) {
		final ParseFile ph = new ParseFile("ph"
				+ Long.toString(System.currentTimeMillis()) + ".jpeg",
				imageData);
		put("photo_img", ph);
		ph.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					TGPost.this.setPhoto_url(ph.getUrl());
				} else {
					e.printStackTrace();
				}

			}
		});
	}

	public void setStory(TGStory s) {
		if (s != null)
			put("story", s);
		this.story = s;
	}

	public TGStory getStory() {
		return (TGStory) get("story");
	}

	public TGPost.PostType getType() {
		int t = getInt(KEY_POST_TYPE);
		if (t == PostType.METADATA.getNumVal()) {
			return PostType.METADATA;
		}
		if (t == PostType.LOCATION.getNumVal()) {
			return PostType.LOCATION;
		}
		if (t == PostType.PHOTO.getNumVal()) {
			return PostType.PHOTO;
		}
		if (t == PostType.NOTE.getNumVal()) {
			return PostType.NOTE;
		}
		return PostType.METADATA;
	}

	public void setType(PostType t) {
		type = t.getNumVal();
		put(KEY_POST_TYPE, type);
	}

	public long getSequenceId() {
		long v = getLong(KEY_POST_SEQUENCE);
		sequence_id = v;
		return v;
	}

	public void setSequenceId(long sequence_id) {
		this.sequence_id = sequence_id;
		if (sequence_id != getSequenceId()) {
			put(KEY_POST_SEQUENCE, sequence_id);
		}
	}

	public void saveData() {
		ParseFile p = getPhoto();
		if (p != null) {
			p.saveInBackground();
		}
		saveInBackground();
	}
	
}
