package com.hijiyama_koubou.ja090dm;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.HashMap;
import java.util.List;

public class QrFragment extends Fragment {
	private LinearLayout scam_ll;
	private DecoratedBarcodeView qrReaderView;
	private ImageButton release_bt;
	private LinearLayout coment_ll;

	private LinearLayout prevew_ll;
	private ImageView qr_result_iv;
	private TextView qr_result_tv;

	private Button acsess_bt;
	private Button rescan_bt;

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String TAG = "onCreate";
		String dbMsg = "[QrFragment]";
		try {

			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	/**
	 * Fragmentで表示するViewを作成するメソッド
	 * */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		final String TAG = "onCreateView";
		String dbMsg = "[QrFragment]";
		try {
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}		// 先ほどのレイアウトをここでViewとして作成します
		return inflater.inflate(R.layout.activity_qr, container, false);
	}

	/**
	 * Viewが生成し終わった時に呼ばれるメソッド
	 * */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final String TAG = "onViewCreated";
		String dbMsg = "[QrFragment]";
		try {
			scam_ll = ( LinearLayout ) view.findViewById(R.id.scam_ll);
			dbMsg += ",scam_ll=" + scam_ll;
			qrReaderView = ( DecoratedBarcodeView ) view.findViewById(R.id.decoratedBarcodeView);
			dbMsg += ",qrReaderView=" + qrReaderView;
			release_bt = ( ImageButton ) view.findViewById(R.id.release_bt);
			release_bt.setVisibility(View.GONE);
//			dbMsg += ",release_bt=" + release_bt;
			coment_ll = ( LinearLayout ) view.findViewById(R.id.coment_ll);
			dbMsg += ",coment_ll=" + coment_ll;
			prevew_ll = ( LinearLayout ) view.findViewById(R.id.prevew_ll);
			acsess_bt = ( Button ) view.findViewById(R.id.acsess_bt);
			rescan_bt = ( Button ) view.findViewById(R.id.rescan_bt);
//			prevew_ll.setVisibility(View.GONE);
			qr_result_iv = ( ImageView ) view.findViewById(R.id.qr_result_iv);
			qr_result_tv = ( TextView ) view.findViewById(R.id.qr_result_tv);

			laterCreate();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		final String TAG = "onAttach";
		String dbMsg = "[QrFragment]" ;/////////////////////////////////////////////////
		myLog(TAG , dbMsg);
	}

