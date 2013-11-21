package com.example.handle;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;

import com.example.tracingtaxi.MainActivity;
import com.example.tracingtaxi.R;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class ListenToMyLocation implements Runnable {

	/*private GoogleMap mMap;

	private LocationClient mLocationClient;*/
	//����һ��������������Ļ����
	private  boolean LOCKCAMERA = false;
	
	
	private MainActivity activity=null;
	private PolylineOptions polylineOptions=new PolylineOptions();
	private CameraUpdate update;
	private GoogleMap map;
	private Handler handler;
	private LatLng LastPosition;
	private LatLng CurrentFriendLocation=new LatLng(30.28397, 121.20738000000006);

	
	public float CurrentZoom = 16f;
	public float MinZoom = 16f;
	
	//��ǰ���ĵ�
	public Marker marker;

	//����һ������Ϊ���ѱ�ǵ�˳��
	public int FriendLocationOrder=0;
	float i=1;
	Marker mBrisbane;
	public ListenToMyLocation(MainActivity a,Handler h)
	{
		this.activity=a;
		this.handler=h;
	
		
		
		
		
		}
		
	//��ס�����ӽ�
	public void lockCamera()
	{
		this.LOCKCAMERA=true;
		
	}
	//�����ӽ�
	public void getFriendCamera(LatLng location)
	{
		CameraPosition cameraPosition  = new  CameraPosition.Builder().zoom(CurrentZoom).bearing(0)
                .tilt(0).target(location).build();
		
		 setUpdate(CameraUpdateFactory.newCameraPosition(cameraPosition));
		 this.activity.mMap.moveCamera(update);
	}
	//���������ӽ�
	public void unlockCamera()
	{
		this.LOCKCAMERA=false;
		
	}
	//�õ���ǰ�ӽǵ�����״̬
	public boolean getLock()
	{
		return LOCKCAMERA;
		
	}
	
	//�Զ���λ���ҵ�λ��
	private void onGoToMyLocation()
	{
	
		//if (this.activity.mLocationClient != null && this.activity.mLocationClient.isConnected()&&this.activity.mLocationClient.getLastLocation()!=null) {
			//map=((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
		     //       .getMap();
			LatLng location =new LatLng(Math.min(CurrentFriendLocation.latitude+(i+=0.00001),90),
					    Math.min(CurrentFriendLocation.longitude,180f));
			//System.out.println(location.latitude+"  "+location.longitude);
			
			if(marker==null)
			{
				marker = this.activity.mMap.addMarker(new MarkerOptions().position(location)
	            .title("�ҵĺ���")
	        .snippet("��������־��")
	        .icon(BitmapDescriptorFactory.fromResource(R.drawable.spot2))
	        .infoWindowAnchor(0.5f, 0.5f));
				
			}else{
			marker.setPosition(location);
			}
			//�����Ļ��������
			if(LOCKCAMERA){
				this.getFriendCamera(location);
			/*CameraPosition cameraPosition  = new  CameraPosition.Builder().target(location).bearing(0)
                    .tilt(0).build();
			
			 setUpdate(CameraUpdateFactory.newCameraPosition(cameraPosition));
			 this.activity.mMap.moveCamera(update);*/
			}else
			{
				if(this.activity.cameraChange)
				{
					this.CurrentZoom=this.activity.RecordCamera;
					this.activity.cameraChange=false;
				}
			}
			 
			
				
			polylineOptions.add(location);
			
			//��һ����
			if(getLastPosition()!=null)
			{
				
				
			
			 //����
			Polyline line = this.activity.mMap.addPolyline(new PolylineOptions().add(getLastPosition())
		     .add(location)
		     .width(7)
		     .color(Color.BLUE));
			
			}else
			{
				Polyline line = this.activity.mMap.addPolyline(new PolylineOptions()
					     .add(location)
					     .width(7)
					     .color(Color.BLUE));
				
			}
			setLastPosition(location);
			
		//	System.out.println("success: "+Thread.currentThread().getId());
		    
	//	}
		
	
		
		}
	
	@Override
public void run() {
	// TODO Auto-generated method stub
		
		   this.onGoToMyLocation();
		   this.handler.postDelayed(this, 500);
		   
		  
	
}

	//�л��ӽǵ�����λ��
	public void goToFriendLocation()
	{
		if(getLastPosition()!=null)
		{
		 this.getFriendCamera(getLastPosition());
		 
		 // Uses a colored icon.
		   this.activity.mMap.clear();
		   FriendLocationOrder=0;
		   
		   this.activity.mMap.addPolyline(polylineOptions.width(7)
				     .color(Color.BLUE));
		   
	       mBrisbane = this.activity.mMap.addMarker(new MarkerOptions()
	                .position(getLastPosition())
	                .title("����λ��"+FriendLocationOrder++)
	                .snippet("γ��"+(getLastPosition().latitude)+"����"+getLastPosition().longitude)
	                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		}
	}
	
	//���浱ǰ��������
		public void markFriendLocation()
		{
			if(getLastPosition()!=null)
			{
				 this.getFriendCamera(getLastPosition());
			 
			 // Uses a colored icon.
			   
			   
		       mBrisbane = this.activity.mMap.addMarker(new MarkerOptions()
		                .position(getLastPosition())
		                 .title("����λ��"+FriendLocationOrder++)
	                .snippet("γ��"+(getLastPosition().latitude)+"����"+getLastPosition().longitude)
		                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			}
		}


	public CameraUpdate getUpdate() {
		return update;
	}



	public void setUpdate(CameraUpdate update) {
		this.update = update;
	}

	public void clearMark() {
		// TODO Auto-generated method stub
		this.activity.mMap.clear();
		this.FriendLocationOrder=0;
		this.activity.mMap.addPolyline(polylineOptions.width(5)
			     .color(Color.BLUE));
		
	}

	public LatLng getLastPosition() {
		return LastPosition;
	}

	public void setLastPosition(LatLng lastPosition) {
		LastPosition = lastPosition;
	}
	
	
}