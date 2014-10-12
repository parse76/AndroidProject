package com.storymakers.sandbox.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class UploadPhotoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_photo);
	}

public class UploadPhotoActivity extends Activity {
	public final String APP_TAG = "MyCustomApp";
	public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
	public String photoFileName = "newpo.jpg";
	private Uri photoUriToSave;
	private String photoNametoSave;
	
	TGStory story;
	TGUser u;
	EditText etNote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_photo);
		etNote = (EditText) findViewById(R.id.etNote);
		u = ParseClient.getCurrentUser();
	}

	public void runListPhotos(View v) {
		if (etNote.getText().toString().startsWith("create")) {
			story = TGStory.createNewStory(u, etNote.getText().toString().replace("create", ""));
			story.saveEventually();
			Toast.makeText(this, "Created a story", Toast.LENGTH_SHORT).show();
		}
		if (story == null) {
			ArrayList<TGStory> stories = u.getAllStories();
			if (stories.size() > 0) {
				story = stories.get(0);
				Toast.makeText(this,
						"Found a story with title" + story.getTitle(),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public void addNoteAction(View v) {
		
		String text = etNote.getText().toString();
		if (text.length() > 0) {
			TGPost p = TGPost.createNewPost(story, PostType.NOTE);
			p.setNote(text);
			p.saveData();
			Toast.makeText(this, "Saved a note", Toast.LENGTH_SHORT).show();
			etNote.setText("");
		} else {
			Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void onLaunchCamera(View view) {
		photoNametoSave = Long.toString(System.currentTimeMillis())
				+ photoFileName;
		photoUriToSave = getPhotoFileUri(photoNametoSave);

		// create Intent to take a picture and return control to the calling
		// application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUriToSave); // set the
																	// image
																	// file name
		// Start the image capture intent to take photo
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri takenPhotoUri = photoUriToSave; // getPhotoFileUri(photoFileName);
				// by this point we have the camera photo on disk
				Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri
						.getPath());
				// Load the taken image into a preview
				ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
				ivPreview.setImageBitmap(takenImage);
				savePhotoUriToParse(photoNametoSave, photoUriToSave);
			} else { // Result was a failure
				Toast.makeText(this, "Picture wasn't taken!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// Returns the Uri for a photo stored on disk given the fileName
	public Uri getPhotoFileUri(String fileName) {

		// Get safe storage directory for photos
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				APP_TAG);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
			Log.d(APP_TAG, "failed to create directory");
		}

		// Return the file target for the photo based on filename
		return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator
				+ fileName));
	}

	public byte[] getBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		return byteBuffer.toByteArray();
	}

	public void savePhotoUriToParse(String name, Uri photo) {
		InputStream iStream;
		byte[] inputData = null;
		try {
			iStream = getContentResolver().openInputStream(photo);
			try {
				inputData = getBytes(iStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (inputData != null) {
			ParseFile pphoto = new ParseFile(name, inputData);
			TGPost p = TGPost.createNewPost(story, TGPost.PostType.PHOTO);
			p.setPhoto(pphoto);
			p.setLocation(getGeoLocationFromPhoto(photo.getEncodedPath()));
			p.saveData();
			Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show();
		}
	}

	public ParseGeoPoint getGeoLocationFromPhoto(String photo_path) {
		ParseGeoPoint retval = null;
		ExifInterface exif;
		try {
			exif = new ExifInterface(photo_path);
			float latlong[] = { (float) 0.0, (float) 0.0 };
			if (exif.getLatLong(latlong)) {
				retval = new ParseGeoPoint(latlong[0], latlong[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retval;
	}
}
