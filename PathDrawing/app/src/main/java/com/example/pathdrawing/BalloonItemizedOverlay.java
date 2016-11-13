package com.example.pathdrawing;



import java.lang.reflect.Method; 

import java.util.List;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public abstract class BalloonItemizedOverlay<Item> extends ItemizedOverlay<OverlayItem> {

	private MapView mapView;
	private BalloonOverlayView balloonView;
	private int viewOffset;
	final MapController mc;
	
	public BalloonItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker);
		this.mapView = mapView;
		viewOffset = 50; //마커 이미지와 말풍선 레이아웃 간의 거리
		mc = mapView.getController();
	}

	@Override
	protected final boolean onTap(int index) {
		
		boolean isRecycled;
		GeoPoint point = createItem(index).getPoint();

		if (balloonView == null) {
			balloonView = new BalloonOverlayView(mapView.getContext(), viewOffset);
			isRecycled = false;
		} else {
			isRecycled = true;
		}
		
		balloonView.setData(createItem(index)); 
		
		MapView.LayoutParams params = new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point,
				MapView.LayoutParams.BOTTOM_CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;
		//MODE_MAP은 스크린에 종속되지 않고 맵에 종속됨 내가 맵을 어디로 알려주든
		//인자값으로 넘긴 point좌표에 고정된다는 뜻
		
		balloonView.setVisibility(View.VISIBLE); //framelayout내의 linearlayout, title,snippet을 먼저 위에서
                                                 //setvisibility로 VISIBLE상태로 만든 후 가장 바깥 껍질인 FrameLayout의
                                                 //상태를 VISIBLE상태로 만드는 부분.

		if (isRecycled) {
			balloonView.setLayoutParams(params);
		} else {
			mapView.addView(balloonView, params);
		}
		
		mc.animateTo(point);
		
		return true;
	}
}
