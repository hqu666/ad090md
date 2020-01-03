package com.hijiyama_koubou.ja090dm;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class NextFragment  extends Fragment {
    private static final String ARGS_NAME = "name";

    public static NextFragment newInstance(String name) {
        final String TAG = "newInstance";
        String dbMsg = "[NextFragment]";/////////////////////////////////////////////////
        NextFragment fragment = new NextFragment();
        try {
            dbMsg += ",name=" + name;
            Bundle args = new Bundle();
            args.putString(ARGS_NAME, name);
            fragment.setArguments(args);
            myLog(TAG , dbMsg);
        } catch (Exception er) {
            myErrorLog(TAG , dbMsg + "で" + er.toString());
        }
        return fragment;
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
