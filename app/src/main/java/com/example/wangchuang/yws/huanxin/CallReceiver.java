package com.example.wangchuang.yws.huanxin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/8/1.
 */

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //username
        String from = intent.getStringExtra("from");
        //call type
        String type = intent.getStringExtra("type");
        if("video".equals(type)){ //video call

        }else{ //voice call
          /*  context.startActivity(new Intent(context, VoiceCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));*/
        }
    }
}
