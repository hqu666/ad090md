package com.hijiyama_koubou.ja090dm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import net.nend.android.NendAdView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private LinearLayout ad_layout;
	private PublisherAdView adView;
	private ViewGroup content_ll;
//	private LinearLayout content_ll;
	private LinearLayout nend_layout;
	private NendAdView nendAdView;
	private WebView webView;
	private EditText url_et;
	private String urlStr ="https://www.yahoo.co.jp/";
	private String AdUnitID ="";			//"ca-app-pub-3940256099942544/6300978111";

	private String rootUrlStr  ="https://www.yahoo.co.jp/";
	private int topView = R.layout.activity_qr;
	private int nowView = topView;

	/**
	 * このアプリケーションの設定ファイル読出し
	 **/
	public void readPref() {
		final String TAG = "readPref[RBS]";
		String dbMsg = "許諾済み";//////////////////
		try {
			if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {                //(初回起動で)全パーミッションの許諾を取る
				dbMsg = "許諾確認";
				String[] PERMISSIONS = { Manifest.permission.INTERNET , Manifest.permission.ACCESS_NETWORK_STATE,
										 Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE
										};
//				Manifest.permission.ACCESS_NETWORK_STATE , Manifest.permission.ACCESS_WIFI_STATE ,
// , Manifest.permission.MODIFY_AUDIO_SETTINGS , Manifest.permission.RECORD_AUDIO ,  Manifest.permission.MODIFY_AUDIO_SETTINGS,        Manifest.permission.CAMERA
				boolean isNeedParmissionReqest = false;
				for ( String permissionName : PERMISSIONS ) {
					dbMsg += "," + permissionName;
					int checkResalt = checkSelfPermission(permissionName);
					dbMsg += "=" + checkResalt;
					if ( checkResalt != PackageManager.PERMISSION_GRANTED ) {
						isNeedParmissionReqest = true;
					}
				}
				if ( isNeedParmissionReqest ) {
					dbMsg += "許諾処理へ";
					requestPermissions(PERMISSIONS , REQUEST_PREF);
					return;
				}
			}
//			dbMsg += ",isReadPref=" + isReadPref;
			MyPreferenceFragment prefs = new MyPreferenceFragment();
			prefs.readPref(this);
//			rootUrlStr = prefs.rootUrlStr;
//			dbMsg += ",rootUrlStr=" + rootUrlStr;
//			readFileName = prefs.readFileName;
//			dbMsg += ",readFileName=" + readFileName;
//			savePatht = prefs.savePatht;
//			dbMsg += ",作成したファイルの保存場所=" + savePatht;
//			isStartLast = prefs.isStartLast;
//			dbMsg += ",次回は最後に使った元画像からスタート=" + isStartLast;
//			is_v_Mirror = prefs.is_v_Mirror;
//			dbMsg += ",左右鏡面動作=" + is_v_Mirror;
//			is_h_Mirror = prefs.is_h_Mirror;
//			dbMsg += ",上下鏡面動作=" + is_h_Mirror;
//			isAautoJudge = prefs.isAautoJudge;
//			dbMsg += ",トレース後に自動判定=" + isAautoJudge;
//			traceLineWidth = prefs.traceLineWidth;
//			dbMsg += ",トレース線の太さ=" + traceLineWidth;
//			isPadLeft = prefs.isPadLeft;
//			dbMsg += ",左側にPad=" + isPadLeft;
//			isLotetCanselt = prefs.isLotetCanselt;
//			dbMsg += ",自動回転阻止=" + isLotetCanselt;
//			sharedPref = PreferenceManager.getDefaultSharedPreferences(this);            //	getActivity().getBaseContext()
//			myEditor = sharedPref.edit();
//			stereoTypeRady( "");

			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}
	static final int REQUEST_PREF = 100;                          //Prefarensからの戻り

	@Override
	protected void onActivityResult(int requestCode , int resultCode , Intent data) {
		final String TAG = "onActivityResult[MA]";
		String dbMsg = "requestCode=" + requestCode + ",resultCode=" + resultCode;
		try {
			switch ( requestCode ) {
				case REQUEST_PREF:                                //Prefarensからの戻り
					readPref();
					break;
			}
//			IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//			if(result != null) {
//				String dataURI  = result.getContents();
//				dbMsg = ",dataURI="+dataURI;
//				Intent webIntent = new Intent(this , Web_Activity.class);
//				webIntent.putExtra("dataURI" , dataURI);
//				startActivity(webIntent);
//			} else {
//				super.onActivityResult(requestCode, resultCode, data);
//			}

			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	/**
	 * Cameraパーミッションが通った時点でstartLocalStream
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode , String permissions[] , int[] grantResults) {
		final String TAG = "onRequestPermissionsResult[MA]";
		String dbMsg = "";
		try {
			dbMsg = "requestCode=" + requestCode;
			switch ( requestCode ) {
				case REQUEST_PREF:
//					readPref();        //ループする？
					break;
			}
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		final String TAG = "dispatchTouchEvent[RBS]";
//		String dbMsg = "";
//		boolean retBool = false; //trueに設定すると「TouchEventを消化」したものとして他に送らない
//		try {
//			float xPoint = event.getX();   //画面上での座標
//			float yPoint = event.getY();
//			int action = event.getAction();
//			dbMsg += "(" + xPoint + "×" + yPoint + ")action=" + action;
////		if (ev.getAction() == MotionEvent.ACTION_UP) {
////			if (listener != null) {
////				post(new Runnable() {
////					@Override
////					public void run() {
////						listener.onClick(CustomButtonView.this);
////					}
////				});
////			}
////		}
//			retBool = true;
//			myLog(TAG , dbMsg);
//		} catch (Exception er) {
//			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
//		}
////		return super.dispatchTouchEvent(event);
//		return retBool;
//	}
	public void callQuit() {
		final String TAG = "callQuit[MA]";
		String dbMsg = "";
		try {
//			sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);            //	getActivity().getBaseContext()
//			myEditor = sharedPref.edit();
////			myEditor.putString("peer_id_key" , "");      //使用した
////			boolean kakikomi = myEditor.commit();
			if(nowView == topView){
				this.finish();
				if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
					finishAndRemoveTask();                      //アプリケーションのタスクを消去する事でデバッガーも停止する。
				} else {
					moveTaskToBack(true);                       //ホームボタン相当でアプリケーション全体が中断状態
				}
			}else{
				setTopFragument();
//				setTopView();
			}
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	public void reStart() {
		final String TAG = "reStart[MA}";
		String dbMsg = "";
		try {
//			Intent intent = new Intent();
//			intent.setClass(this , this.getClass());
//			this.startActivity(intent);
//			this.finish();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean onKeyDown(int keyCode , KeyEvent event) {
		final String TAG = "onKeyDown";
		String dbMsg = "開始";
		try {
			dbMsg = "keyCode=" + keyCode;//+",getDisplayLabel="+String.valueOf(MyEvent.getDisplayLabel())+",getAction="+MyEvent.getAction();////////////////////////////////
			myLog(TAG , dbMsg);
			switch ( keyCode ) {    //キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
				case KeyEvent.KEYCODE_HOME:            //3
				case KeyEvent.KEYCODE_BACK:            //4KEYCODE_BACK :keyCode；09SH: keyCode；4,MyEvent=KeyEvent{action=0 code=4 repeat=0 meta=0 scancode=158 mFlags=72}
					callQuit();
					return true;
				default:
					return false;
			}
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
			return false;
		}
	}
	/**
	 * MainActivityのメニュー
	 * ドロワーと共通になるので関数化
	 */
	public boolean funcSelected(MenuItem item) {
		final String TAG = "funcSelected[RBS]";
		String dbMsg = "MenuItem" + item.toString();/////////////////////////////////////////////////
		try {
			Bundle bundle = new Bundle();
			CharSequence toastStr = "";
			int id = item.getItemId();
			dbMsg = "id=" + id;
			switch ( id ) {
				case R.id.md_call_top:
//				case R.id.mm_call_top:
					setTopFragument();
//					setTopView();
					break;
				case R.id.md_call_web2:
//				case R.id.mm_call_web2:
					nowView = R.layout.activity_web; 					//表示中のview
					Intent webIntent = new Intent(this , Web_Activity.class);
					String dataURI = rootUrlStr;
					dbMsg += "dataURI=" + dataURI;
					webIntent.putExtra("dataURI" , dataURI);                        //最初に表示するページのパス
					startActivity(webIntent);
					break;
				case R.id.md_call_web:
//				case R.id.mm_call_web:
					setWebFragument("https://www.yahoo.co.jp/");
//					setWebView();
					break;
				case R.id.md_prefarence:      //設定
				case R.id.mm_prefarence:      //設定
					nowView = R.xml.preferences; 					//表示中のview
					Intent settingsIntent = new Intent(MainActivity.this , MyPreferencesActivty.class);
					startActivityForResult(settingsIntent , REQUEST_PREF);//		StartActivity(intent);
					break;
				case R.id.md_quit:
				case R.id.mm_quit:
					callQuit();
					break;
				default:
					pendeingMessege();
					break;
			}
//			if (drawer.isDrawerOpen(GravityCompat.START)) {
//				// ドロワーメニューが開いた際に閉じる画像を非表示にする
//				drawer.closeDrawer(GravityCompat.START);
//			} else {
//				drawer.openDrawer(GravityCompat.START);
//				// ドロワーメニューが開いた際に閉じる画像を表示する
//			}
			dbMsg += ">>" + toastStr;
			if ( !toastStr.equals("") ) {
				Toast.makeText(this , toastStr , Toast.LENGTH_SHORT).show();
			}

			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + "で" + er.toString());
		}
		return false;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		readPref();
		setContentView(R.layout.activity_main);
		Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


//		FloatingActionButton fab = ( FloatingActionButton ) findViewById(R.id.fab);
//		fab.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Snackbar.make(view , "Replace with your own action" , Snackbar.LENGTH_LONG).setAction("Action" , null).show();
//			}
//		});

		DrawerLayout drawer = ( DrawerLayout ) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawer , toolbar , R.string.navigation_drawer_open , R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = ( NavigationView ) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

//		content_ll = findViewById(R.id.content_ll);

		//広告表示//////////////////////////////////////////////////
		ad_layout = findViewById(R.id.ad_layout);
		nend_layout = findViewById(R.id.nend_layout);
		AdUnitID = getString (R.string.banner_ad_unit_id);
		MobileAds.initialize(this,getResources ().getString (R.string.banner_ad_unit_id));       //Mobile Ads SDK を初期化
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		setTopFragument();
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
	public void setTopFragument() {
		final String TAG = "setTopFragument";
		String dbMsg = "[MainActivity]" ;/////////////////////////////////////////////////
		try {
			nowView = topView; 					//表示中のview
			QrFragment fragment = new QrFragment();											// Fragmentを作成します
//			Bundle args = new Bundle();															// Fragmentに渡す値はBundleという型でやり取りする
//			args.putString(KEY_NAME, dataURI);													// Key/Pairの形で値をセットする
//			fragment.setArguments(args);														// Fragmentに値をセットする
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();	// Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
			transaction.add(R.id.container, fragment);											// 新しく追加を行うのでaddを使用します
			// 他にも、よく使う操作で、replace removeといったメソッドがあります
			// メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
			transaction.commit();																// 最後にcommitを使用することで変更を反映します
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + "で" + er.toString());
		}
	}

//	private String dataURI = "https://www.yahoo.co.jp/";
	private final static String KEY_NAME = "dataURI";
	public void setWebFragument(String dataURI) {
		final String TAG = "setWebFragument";
		String dbMsg = "[MainActivity]" ;/////////////////////////////////////////////////
		try {
			nowView = R.layout.activity_web2; 					//表示中のview

			WebFragment fragment = new WebFragment();											// Fragmentを作成します
			Bundle args = new Bundle();															// Fragmentに渡す値はBundleという型でやり取りする
			args.putString(KEY_NAME, dataURI);													// Key/Pairの形で値をセットする
			fragment.setArguments(args);														// Fragmentに値をセットする
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();	// Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
			transaction.add(R.id.container, fragment);											// 新しく追加を行うのでaddを使用します
																								// 他にも、よく使う操作で、replace removeといったメソッドがあります
																								// メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
			transaction.commit();																// 最後にcommitを使用することで変更を反映します
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + "で" + er.toString());
		}
	}

	//classが無い小規模viewはレイアウトの呼び込み ///////////////////////////////////
	public void setTopView() {
		final String TAG = "setTopView";
		String dbMsg = "[MainActivity]" ;/////////////////////////////////////////////////
		try {
			nowView = topView; 					//表示中のview

//			content_ll.removeAllViews();
//  			Context context = getApplicationContext();
//			LayoutInflater inflater = LayoutInflater.from(context); // LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			inflater.inflate(R.layout.activity_top, content_ll);
//			nowView = R.layout.activity_top;
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + "で" + er.toString());
		}
	}

	public void setWebView() {
			final String TAG = "setWebView";
			String dbMsg = "[MainActivity]" ;/////////////////////////////////////////////////
			try {
//				content_ll.removeAllViews();
//				Context context = getApplicationContext();
//				LayoutInflater inflater = LayoutInflater.from(context); // LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				View inf =inflater.inflate(R.layout.activity_web2, content_ll);
//				webView = findViewById(R.id.webview);
//				url_et = findViewById(R.id.url_et);
//				url_et.setText(urlStr);
//				webView.setWebViewClient(new WebViewClient());
//				webView.loadUrl(urlStr);
//				nowView = R.layout.activity_web2;
//		//		url_et.setFocusable(false);
//		//		url_et.setFocusableInTouchMode(false);
//
//				url_et.addTextChangedListener(new TextWatcher() {
//					@Override
//					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//						//テキスト変更前
//					}
//
//					@Override
//					public void onTextChanged(CharSequence s, int start, int before, int count) {
//						//テキスト変更中
//					}
//
//					@Override
//					public void afterTextChanged(Editable s) {
//						urlStr = s.toString();
//						webView.loadUrl(urlStr);
//						//テキスト変更後
//					}
//				});
				myLog(TAG , dbMsg);
			} catch (Exception er) {
				myErrorLog(TAG , dbMsg + "で" + er.toString());
			}
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

	public void setNend() {               		//https://github.com/fan-ADN/nendSDK-Android/wiki/%E3%83%90%E3%83%8A%E3%83%BC%E5%9E%8B%E5%BA%83%E5%91%8A_%E5%AE%9F%E8%A3%85%E6%89%8B%E9%A0%86
		int nend_spotID = Integer.parseInt(getString (R.string.nend_spotID));
		nendAdView = new NendAdView(this,nend_spotID, getString (R.string.nend_apiKey));    		// 1 NendAdView をインスタンス化
		nend_layout.addView(nendAdView); 		// 2 NendAdView をレイアウトに追加
		nendAdView.loadAd();     		// 3 広告の取得を開始
	}
	////////////////////////////////////////////////////////////////////////////
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
		int id = item.getItemId();
		funcSelected(item);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {
		final String TAG = "onNavigationItemSelected[initDrawer]";
		String dbMsg = "MenuItem" + menuItem.toString();/////////////////////////////////////////////////
		boolean retBool = false;
		try {
			retBool = funcSelected(menuItem);
			DrawerLayout drawer = ( DrawerLayout ) findViewById(R.id.drawer_layout);
			drawer.closeDrawer(GravityCompat.START);
		} catch (Exception e) {
			myLog(TAG , dbMsg + "で" + e.toString());
			return false;
		}
		return retBool;
	}
	///////////////////////////////////////////////////////////////////////////////////
	public void pendeingMessege() {
		String titolStr = "制作中です";
		String mggStr = "最終リリースをお待ちください";
		messageShow(titolStr , mggStr);
	}


	public void messageShow(String titolStr , String mggStr) {
		Util UTIL = new Util();
		UTIL.messageShow(titolStr , mggStr , MainActivity.this);
	}

	public static void myLog(String TAG , String dbMsg) {
		Util UTIL = new Util();
		UTIL.myLog(TAG , dbMsg);
	}

	public static void myErrorLog(String TAG , String dbMsg) {
		Util UTIL = new Util();
		UTIL.myErrorLog(TAG , dbMsg);
	}
}
