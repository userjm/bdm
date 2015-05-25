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
	// 获取地图控件引用
	@ViewInject(R.id.bmapView)
	MapView mMapView;
	@ViewInject(R.id.tv)
	TextView tv;
	@ViewInject(R.id.surface_touch_view)
	View vsds;

	private int i;

	String[] statusNameArray = new String[] { "卫星", "普通", "热力开", "热力关", "路况开", "路况关" };

	private BaiduMap mBaiduMap;

	private PoiSearch mPoiSearch;
	private Marker marker;

	void showLocPoint(LatLng loc, int drawable) {
		BitmapDescriptor bitmap;
		bitmap = BitmapDescriptorFactory.fromResource(drawable == 0 ? R.drawable.ic_launcher : drawable);
		// 构建Marker图标

		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(loc).icon(bitmap);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);
	}

	void showLocPoint(LatLng loc) {
		showLocPoint(loc, 0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 //在使用SDK各组件之前初始化context信息，传入ApplicationContext
		 //注意该方法要再setContentView方法之前实现

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
						mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);// 卫星
						break;
					case 1:
						mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);// 普通
						break;
					case 2:
						mBaiduMap.setBaiduHeatMapEnabled(true);// 热力图
						break;
					case 3:
						mBaiduMap.setBaiduHeatMapEnabled(false);
						break;
					case 4:
						mBaiduMap.setTrafficEnabled(true);// 交通实况
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
		
		
		// 自带的位置类
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
	    .position(new LatLng(20, 116))  //设置marker的位置
	    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))  //设置marker图标
	    .zIndex(9)  //设置marker所在层级
	    .draggable(true);  //设置手势拖拽
	//将marker添加到地图上
	marker = (Marker) (mBaiduMap.addOverlay(options));
	
	 
	
	//调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
	mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
	    public void onMarkerDrag(Marker marker) {
	        //拖拽中
	    }
	    public void onMarkerDragEnd(Marker marker) {
	        //拖拽结束
	    }
	    public void onMarkerDragStart(Marker marker) {
	        //开始拖拽
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
		    //获取POI检索结果  
		    	Toast.makeText(MainActivity.this,"run_here11", 0).show();
		    	showLocPoint(result.getAllPoi().get(0).location);
		    }  
		    public void onGetPoiDetailResult(PoiDetailResult result){   
		    //获取Place详情页检索结果  
		    	Toast.makeText(MainActivity.this,"run_here22", 0).show();
		    	result.getLocation();
		    }  
		});
		
		
		mPoiSearch.searchInCity((new PoiCitySearchOption())  
			    .city("北京")  
			    .keyword("美食")  
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
//			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);// 卫星
//			break;
//		case 1:
//			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);// 普通
//			break;
//		case 2:
//			mBaiduMap.setBaiduHeatMapEnabled(true);// 热力图
//			break;
//		case 3:
//			mBaiduMap.setBaiduHeatMapEnabled(false);
//			break;
//		case 4:
//			mBaiduMap.setTrafficEnabled(true);// 交通实况
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
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
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

