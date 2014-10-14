package com.storymakers.apps.trailguide.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.fragments.HikesListFragment.TGStory;

public class HikeArrayAdapter extends ArrayAdapter<TGStory> {
	private ImageLoader imageLoader;

	private static class ViewHolder {
		ImageView ivCoverPhoto;
		TextView tvLikes;
		TextView tvTitle;
		TextView tvRefs;
	}

	public HikeArrayAdapter(Context context, List<TGStory> stories) {
		super(context, R.layout.hike_list_item, stories);
		imageLoader = ImageLoader.getInstance();
		if (!imageLoader.isInited()) {
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
					cacheInMemory().cacheOnDisc().build();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
					.defaultDisplayImageOptions(defaultOptions)
					.build();
			imageLoader.init(config);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TGStory story = getItem(position);

		ViewHolder viewHolder;

		if (convertView == null) {  // No recycled view.
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.hike_list_item, parent, false);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		initializeViews(viewHolder, convertView);

		viewHolder.ivCoverPhoto.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(story.coverPhoto, viewHolder.ivCoverPhoto);
		viewHolder.tvLikes.setText(String.valueOf(story.numLikes));
		viewHolder.tvRefs.setText(String.valueOf(story.numRefs));
		viewHolder.tvTitle.setText(story.title);
		View v = (View) convertView.findViewById(R.id.rlHikeListItem);
		v.setTag(story);
		return convertView;
	}

	private void initializeViews(ViewHolder viewHolder, View convertView) {
		viewHolder.ivCoverPhoto = (ImageView) convertView.findViewById(R.id.ivCoverPhoto);
		viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
		viewHolder.tvRefs = (TextView) convertView.findViewById(R.id.tvRefs);
		viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		convertView.setTag(viewHolder);
	}
	
}