package com.hijiyama_koubou.ja090dm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.ListAdapter;

import java.util.Map;


public class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
	private Util UTIL;
	private OnFragmentInteractionListener mListener;

	public PreferenceScreen sps;
	public static final String DEFAULT = "未設定";
	public Context context;
	public static SharedPreferences sharedPref;
	public SharedPreferences.Editor myEditor;

//	public PreferenceScreen transition_setting_key;        //接続設定
//	public EditTextPreference transition_type_key;
	public ListPreference transition_type_list_key;        //遷移方法
//	public PreferenceScreen conection_setting_key;        //接続設定
//	public EditTextPreference rootUrl_key;
	public ListPreference rootUrl_list_key;

	public PreferenceScreen trace_setting_key;        //トレース設定

	public PreferenceScreen other_setting_key;        //その他の設定
	public EditTextPreference save_path_key;        //作成したファイルの保存場所
	public CheckBoxPreference lotet_cansel_key;        //自動回転阻止

	public String transitionType = "";        //遷移方法
	public String rootUrlStr = "";
	public String testUrlStr = "";

	public String savePatht = "";        //作成したファイルの保存場所
	public String lotet_canselt = "true";        //自動回転阻止
	public boolean isLotetCanselt = true;        //自動回転阻止

	public String transition_setting_sum_str;        //遷移方法
	public String conection_setting_sum_str;         //接続設定
	public String other_setting_sum_str;        //その他の設定


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String TAG = "onCreate[MPF]";
		String dbMsg = "[MyPreferenceFragment]";
		try {
			MyPreferenceFragment.this.addPreferencesFromResource(R.xml.preferences);
			sps = this.getPreferenceScreen();            //☆PreferenceFragmentなら必要  .
			dbMsg += ",sps=" + sps;

			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + "でエラー発生；" + er);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		final String TAG = "onAttach";
		String dbMsg = "[MyPreferenceFragment]";
		try {
			dbMsg += ",context;" + context.getPackageName();        // keys.size()
			this.context = context;			//his.getActivity().getApplicationContext();    //( MainActivity ) context;
			transitionType =  getString(R.string.transition_fragment);        //遷移方法
			rootUrlStr =  getString(R.string.rootUrlStr);
			readPref(context);
			myLog(TAG , dbMsg);
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
		} catch (Exception er) {
			myLog(TAG , dbMsg + "で" + er.toString());
		}
	}

	/**
	 * 初期表示
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final String TAG = "onActivityCreated";
		String dbMsg = "[MyPreferenceFragment]";
		try {
			sps = MyPreferenceFragment.this.getPreferenceScreen();            //☆PreferenceFragmentなら必要  .
			dbMsg += ",sps=" + sps;
			String summaryStr = "";
//			transition_setting_key = ( PreferenceScreen ) sps.findPreference("transition_setting_key");        //遷移方法
			transition_type_list_key = ( ListPreference ) sps.findPreference("transition_type_list_key");        //遷移方法
			dbMsg += ",transition_type_list_key=" + transition_type_list_key;
			if ( findPreference("transition_type_list_key") != null ) {
				transition_type_list_key.setSummary(transitionType);
			}
//			transition_setting_key = ( PreferenceScreen ) sps.findPreference("transition_setting_key");        //遷移方法

//			rootUrl_key = ( EditTextPreference ) sps.findPreference("rootUrl_key");
//			dbMsg += ",rootUrlStr=" + rootUrlStr;
//			if ( findPreference("rootUrl_key") != null ) {
//				rootUrl_key.setDefaultValue(rootUrlStr);
//				rootUrl_key.setSummary(rootUrlStr);
//			} else {
//				(( EditTextPreference ) findPreference("rootUrl_key")).setText(rootUrlStr);
//			}

			rootUrl_list_key = ( ListPreference ) sps.findPreference("rootUrl_list_key");
//			dbMsg += ",testUrlStr=" + testUrlStr;
			if ( findPreference("rootUrl_list_key") != null ) {
				rootUrl_list_key.setSummary(rootUrlStr);
 //				testUrlArray = getResources().obtainTypedArray(R.array.url_array);
////				testUrlArray.
//				String[] urls = getResources().getStringArray(R.array.url_array);

//				video_audio_source_key.setValue(audioSourceName);
//				summaryStr += "," + getResources().getString(R.string.audio_source) + ":" + audioSourceName;
			}
//			conection_setting_key = ( PreferenceScreen ) sps.findPreference("conection_setting_key");

//			isAutoFlash_key = ( CheckBoxPreference ) sps.findPreference("isAutoFlash_key");        //サブカメラに切り替え
//			dbMsg += ",オートフラッシュ=" + isAutoFlash;
//			if ( findPreference("isAutoFlash_key") != null ) {
//				isAutoFlash_key.setChecked(isAutoFlash);
////				if(isAutoFlash){
////					summaryStr +=","+ getResources().getString(R.string.mm_phot_flash) ;
////				}
//			}
			setAllSummary();

//			reloadSummary();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
		}
	}

	/**
	 * 保存時の処理
	 * http://libro.tuyano.com/index3?id=306001&page=4
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		final String TAG = "onSaveInstanceState";
		String dbMsg = "[MyPreferenceFragment]";
		try {
			myLog(TAG , dbMsg);
		} catch (Exception e) {
			Log.e(TAG , dbMsg + "で" + e.toString());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		final String TAG = "onResume]";
		String dbMsg = "[MyPreferenceFragment]";
		try {
			sharedPref.registerOnSharedPreferenceChangeListener(this);   //Attempt to invoke virtual method 'android.content.SharedPreferences android.preference.PreferenceScreen.getSharedPreferences()
//			setAllSummary();

//			reloadSummary();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + "でエラー発生；" + er);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		final String TAG = "onPause";
		String dbMsg = "[MyPreferenceFragment]";
		try {
			sharedPref.unregisterOnSharedPreferenceChangeListener(this);
			myLog(TAG , dbMsg);
		} catch (Exception e) {
			Log.e(TAG , dbMsg + "で" + e.toString());
			//      myLog(TAG, dbMsg + "で" + e.toString(), "e");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		final String TAG = "onDestroy";
		String dbMsg = "[MyPreferenceFragment]";
		try {
			myLog(TAG , dbMsg);
		} catch (Exception e) {
			Log.e(TAG , dbMsg + "で" + e.toString());
			//      myLog(TAG, dbMsg + "で" + e.toString(), "e");
		}
	}                                                            //切替時⑥

	/**
	 * 変更された項目の更新
	 * 2階層目以降の連携更新もここで行う
	 * @param key 変更された項目のkey
	 *            照合の為に文字列定数を使う
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences , String key) {
		final String TAG = "onSharedPrefChd";
		String dbMsg = "[MyPreferenceFragment]";
		try {
//			dbMsg += "sharedPreferences=" + sharedPreferences.getAll();
			dbMsg += "変更されたのは " + key;
			dbMsg += ":" + sharedPreferences.getString(key , "");
			if ( key.equals("transition_type_list_key") ) {
				ListPreference list_preference = (ListPreference)getPreferenceScreen().findPreference(key);
				String var = list_preference.getValue();
				dbMsg += ">>var = " + var;
				transitionType  =transition_type_list_key.getValue()+"";
				dbMsg += ">transitionTypeを　" + transitionType;
				myEditor.putString(key , transitionType );

				transition_type_list_key.setSummary(transitionType);
//				PreferenceScreen screenPref = (PreferenceScreen)findPreference("transition_setting_key");
//				screenPref.setSummary(transitionType);
//				transition_setting_key.setSummary(list_preference.getValue());

//				transition_setting_key.setSummary(transitionType);
//			} else if ( key.equals("rootUrl_key")) {
//				rootUrlStr = rootUrl_key.getText();
//				dbMsg += " = " + rootUrlStr;
//				myEditor.putString(key , rootUrlStr);
			}else if ( key.equals("rootUrl_list_key") ) {
				rootUrlStr  =rootUrl_list_key.getValue()+"";
				dbMsg += " = " + rootUrlStr;
				myEditor.putString(key , rootUrlStr );
//				myEditor.putString("rootUrl_key" , rootUrlStr);
			}
			dbMsg += ",更新";
			myEditor.commit();
			dbMsg += "完了";
			setAllSummary();
			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + "でエラー発生；" + er);
		}
	}

	//各項目のプリファレンス上の設定///////////////////////////////////////////////////////////////////////////
	/**
	 * サマリーの更新
	 */
	public void setAllSummary() {
		final String TAG = "setAllSummary";
		String dbMsg = "[MyPreferenceFragment]";
		try {
			dbMsg += ",transitionType=" + transitionType ;
			if( ! transitionType.equals("") ){
////				transition_setting_sum_str += getString(R.string.pref_transition_titol) + "=" +transitionType;
//				transition_setting_key.setSummary(transitionType);
//				transition_type_list_key.setSummary(transitionType);
////			}else{
////				transitionType =  getString(R.string.transition_fragment);        //遷移方法
////				dbMsg += ">>" + transitionType ;
			}

//			conection_setting_sum_str ="";         //接続設定
			dbMsg += ",rootUrlStr=" + rootUrlStr;
			if( ! rootUrlStr.equals("") ){
//				conection_setting_sum_str += getString(R.string.partner_titol)+"= " + rootUrlStr;

//				conection_setting_key.setSummary(rootUrlStr);
				rootUrl_list_key.setSummary(rootUrlStr);
//				rootUrl_key.setSummary(rootUrlStr);

//				conection_setting_key.setDefaultValue(rootUrlStr);
//				rootUrl_list_key.setDefaultValue(rootUrlStr);
//				rootUrl_key.setDefaultValue(rootUrlStr);
			}else{
				rootUrlStr =  getString(R.string.rootUrlStr);
			}


//			other_setting_sum_str ="";        //その他の設定
//			dbMsg += ",作成したファイルの保存場所=" + savePatht;
//				other_setting_sum_str += getString(R.string.trace_setting_is_pad_right)+";" + savePatht;
//			}
//			dbMsg += ",自動回転阻止=" + isLotetCanselt;
//			if( isLotetCanselt ){
//				other_setting_sum_str +="\n" +  getString(R.string.other_setting_lotet_cansel);
//			}
//			other_setting_key.setSummary(""+other_setting_sum_str);

			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + "でエラー発生；" + er);
		}
	}

	/**
	 * サマリーの更新
	 * https://qiita.com/noppefoxwolf/items/18e785f4d760f7cc4314
	 * 一階層目しか反映されていない
	 */
	private void reloadSummary() {
		String TAG = "reloadSummary";
		String dbMsg = "[MyPreferenceFragment]";
		try {
			sps = this.getPreferenceScreen();            //☆PreferenceFragmentなら必要  .
			ListAdapter adapter = sps.getRootAdapter();      //            var adapter = this.preferenceScreen.RootAdapter;
//			for ( int i = 0 ; i < adapter.getCount() ; i++ ) {               //ListとEdit連携用のインデックス取得
//				Object item = adapter.getItem(i);
//				if ( item instanceof EditTextPreference ) {
//					EditTextPreference pref = ( EditTextPreference ) item;
//					String key = pref.getKey();
////					if ( key.equals("waiting_scond_key") ) {
////						waiting_scond_index = i;
////					}
//				} else if ( item instanceof ListPreference ) {
//					ListPreference pref = ( ListPreference ) item;
//					pref.setSummary(pref.getEntry() == null ? "" : pref.getEntry());
//
//					String key = pref.getKey();
////					if ( key.equals("waiting_scond_list") ) {
////						waiting_scond_list_index = i;
////					}
//				}
//			}
//			dbMsg += "waiting_scond_index=" + waiting_scond_index + ",_list_index=" + waiting_scond_list_index;
			sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
			myEditor = sharedPref.edit();

			for ( int i = 0 ; i < adapter.getCount() ; i++ ) {               //ListとEdit連携用のインデックス取得
				dbMsg += "\n" + i + ")";
				Object item = adapter.getItem(i);
				dbMsg += item;

				if ( item instanceof EditTextPreference ) {
					dbMsg += "EditTextPreference;";
					EditTextPreference pref = ( EditTextPreference ) item;
					String key = pref.getKey();
					String val = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(key , "");
					dbMsg += ";" + key + ";" + val;
//					if ( key.equals(transition_type_key) ) {
//						transitionType = val;
//						dbMsg += ",transitionType=" + transitionType;
//					}else
//					if ( key.equals(rootUrl_key) ) {
//						rootUrlStr = val;
//						dbMsg += ",rootUrlStr=" + val;
//						dbMsg += "," + getResources().getString(R.string.rootUrl) + "=" + rootUrlStr;
//					}
					pref.setSummary(val);
				} else if ( item instanceof CheckBoxPreference ) {
					dbMsg += "CheckBoxPreference;";
					CheckBoxPreference pref = ( CheckBoxPreference ) item;
					String key = pref.getKey();
					boolean pVal = pref.isChecked();
					dbMsg += ";" + key + ";" + pVal;
//					if ( key.equals("isTexturView_key") ) {
//						pref.setSummaryOn(getResources().getString(R.string.mm_effect_preview_tv));
//						pref.setSummaryOff(getResources().getString(R.string.mm_effect_preview_sufece));
//					} else {
//						pref.setSummaryOn("現在 On");                        // CheckBox が On の時のサマリーを設定
//						pref.setSummaryOff("現在 Off");                        // CheckBox が Off の時のサマリーを設定
//					}
				} else if ( item instanceof ListPreference ) {
					dbMsg += "ListPreference;";
					ListPreference pref = ( ListPreference ) item;
					String key = pref.getKey();
					String pVal = pref.getValue();
					int pIndex = pref.findIndexOfValue(pVal);
					dbMsg += ";" + key + ";" + pIndex + ")" + pVal;
					pref.setSummary(pVal);
//					if ( key.equals("rootUrl_list_key") ) {
//						dbMsg += ",rootUrlStr=" + rootUrlStr;
////						String rStr = setTestURL(pIndex);
////						dbMsg += ">>=" + rStr;
//						if ( rootUrlStr != key ) {
//							myEditor.putString("rootUrl_list_key" , pVal) ;
//							rootUrl_list_key.setSummary(pVal);
//							myEditor.putString("rootUrl_key" , pVal) ;
//							rootUrl_key.setSummary(pVal);
//							;
//							dbMsg += ",更新";
//							myEditor.commit();
//							dbMsg += "完了";
//							//							Object tItem = adapter.getItem(waiting_scond_index);
////							EditTextPreference tPref = ( EditTextPreference ) tItem;
////							tPref.setDefaultValue(pVal);
////							tPref.setText(pVal);
//						}
//					}
				} else if ( item instanceof PreferenceScreen ) {
					dbMsg += "PreferenceScreen;";
					PreferenceScreen pref = ( PreferenceScreen ) item;
					String key = pref.getKey();
					dbMsg += ";key=" + key;
					//     List<String> vals = new ArrayList<String>();
//					List< String > keyList = new ArrayList< String >();
					String wrString = "";
//					if ( key.equals("transition_setting_key") ) {
//						transitionType = sharedPref.getString(key , transitionType);
//						wrString = getResources().getString(R.string.pref_transition_titol) + "=" + transitionType;
//						dbMsg += ",wrString=" + wrString;
//					}else if ( key.equals("conection_setting_key") ) {
//						rootUrlStr = sharedPref.getString(key , rootUrlStr);
//						testUrlStr = sharedPref.getString(key , testUrlStr);
//						wrString = getResources().getString(R.string.rootUrl) + "=" + rootUrlStr + "\n" + getResources().getString(R.string.testUrl) + "=" + testUrlStr;
//						dbMsg += ",wrString=" + wrString;
//					}
					pref.setSummary(wrString);
				} else if ( item instanceof Preference ) {
					dbMsg += "Preference;";
					Preference pref = ( Preference ) item;
					String key = pref.getKey();
					String pVal = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(key , "");
					dbMsg += ";" + key + ";" + pVal;
					dbMsg += ";" + key + ";" + pVal;
					pref.setSummary(pVal);
				}
			}

			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + "でエラー発生；" + er);
		}
	}

	/**
	 * 全項目読み
	 */
	public void readPref(Context context) {
		String TAG = "readPref";
		String dbMsg = "[MyPreferenceFragment]";
		try {
			this.context = context;
			dbMsg += ",context;" + this.context.getPackageName();        // keys.size()
			sharedPref = PreferenceManager.getDefaultSharedPreferences(	this.context);
			myEditor = sharedPref.edit();
			Map< String, ? > keys = sharedPref.getAll();                          //System.collections.Generic.IDictionary<string, object>
			dbMsg += ",読み込み開始;keys=" + keys.size() + "件";        // keys.size()
			if ( UTIL == null ) {
				UTIL = new Util();
			}
			int i = 0;
			for ( String key : keys.keySet() ) {
				i++;
				String rStr = "";
				dbMsg += "\n" + i + "/" + keys.size() + ")" + key;
				if ( key.equals("transition_type_list_key") ) {                                     //接続設定
					transitionType = sharedPref.getString(key , transitionType);
					dbMsg += " = " + transitionType;
//				} else if ( key.equals("rootUrl_key") ) {                                     //接続設定
//					rootUrlStr = sharedPref.getString(key , rootUrlStr);
//					dbMsg += " = " + rootUrlStr;
				} else if ( key.equals("rootUrl_list_key")) {                                     //接続設定
					rootUrlStr = sharedPref.getString(key , rootUrlStr);
					dbMsg += " = " + rootUrlStr;

//				} else if ( key.equals("rootUrl_list_key") ) {
//					testUrlStr = sharedPref.getString(key , rootUrlStr);
//					dbMsg += ",testUrlStr=" + testUrlStr;
//				} else if ( key.equals("save_path_key") ) {
//					savePatht = sharedPref.getString(key , savePatht);       //その他の設定
//					dbMsg += ",作成したファイルの保存場所=" + savePatht;
//				} else if ( key.equals("lotet_cansel_key") ) {
//					isLotetCanselt = sharedPref.getBoolean(key , isLotetCanselt);
//					dbMsg += ",自動回転阻止=" + isLotetCanselt;
				}
			}

//			if ( savePatht.equals("") ) {
//				UTIL = new Util();
//				savePatht = UTIL.getSavePath(context , Environment.DIRECTORY_PICTURES ,  getString(R.string.app_name));   // context.getResources().getString(R.string.app_name);
//				if ( savePatht.equals("") ) {
//					UTIL = new Util();
//					savePatht = UTIL.getSavePath(context , Environment.DIRECTORY_DCIM , getString(R.string.app_name));
//				}
//				dbMsg += ",作成したファイルの保存場所(初期設定)=" + savePatht;
//				myEditor.putString("save_path_key" , savePatht);
//				dbMsg += ",更新";
//				myEditor.commit();
//				dbMsg += "完了";
//			}

			dbMsg += "\n" + ",transitionType=" + transitionType;
			if(transitionType.equals("") ){
				transitionType = getResources().getString(R.string.transition_fragment);
				dbMsg += ">>" + transitionType;
			}

			dbMsg += " ,rootUrlStr=" + rootUrlStr;
			if(rootUrlStr.equals("") ){
				rootUrlStr = getResources().getString(R.string.rootUrlStr);
				dbMsg += ">>" + rootUrlStr;
			}

			myLog(TAG , dbMsg);
		} catch (Exception er) {
			myErrorLog(TAG , dbMsg + "でエラー発生；" + er);
		}
	}                                                                     //プリファレンスの読込み

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}

	public void myLog(String TAG , String dbMsg) {
		if ( UTIL == null ) {
			UTIL = new Util();
		}
		UTIL.myLog(TAG , dbMsg);
	}

	public void myErrorLog(String TAG , String dbMsg) {
		if ( UTIL == null ) {
			UTIL = new Util();
		}
		UTIL.myErrorLog(TAG , dbMsg);
	}

}