package com.example.pathdrawing;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.OverlayItem;

public class BalloonOverlayView extends FrameLayout {

	private LinearLayout layout;
	private TextView allPath;
	private TextView sectionPath;

	public BalloonOverlayView(Context context, int balloonBottomOffset) {

		super(context);

		setPadding(10, 0, 10, balloonBottomOffset);
		layout = new LinearLayout(context); //동적 레이아웃 
		layout.setVisibility(VISIBLE);

		setupView(context, layout);
		
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.NO_GRAVITY;

		addView(layout, params);
	}
	
	protected void setupView(Context context, final ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.balloon_overlay, layout);
		allPath = (TextView) view.findViewById(R.id.all_path);
		sectionPath = (TextView) view.findViewById(R.id.section_path);

		view.findViewById(R.id.balloon_close).setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) {
				layout.setVisibility(GONE);
			}
		});
	}
	public void setData(OverlayItem item) {
		layout.setVisibility(VISIBLE);
		
		allPath.setVisibility(VISIBLE);
		allPath.setText(item.getTitle());
		
		sectionPath.setVisibility(VISIBLE);
		sectionPath.setText(item.getSnippet());
	}
}
