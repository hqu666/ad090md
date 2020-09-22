package com.hijiyama_koubou.ja090dm;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AndroidRuntimeException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QrFragment extends Fragment {
	public static SharedPreferences sharedPref;
	private LinearLayout scam_ll;
	private DecoratedBarcodeView qrReaderView;			//コードリーダーview
	private ImageButton release_bt;
	private LinearLayout coment_ll;

	private LinearLayout prevew_ll;
	private ImageView qr_result_iv;
	private TextView qr_result_tv;

	private Button continue_bt;		//連続読み取り
	private Button acsess_bt;
	private Button rescan_bt;
	private Drawable orgDrawable;
	public String transitionType;
	public boolean isContinue = false;
	public ArrayList<String> readCords = new ArrayList<String>();


	public static QrFragment newInstance(int count){
		final String TAG = "QrFragment";
		String dbMsg = "[QrFragment]";
		QrFragment QrFragment = new QrFragment();		// インスタンス生成
		try {
			Bundle args = new Bundle();		// Bundle にパラメータを設定
			args.putInt("Counter", count);
			QrFragment.setArguments(args);
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
		return QrFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String TAG = "onCreate";
		String dbMsg = "[QrFragment]";
		try {
			Bundle extras = getArguments();
			if(extras != null){
				this.transitionType = extras.getString("transitionType");                        //最初に表示するページのパス
			}else{
				this.transitionType =  getString(R.string.transition_fragment);;                        //最初に表示するページのパス
			}
			dbMsg += "transitionType=" + transitionType;

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
			release_bt = ( ImageButton ) view.findViewById(R.id.release_bt);
			release_bt.setVisibility(View.GONE);
			coment_ll = ( LinearLayout ) view.findViewById(R.id.coment_ll);
			dbMsg += ",coment_ll=" + coment_ll;
			prevew_ll = ( LinearLayout ) view.findViewById(R.id.prevew_ll);
			continue_bt = ( Button ) view.findViewById(R.id.continue_bt);
			continue_bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {                    // ボタンがクリックされた時に呼び出されます
					final String TAG = "continue_bt";
					String dbMsg = "[onViewCreated]";
					try {
						isContinue = true;
						dbMsg = "連続読取開始";
						startCapture();
						myLog(TAG , dbMsg);
					} catch (Exception er) {
						myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
					}
				}
			});

			acsess_bt = ( Button ) view.findViewById(R.id.acsess_bt);
			acsess_bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {    // ボタンがクリックされた時に呼び出されます
					final String TAG = "acsess_bt";
					String dbMsg = "[onViewCreated]";
					try {
						Button button = ( Button ) v;
						String dataURI = ( String ) button.getText();
						dbMsg += ",dataURI=" + dataURI;
						sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
						transitionType = sharedPref.getString("transition_type_list_key" , transitionType);      //切替直後の反映
						dbMsg += " ,transitionType=" + transitionType;
						MainActivity MA = new MainActivity();

						if( transitionType.equals(getString(R.string.transition_actvity))) {
//							Intent webIntent = new Intent(getActivity(). , WebActivity.class);       //getContext()が必要			MainActivity.this
//					       //getContext、getActivity、送り先でjava.lang.NullPointerException: Attempt to invoke virtual method 'android.app.ActivityThread$ApplicationThread android.app.ActivityThread.getApplicationThread()' on a null object reference
//							MA.callWebIntent(dataURI , webIntent);
							///callWebIntentのメソッドをここで実行/////////////////////////////////
							MA.nowView = R.layout.activity_web; 					//表示中のview
							Intent webIntent = new Intent(getContext() , WebActivity.class);		//getContext()が必要			MainActivity.this
							webIntent.putExtra("dataURI" , dataURI);                        //最初に表示するページのパス
							startActivity(webIntent);
						}else if( transitionType.equals(getString(R.string.transition_fragment))) {
							FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();	// Fragumenntからの呼出しにはgetActivity が必要
							MA.setWebFragument(dataURI , transaction);
						}
						myLog(TAG , dbMsg);
					} catch (Exception er) {
						myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
					}
				}
			});

			rescan_bt = ( Button ) view.findViewById(R.id.rescan_bt);
			rescan_bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {                    // ボタンがクリックされた時に呼び出されます
					final String TAG = "rescan_bt";
					String dbMsg = "[onViewCreated]";
					try {
						isContinue = false;
						startCapture();
						myLog(TAG , dbMsg);
					} catch (Exception er) {
						myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
					}
				}
			});

			qr_result_iv = ( ImageView ) view.findViewById(R.id.qr_result_iv);
			orgDrawable = qr_result_iv.getDrawable();
			qr_result_tv = ( TextView ) view.findViewById(R.id.qr_result_tv);

			qrReaderView = ( DecoratedBarcodeView ) view.findViewById(R.id.decoratedBarcodeView);
			dbMsg += ",qrReaderView=" + qrReaderView;

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
		String dbMsg = "[QrFragment]" ;//acttyからメニューを表示したら発生する
