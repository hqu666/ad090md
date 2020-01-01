package com.hijiyama_koubou.ja090dm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class TopFragment  extends Fragment {

    private TextView top_view_titol;
    private TextView top_view_capsion;
//    private TextView top_view_result_tv;
//    private Button qr_act_bt;
//    private Button web_act_bt;

    public String transitionType;

    public static TopFragment newInstance(int count){
        final String TAG = "TopFragment";
        String dbMsg = "[TopFragment]";
        TopFragment TopFragment = new TopFragment();		// インスタンス生成
        try {
            Bundle args = new Bundle();		// Bundle にパラメータを設定
            args.putInt("Counter", count);
            TopFragment.setArguments(args);
            myLog(TAG , dbMsg);
        } catch (Exception er) {
            myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
        }
        return TopFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String TAG = "onCreate";
        String dbMsg = "[TopFragment]";
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
        String dbMsg = "[TopFragment]";
        try {
            myLog(TAG , dbMsg);
        } catch (Exception er) {
            myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
        }		// 先ほどのレイアウトをここでViewとして作成します
        return inflater.inflate(R.layout.activity_top, container, false);
    }

    /**
     * Viewが生成し終わった時に呼ばれるメソッド
     * */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String TAG = "onViewCreated";
        String dbMsg = "[TopFragment]";
        try {
            top_view_titol = (TextView) view.findViewById(R.id.top_view_titol);
            top_view_titol.setText(getString(R.string.menu_top_view));
            top_view_capsion = (TextView) view.findViewById(R.id.top_view_capsion);
            top_view_capsion.setText(getString(R.string.capsion_top_view));

//            top_view_result_tv = (TextView) view.findViewById(R.id.top_view_result_tv);

            myLog(TAG , dbMsg);
        } catch (Exception er) {
            myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        final String TAG = "onAttach";
        String dbMsg = "[TopFragment]" ;/////////////////////////////////////////////////
        myLog(TAG , dbMsg);
    }

    /***
     * Fragmentの内部のViewリソースの整理を行う
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final String TAG = "onDestroyView";
        String dbMsg = "[TopFragment]" ;//acttyからメニューを表示したら発生する
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
        String dbMsg = "[TopFragment]";
        try {
            myLog(TAG , dbMsg);
        } catch (Exception er) {
            myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
        }
    }

    @Override
    public void onPause() {
        final String TAG = "onPause";
        String dbMsg = "[TopFragment]";
        try {
            myLog(TAG , dbMsg);
        } catch (Exception er) {
            myErrorLog(TAG , dbMsg + ";でエラー発生；" + er);
        }
        super.onPause();
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