	/***
	 * Fragmentの内部のViewリソースの整理を行う
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		final String TAG = "onDestroyView";
		String dbMsg = "[QrFragment]" ;/////////////////////////////////////////////////
		myLog(TAG , dbMsg);
	}

	@Override
	public void onResume() {
		super.onResume();
		final String TAG = "onResume";
		String dbMsg = "[QrFragment]";
		try {
			qrReaderView.resume();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	@Override
	public void onPause() {
		final String TAG = "onPause";
		String dbMsg = "[QrFragment]";
		try {
			qrReaderView.pause();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
		super.onPause();
	}

////	@Override
//	public void onActivityResult(int requestCode , int resultCode , Intent data) {         // Fragmentにはない
//		final String TAG = "onActivityResult";
//		String dbMsg = "[QrFragment]";
//		dbMsg += "requestCode=" + requestCode + ",resultCode=" + resultCode;                                      //requestCode=49374,resultCode=0
//		try {
//			IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//			if(result != null) {
//				String dataURI  = result.getContents();
//				dbMsg = ",dataURI="+dataURI;
//				Intent webIntent = new Intent(getContext() , Web_Activity.class);
//				webIntent.putExtra("dataURI" , dataURI);
//				startActivity(webIntent);
//			} else {
//				super.onActivityResult(requestCode, resultCode, data);
//			}
//
//			myLog(TAG , dbMsg);
//		} catch (Exception er) {
//			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
//		}
//	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
////	@Override
//	public boolean onKeyDown(int keyCode , KeyEvent event) {
//		final String TAG = "onKeyDown[QRA]";
//		String dbMsg = "[QrFragment]";
//		try {
//			dbMsg = "keyCode=" + keyCode;//+",getDisplayLabel="+String.valueOf(MyEvent.getDisplayLabel())+",getAction="+MyEvent.getAction();////////////////////////////////
//			myLog(TAG , dbMsg);
//			switch ( keyCode ) {    //キーにデフォルト以外の動作を与えるもののみを記述★KEYCODE_MENUをここに書くとメニュー表示されない
//				case KeyEvent.KEYCODE_HOME:            //3
//				case KeyEvent.KEYCODE_BACK:            //4KEYCODE_BACK :keyCode；09SH: keyCode；4,MyEvent=KeyEvent{action=0 code=4 repeat=0 meta=0 scancode=158 mFlags=72}
//					callQuit();
//					return true;
//				default:
//					return false;
//			}
//		} catch (Exception er) {
//			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
//			return false;
//		}
//	}

	public void laterCreate() {
		final String TAG = "laterCreate";
		String dbMsg = "[QrFragment]";
		try {
			int orientation = getResources().getConfiguration().orientation;
			dbMsg += "orientation=" + orientation;
			if ( orientation == Configuration.ORIENTATION_LANDSCAPE ) {
				dbMsg += "=横向き";
			} else if ( orientation == Configuration.ORIENTATION_PORTRAIT ) {
				dbMsg += "=縦向き";
			}

			release_bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {                    // ボタンがクリックされた時に呼び出されます
					final String TAG = "release_bt[MA]";
					String dbMsg = "[QrFragment]";
					try {
						startCapture();
						myLog(TAG , dbMsg);
					} catch (Exception er) {
						myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
					}
				}
			});

			acsess_bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {    // ボタンがクリックされた時に呼び出されます
					final String TAG = "acsess_bt[MA]";
					String dbMsg = "[QrFragment]";
					try {
						Button button = ( Button ) v;
						String dataURI = ( String ) button.getText();
						dbMsg += ",dataURI=" + dataURI;

						MainActivity MA = new MainActivity();

						if( MA.transitionType == MA.transitionActivity) {
							MA.callWebIntent(dataURI);
						}else if( MA.transitionType == MA.transitionFragment){
							MA.setWebFragument(dataURI);
						}else if( MA.transitionType == MA.transitionInflater){

						}
						myLog(TAG , dbMsg);
					} catch (Exception er) {
						myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
					}
				}
			});

			rescan_bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {                    // ボタンがクリックされた時に呼び出されます
					final String TAG = "rescan_bt[MA]";
					String dbMsg = "";
					try {
						reStart();    //☆再スキャンできないのでリスタート
//						startCapture();
						myLog(TAG , dbMsg);
					} catch (Exception er) {
						myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
					}
				}
			});

			//onActivityResult で結果を受け取るパターン  /////////////////////////////////////////////////////////////////
//			new IntentIntegrator(getActivity()).initiateScan();   				//これだけでもQRコードアクティビティを形成して、onActivityResultで内容を取得できる
//			DecoratedBarcodeView qrReaderView = findViewById(R.id.decoratedBarcodeView);
			startCapture();
		myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	public void callQuit() {
		final String TAG = "callQuit[MA]";
		String dbMsg = "[QrFragment]";
		try {
//			sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);            //	getActivity().getBaseContext()
//			myEditor = sharedPref.edit();
////			myEditor.putString("peer_id_key" , "");      //使用した
////			boolean kakikomi = myEditor.commit();
//			this.finish();
//			if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
//				finishAndRemoveTask();                      //アプリケーションのタスクを消去する事でデバッガーも停止する。
//			} else {
//				moveTaskToBack(true);                       //ホームボタン相当でアプリケーション全体が中断状態
//			}
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	public void reStart() {
		final String TAG = "reStart[MA}";
		String dbMsg = "[QrFragment]";
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

	private void startCapture() {
		final String TAG = "startCapture]";                        //
		String dbMsg = "[QrFragment]";
		try {
			prevew_ll.setVisibility(View.GONE);
			coment_ll.setVisibility(View.VISIBLE);
			qrReaderView.decodeSingle(new BarcodeCallback() {
				@Override
				public void barcodeResult(BarcodeResult barcodeResult) {
					final String TAG = "barcodeResult";
					String dbMsg = "[QrFragment]QRコードが詠み込めた";
					try {
						String dataURI = barcodeResult.getText();
						dbMsg += ",dataURI=" + dataURI;
						coment_ll.setVisibility(View.VISIBLE);
						try {
							BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
							HashMap hints = new HashMap();

							//文字コードの指定
							hints.put(EncodeHintType.CHARACTER_SET, "shiftjis");

							hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);									//誤り訂正レベルを指定:/L 7% /M 15%が復元可能 /Q 25%が復元可能 /H 30%が復元可能
							int size = qr_result_iv.getHeight();
							dbMsg += " ,size=" + size;
							Bitmap bitmap = barcodeEncoder.encodeBitmap(dataURI, BarcodeFormat.QR_CODE , size, size, hints);	//QRコードをBitmapで作成
							qr_result_iv.setImageBitmap(bitmap);			//作成したQRコードを画面上に配置
						} catch (WriterException e) {
							throw new AndroidRuntimeException("Barcode Error.", e);
						}

						if ( dataURI.startsWith("http") ) {
							qr_result_tv.setVisibility(View.GONE);
							release_bt.setVisibility(View.GONE);
							prevew_ll.setVisibility(View.VISIBLE);
							acsess_bt.setText(dataURI);
						} else {
							acsess_bt.setVisibility(View.GONE);
							qr_result_tv.setVisibility(View.VISIBLE);
							qr_result_tv.setText(dataURI);
						}
						myLog(TAG , dbMsg);
					} catch (Exception er) {
						myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);

					}

				}

				@Override
				public void possibleResultPoints(List< ResultPoint > list) {
					final String TAG = "possibleResultPoints[MA]";
					String dbMsg = "";
					try {
//						dbMsg += "list = " + list.size() + "件";
//						for (Object  rStr: list){
//							dbMsg += "\n = " + rStr;
//						}
//						myLog(TAG , dbMsg);
					} catch (Exception er) {
						myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
					}
				}
			});
//		qrReaderView?.decodeSingle(object : BarcodeCallback {
//			override fun barcodeResult(result: BarcodeResult?) {
//				stopCapture()
//				if (result == null) {
//					// no result
//					Log.w(TAG, "No result")
//					return
//				}
//				Log.i(TAG, "QRCode Result: ${result.text}")
//				val bytes = result.resultMetadata[ResultMetadataType.BYTE_SEGMENTS] as? List<*>
//				val data = bytes?.get(0) as? ByteArray ?: return
//
//																  // print result
//																  val resultString = StringBuffer()
//				data.map { byte ->
//					resultString.append(String.format("0x%02X,", byte))
//				}
//				Log.i(TAG, resultString.toString())
//			}
//			override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) { }
//		})
//		qrReaderView?.resume()

			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}


	///////////////////////////////////////////////////////////////////////////////////
	public void messageShow(String titolStr , String mggStr) {
		Util UTIL = new Util();
		UTIL.messageShow(titolStr , mggStr , getContext());
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


/**
 * AndroidアプリでQRコードの読取、生成をする     https://qiita.com/11Kirby/items/0f496fe80df84875c132
 **/


