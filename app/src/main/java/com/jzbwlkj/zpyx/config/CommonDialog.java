package com.jzbwlkj.zpyx.config;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


/**
 * Created by admin on 2017/3/30.
 */

public class CommonDialog {

    public static void loginDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.setMessage("您还没有登录,请前往登录")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        context.startActivity(new Intent(context, LoginActivity.class));

                    }
                })
                .create();
        alertDialog.show();
    }

}
