package com.hijiyama_koubou.ja090dm;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import net.nend.android.NendAdView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private LinearLayout ad_layout;
	private PublisherAdView adView;
	private LinearLayout nend_layout;
	private NendAdView nendAdView;
	private WebView webView;
	private EditText url_et;
	private String urlStr ="https://www.yahoo.co.jp/";
	private String AdUnitID ="";			//"ca-app-pub-3940256099942544/6300978111";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = ( FloatingActionButton ) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view , "Replace with your own action" , Snackbar.LENGTH_LONG).setAction("Action" , null).show();
			}
		});

		DrawerLayout drawer = ( DrawerLayout ) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawer , toolbar , R.string.navigation_drawer_open , R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = ( NavigationView ) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		
		ad_layout = findViewById(R.id.ad_layout);
		nend_layout = findViewById(R.id.nend_layout);
		webView = findViewById(R.id.webView);
		url_et = findViewById(R.id.url_et);
		//広告表示//////////////////////////////////////////////////
		AdUnitID = getString (R.string.banner_ad_unit_id);
		MobileAds.initialize(this,getResources ().getString (R.string.banner_ad_unit_id));       //Mobile Ads SDK を初期化

	}
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		setWebView();
		setADSens();//広告表示//////////////////////////////////////////////////
		setNend();
	}

	/** Called when leaving the activity */
	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	/** Called when returning to the activity */
	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	/** Called before the activity is destroyed */
	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	////////////////////////////////////////////////////////////////////////////
	public void setWebView() {
		url_et.setText(urlStr);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(urlStr);
//		url_et.setFocusable(false);
//		url_et.setFocusableInTouchMode(false);

		url_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//テキスト変更前
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//テキスト変更中
			}

			@Override
			public void afterTextChanged(Editable s) {
				urlStr = s.toString();
				webView.loadUrl(urlStr);
				//テキスト変更後
			}
		});
	}
	////////////////////////////////////////////////////////////////////////////
	public void setADSens() {
		adView = new PublisherAdView(this);
		adView.setAdUnitId(AdUnitID);

		float scale = getResources().getDisplayMetrics().density;
		int layoutWidth =(int)(ad_layout.getWidth()/scale);
		int layoutHeight =(int)(ad_layout.getHeight()/scale);

		AdSize customAdSize = new AdSize(layoutWidth, layoutHeight);
		adView.setAdSizes(customAdSize);
		ad_layout.addView(adView);

		PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
		adView.loadAd(adRequest);

	}

	////////////////////////////////////////////////////////////////////////////
	public void setNend() {               		//https://github.com/fan-ADN/nendSDK-Android/wiki/%E3%83%90%E3%83%8A%E3%83%BC%E5%9E%8B%E5%BA%83%E5%91%8A_%E5%AE%9F%E8%A3%85%E6%89%8B%E9%A0%86
		int nend_spotID = Integer.parseInt(getString (R.string.nend_spotID));
		nendAdView = new NendAdView(this,nend_spotID, getString (R.string.nend_apiKey));    		// 1 NendAdView をインスタンス化
		nend_layout.addView(nendAdView); 		// 2 NendAdView をレイアウトに追加
		nendAdView.loadAd();     		// 3 広告の取得を開始
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = ( DrawerLayout ) findViewById(R.id.drawer_layout);
		if ( drawer.isDrawerOpen(GravityCompat.START) ) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main , menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if ( id == R.id.action_settings ) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings ( "StatementWithEmptyBody" )
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if ( id == R.id.nav_camera ) {
			// Handle the camera action
		} else if ( id == R.id.nav_gallery ) {

		} else if ( id == R.id.nav_slideshow ) {

		} else if ( id == R.id.nav_manage ) {

		} else if ( id == R.id.nav_share ) {

		} else if ( id == R.id.nav_send ) {

		}

		DrawerLayout drawer = ( DrawerLayout ) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
