package com.example.shishibdm;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// ��ȡ��ͼ�ؼ�����
	@ViewInject(R.id.bmapView)
	MapView mMapView;
	@ViewInject(R.id.tv)
	TextView tv;
	@ViewInject(R.id.surface_touch_view)
	View vsds;

	private int i;

	String[] statusNameArray = new String[] { "����", "��ͨ", "������", "������", "·����", "·����" };

	private BaiduMap mBaiduMap;

	private PoiSearch mPoiSearch;
	private Marker marker;

	void showLocPoint(LatLng loc, int drawable) {
		BitmapDescriptor bitmap;
		bitmap = BitmapDescriptorFactory.fromResource(drawable == 0 ? R.drawable.ic_launcher : drawable);
		// ����Markerͼ��

		// ����MarkerOption�������ڵ�ͼ�����Marker
		OverlayOptions option = new MarkerOptions().position(loc).icon(bitmap);
		// �ڵ�ͼ�����Marker������ʾ
		mBaiduMap.addOverlay(option);
	}

	void showLocPoint(LatLng loc) {
		showLocPoint(loc, 0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 //��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		 //ע��÷���Ҫ��setContentView����֮ǰʵ��

		 SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		
		
		ViewUtils.inject(this);
		
		
//		tv=(TextView)findViewById(R.id.tv);
		vsds.setClickable(true);
		vsds.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
					((TextView) findViewById(R.id.tv)).setText(statusNameArray[i % 6]);
					switch (i % 6) {
					case 0:
						mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);// ����
						break;
					case 1:
						mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);// ��ͨ
						break;
					case 2:
						mBaiduMap.setBaiduHeatMapEnabled(true);// ����ͼ
						break;
					case 3:
						mBaiduMap.setBaiduHeatMapEnabled(false);
						break;
					case 4:
						mBaiduMap.setTrafficEnabled(true);// ��ͨʵ��
						break;
					case 5:
						mBaiduMap.setTrafficEnabled(false);
						break;

					}
					i++;				
			}
		});
		
		
		

		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		
		
		// �Դ���λ����
		LocationManager mLocM = (LocationManager) getSystemService(this.LOCATION_SERVICE);
		Location mLoc = mLocM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (mLoc == null) {
			//   <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
		    
			mLoc = mLocM.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		showLocPoint(new LatLng(38, 116));
		showLocPoint(new LatLng(35.963175, 116.400244));
		showLocPoint(new LatLng(39.963175, 116.400244));
		showLocPoint(new LatLng(39.826804, 116.302499));

		
		OverlayOptions options = new MarkerOptions()
	    .position(new LatLng(20, 116))  //����marker��λ��
	    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))  //����markerͼ��
	    .zIndex(9)  //����marker���ڲ㼶
	    .draggable(true);  //����������ק
	//��marker��ӵ���ͼ��
	marker = (Marker) (mBaiduMap.addOverlay(options));
	
	 
	
	//����BaiduMap�����setOnMarkerDragListener��������marker��ק�ļ���
	mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
	    public void onMarkerDrag(Marker marker) {
	        //��ק��
	    }
	    public void onMarkerDragEnd(Marker marker) {
	        //��ק����
	    }
	    public void onMarkerDragStart(Marker marker) {
	        //��ʼ��ק
	    }
	});
		
		if(mLoc!=null){
			
			showLocPoint(new LatLng(mLoc.getLatitude(), mLoc.getLongitude()));
		}else {
			Toast.makeText(this,"mLoc==null", 0).show();
		}
		
		
		
		
		mPoiSearch = PoiSearch.newInstance();
		
		mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener(){  
		    public void onGetPoiResult(PoiResult result){  
		    //��ȡPOI�������  
		    	Toast.makeText(MainActivity.this,"run_here11", 0).show();
		    	showLocPoint(result.getAllPoi().get(0).location);
		    }  
		    public void onGetPoiDetailResult(PoiDetailResult result){   
		    //��ȡPlace����ҳ�������  
		    	Toast.makeText(MainActivity.this,"run_here22", 0).show();
		    	result.getLocation();
		    }  
		});
		
		
		mPoiSearch.searchInCity((new PoiCitySearchOption())  
			    .city("����")  
			    .keyword("��ʳ")  
			    .pageNum(1));
		
		
		mPoiSearch.destroy();
	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		// getMenuInflater().inflate(R.menu.main, menu);
//
//		((TextView) findViewById(R.id.tv)).setText(statusNameArray[i % 6]);
//		switch (i % 6) {
//		case 0:
//			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);// ����
//			break;
//		case 1:
//			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);// ��ͨ
//			break;
//		case 2:
//			mBaiduMap.setBaiduHeatMapEnabled(true);// ����ͼ
//			break;
//		case 3:
//			mBaiduMap.setBaiduHeatMapEnabled(false);
//			break;
//		case 4:
//			mBaiduMap.setTrafficEnabled(true);// ��ͨʵ��
//			break;
//		case 5:
//			mBaiduMap.setTrafficEnabled(false);
//			break;
//
//		}
//		i++;
//		return true;
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}
	


//	@Override
//	public void onBackPressed() {
//		TextView aa = new TextView(this);
//		aa.setText("ddsd");
//		aa.setHeight(200);
//		aa.setWidth(200);
//		aa.setGravity(Gravity.CENTER_VERTICAL);
//		mMapView.addView(aa);
//		mMapView.refreshDrawableState();
//	}

}

/**
 * 
 * package com.zhangyun.ylxl.enterprise.customer.activity;
 * 
 * @Override public void dealLogicAfterInitView() {
 * 
 * 
 *           }
 * @OnClick({ R.id.j_widght_general_head_tvRight,R.id.j_ss_tv })
 * @Override public void onClickEvent(View view) {
 * 
 *           break;
 * 
 *           case R.id.bmapView:
 * 
 *           break;
 * 
 *           default: break; }
 * 
 *           } }
 * 
 * 
 * 
 * 
 * */

