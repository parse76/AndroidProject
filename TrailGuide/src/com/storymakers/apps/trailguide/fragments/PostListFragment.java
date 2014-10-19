package com.storymakers.apps.trailguide.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.adapters.StoryPostAdapter;
import com.storymakers.apps.trailguide.model.TGPost;

public class PostListFragment extends Fragment {
	private ArrayList<TGPost> posts;
	private StoryPostAdapter storyPostAdapter;
	private ListView lvStoryPosts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		posts = new ArrayList<TGPost>();
		storyPostAdapter = new StoryPostAdapter(getActivity(), posts);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_post_list, container,
				false);
		
		lvStoryPosts = (ListView) v.findViewById(R.id.lvStoryPosts);
		lvStoryPosts.setAdapter(storyPostAdapter);

		return v;
	}

	public void addAll(List<TGPost> postsList) {
		storyPostAdapter.addAll(postsList);
	}

	public void addPost(TGPost p) {
		storyPostAdapter.add(p);
	}
}