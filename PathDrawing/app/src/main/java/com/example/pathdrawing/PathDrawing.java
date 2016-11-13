package com.example.pathdrawing;

import java.util.ArrayList;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Menu;

public class PathDrawing extends MapActivity {
	MapView mapView;
	Drawable marker;

	public GeoPoint oPsave, recvOp;
	GeoPoint startPoint = new GeoPoint(37451000, 126656000);

	OverlayItem overlayItem;
	CustomItemizedOverlay customItemizedOverlay;
	
	List<Overlay> mapOverlays;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path_drawing);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		MapController mc = mapView.getController();
		mc.animateTo(startPoint);
		mc.setZoom(17);
		
		mapOverlays = mapView.getOverlays();

		marker = getResources().getDrawable(R.drawable.ic_pin_01);
		customItemizedOverlay = new CustomItemizedOverlay(marker, mapView);
		mapView.getOverlays().add(new MyOverlay(marker));
	}

	class MyOverlay extends ItemizedOverlay<OverlayItem> {
		public ArrayList<Vertex> arVertex = new ArrayList<Vertex>();
		public ArrayList<Vertex> arVertexOri = new ArrayList<Vertex>();
		
		Drawable marker;
		
		public float allPath = 0;

		public MyOverlay(Drawable marker) {
			super(marker);
			this.marker = marker;
			populate();
		}

		public boolean onTap(GeoPoint p, MapView mapView) {
			recvOp = new GeoPoint((int) p.getLatitudeE6(),(int) p.getLongitudeE6());
			
			if(oPsave == null)
			{
				oPsave = recvOp;
			}
			
			float[] dis = new float[2];
			
			Location.distanceBetween(oPsave.getLatitudeE6()/1E6, oPsave.getLongitudeE6()/1E6, 
					recvOp.getLatitudeE6()/1E6, recvOp.getLongitudeE6()/1E6, dis);
            allPath += dis[0]/1000;
			
			customItemizedOverlay.addOverlay(new OverlayItem(recvOp, 
					"현재위치까지의 총 거리 : "+allPath+" km", 
					"구간 거리 : "+dis[0]/1000+" km"));
			mapOverlays.add(customItemizedOverlay);
			
			oPsave = recvOp;
			 
			return true;
		}

		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// TODO Auto-generated method stub
			super.draw(canvas, mapView, shadow);
			boundCenterBottom(marker);
		    
			Paint paint1 = new Paint();
			paint1.setStrokeWidth(4);
			paint1.setARGB(255, 255, 0, 0); 
			
			if (recvOp == null) return;

			arVertexOri.add(new Vertex((int) recvOp.getLatitudeE6(),
					(int) recvOp.getLongitudeE6(), true));

			arVertex.clear(); 

			Point pixPoint = new Point();

			for (int i = 1; i < arVertexOri.size(); i++) {
				GeoPoint k = new GeoPoint((int) arVertexOri.get(i).x,
						(int) arVertexOri.get(i).y);
				mapView.getProjection().toPixels(k, pixPoint);

				arVertex.add(new Vertex(pixPoint.x, pixPoint.y, true));
			}

			for (int i = 1; i < arVertex.size(); i++) {
				if (arVertex.get(i).Draw) {
					canvas.drawLine(arVertex.get(i - 1).x,
							arVertex.get(i - 1).y, arVertex.get(i).x,
							arVertex.get(i).y, paint1);
				}
			}
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
