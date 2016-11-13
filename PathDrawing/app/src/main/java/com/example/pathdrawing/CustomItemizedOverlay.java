package com.example.pathdrawing;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class CustomItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	public ArrayList<OverlayItem> balloonOverlays = new ArrayList<OverlayItem>();
	public Context context;
	

	public CustomItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		context = mapView.getContext();
	}

	public void addOverlay(OverlayItem overlay) {
	    balloonOverlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return balloonOverlays.get(i);
	}

	@Override
	public int size() {
		return balloonOverlays.size();
	}
}
