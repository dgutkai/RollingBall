package com.real0168.manager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by lanmi on 18/2/9.
 */

public class ToastManager {
    private  static Toast mtoast=null;
    public static  void show(Context context, String str){
        if (context==null) {
            return;

        }
        if (TextUtils.isEmpty(str)){
            str="";
        }
        if(mtoast!=null){
            mtoast.cancel();

        }
        mtoast= Toast.makeText(context,str, Toast.LENGTH_SHORT);
        mtoast.show();
    }

    public static  void show(Context context, int res){
        if (context==null) {
            return;

        }
        if(mtoast!=null){
            mtoast.cancel();

        }
        mtoast= Toast.makeText(context,res, Toast.LENGTH_SHORT);
        mtoast.show();
    }

}
