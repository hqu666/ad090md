package com.hijiyama_koubou.ja090dm;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.view.KeyEvent;
import android.view.View;
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

public class QRActivity extends AppCompatActivity {
	private LinearLayout scam_ll;
	private DecoratedBarcodeView qrReaderView;
	private ImageButton release_bt;
	private LinearLayout coment_ll;

	private LinearLayout prevew_ll;
	private Button continue_bt;		//連続読み取り
	private Button acsess_bt;
	private Button rescan_bt;
	private TextView qr_result_tv;
	private ImageView qr_result_iv;
	private Drawable orgDrawable;

	public boolean isContinue = false;
	public ArrayList<String> readCords = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String TAG = "onCreate";
		String dbMsg = "[QRActivity]";
		try {
			setContentView(R.layout.activity_qr);
			scam_ll = ( LinearLayout ) findViewById(R.id.scam_ll);
			qrReaderView = ( DecoratedBarcodeView ) findViewById(R.id.decoratedBarcodeView);
			release_bt = ( ImageButton ) findViewById(R.id.release_bt);
			coment_ll = ( LinearLayout ) findViewById(R.id.coment_ll);

			prevew_ll = ( LinearLayout ) findViewById(R.id.prevew_ll);
			continue_bt = ( Button ) findViewById(R.id.continue_bt);
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
			acsess_bt = ( Button ) findViewById(R.id.acsess_bt);
			rescan_bt = ( Button ) findViewById(R.id.rescan_bt);
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

//			prevew_ll.setVisibility(View.GONE);
			qr_result_iv = ( ImageView ) findViewById(R.id.qr_result_iv);
			orgDrawable = qr_result_iv.getDrawable();
			qr_result_tv = ( TextView ) findViewById(R.id.qr_result_tv);

			qrReaderView = ( DecoratedBarcodeView ) findViewById(R.id.decoratedBarcodeView);
			laterCreate();

			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		final String TAG = "onResume";
		String dbMsg = "[QRA]";
		try {
			qrReaderView.resume();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	@Override
	protected void onPause() {
		final String TAG = "onPause";
		String dbMsg = "[QRActivity]";
		try {
			qrReaderView.pause();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode , int resultCode , Intent data) {
		final String TAG = "onActivityResult";
		String dbMsg = "[QRActivity]";
		dbMsg += "requestCode=" + requestCode + ",resultCode=" + resultCode;
		try {
//			IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//			if(result != null) {
//				String dataURI  = result.getContents();
//				dbMsg = ",dataURI="+dataURI;
//				Intent webIntent = new Intent(this , CS_Web_Activity.class);
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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean onKeyDown(int keyCode , KeyEvent event) {
		final String TAG = "onKeyDown";
		String dbMsg = "[QRActivity]";
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


	public void laterCreate() {
		final String TAG = "laterCreate[QRA]";
		String dbMsg = "";
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
					String dbMsg = "";
					try {
//						Button button = (Button) v;
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
					String dbMsg = "";
					try {
						Button button = ( Button ) v;
						String dataURI = ( String ) button.getText();
						dbMsg = ",dataURI=" + dataURI;
						Intent webIntent = new Intent(QRActivity.this , WebActivity.class);
						webIntent.putExtra("dataURI" , dataURI);
						startActivity(webIntent);
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
			release_bt.setVisibility(View.GONE);
			startCapture();
//			navi_head_sub_tv.setText(rootUrlStr);
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	public void callQuit() {
		final String TAG = "callQuit";
		String dbMsg = "[QRActivity]";
		try {
			Intent intent = new Intent();
			String wStr = "";
			for(String rCrod : readCords) {            //読み取っているコード
				wStr +=  rCrod + ",";
			}


			intent.putExtra("readCords", wStr);
			setResult(RESULT_OK, intent);
			this.finish();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	public void reStart() {
		final String TAG = "reStart[MA}";
		String dbMsg = "";
		try {
			Intent intent = new Intent();
			intent.setClass(this , this.getClass());
			this.startActivity(intent);
			this.finish();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	private void startCapture() {
		final String TAG = "startCapture";                        //
		String dbMsg = "[QRActivity]";
		try {
//			DecoratedBarcodeView qrReaderView = findViewById(R.id.decoratedBarcodeView);
//			new IntentIntegrator(QRActivity.this).initiateScan();

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

//				@Override
//				public void possibleResultPoints(List< ResultPoint > list) {
//					final String TAG = "possibleResultPoints";
//					String dbMsg = "[QRActivity]";
//					try {
////						dbMsg += "list = " + list.size() + "件";
////						for (Object  rStr: list){
////							dbMsg += "\n = " + rStr;
////						}
////						myLog(TAG , dbMsg);
//					} catch (Exception er) {
//						myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
//					}
//				}
//			});


			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}


	///////////////////////////////////////////////////////////////////////////////////
	public void messageShow(String titolStr , String mggStr) {
		Util UTIL = new Util();
		UTIL.messageShow(titolStr , mggStr , QRActivity.this);
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