//		Fragment fragment = (getFragmentManager().findFragmentById(R.id.container));
//		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//		ft.remove(fragment);
//		ft.commit();
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

	// onActivityResult,onKeyDown(KeyEvent)はFragmentにはない

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

			//onActivityResult で結果を受け取るパターン  /////////////////////////////////////////////////////////////////
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

	private void startCapture() {
		final String TAG = "startCapture";                        //
		String dbMsg = "[QrFragment]";
		try {
//			new IntentIntegrator(QrFragment.this.getActivity()).initiateScan();

//			prevew_ll.setVisibility(View.GONE);
			coment_ll.setVisibility(View.VISIBLE);
			qr_result_tv.setVisibility(View.VISIBLE);
			qr_result_iv.setImageDrawable(orgDrawable);
			qrReaderView.decodeSingle(new BarcodeCallback() {
				@Override
				public void barcodeResult(BarcodeResult barcodeResult) {
					final String TAG = "barcodeResult";
					String dbMsg = "[QrFragment]QRコード読み取り";

					//	readCords
					try {
						String dataURI = barcodeResult.getText();	//よくあある用途でURLとして読み取る
						dbMsg += ",dataURI=" + dataURI;
						boolean isAdd = true;
						for(String rCrod : readCords) {			//読み取っているコードの中に
							if(rCrod.equals(dataURI)){			//同じコードが有れば
								isAdd = false;					//処理中断
								break;
							}
						}
						if(isAdd){								//重複がなければ
							readCords.add(dataURI);				//追加
							dbMsg += ">>追加" + readCords.size() + "件目";
						}else{
							dbMsg += ">>重複";
						}
						//	coment_ll.setVisibility(View.VISIBLE);
						if(isContinue){
							qr_result_iv.setVisibility(View.GONE);
							rescan_bt.setVisibility(View.GONE);
							if(isAdd){								//追加が有れば
								String wStr = "";
								int wCount = 0;
								for(String rCrod : readCords) {            //読み取っているコード
									wCount++;
									wStr += "(" + wCount + ")" + rCrod + "\n";
								}
								dbMsg += " ,wStr=" + wStr;
								qr_result_tv.setText(wStr);
							}
							dbMsg += ">>再読み込み";
							startCapture();
						}else{
							qr_result_iv.setVisibility(View.VISIBLE);
							rescan_bt.setVisibility(View.VISIBLE);
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
								qr_result_tv.setText("読み取ったコードは　" + dataURI);
							}
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
						}
						myLog(TAG , dbMsg);
					} catch (Exception er) {
						myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);

					}

				}

				@Override
				public void possibleResultPoints(List< ResultPoint > list) {
					final String TAG = "possibleResultPoints";
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
 * QRコードの読取、生成をする [Android]         https://qiita.com/hoshiume11/items/0f496fe80df84875c132
 **/


