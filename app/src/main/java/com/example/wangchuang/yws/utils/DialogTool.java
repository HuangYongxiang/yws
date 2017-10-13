package com.example.wangchuang.yws.utils;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.wangchuang.yws.R;


public class DialogTool {

    public static Dialog dialog=null;


    /**
     * 选择打开导航程序弹窗点击事件
     *
     * @author raotao
     */

    public static abstract class OnAlertDialogOptionListener {
        protected void onClickOption(int position) {
        }
    }

    //vurtnewk start

    /**
     * 弹出有两个选项的弹窗 竖方向
     *
     * @param oneOptionTitle 第一个选项标题
     * @param twoOptionTitle 第二个选项标题
     * @param listener
     */
    public static void showAlertDialogOptionTwo(Context context,
                                             String oneOptionTitle, String twoOptionTitle,
                                             final OnAlertDialogOptionListener listener) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.alertdialog1, null);
        TextView onetext=(TextView) view.findViewById(R.id.alertdialog_option_one_text);
        TextView twotext=(TextView) view.findViewById(R.id.alertdialog_option_two_text);
        if(oneOptionTitle.equals("中文报告")){
            onetext .setText("中文报告");
        }else{
            onetext .setText(oneOptionTitle);
        }
        if(twoOptionTitle.equals("英文报告")){
            twotext.setText("中文报告");
        }else{
        twotext.setText(twoOptionTitle);}
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp);
        onetext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClickOption(0);
            }
        });
        twotext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClickOption(1);

            }
        });
        dialog.show();
    }
    public static void showAlertDialogOptionThree(Context context,
                                                String oneOptionTitle, String twoOptionTitle,String threeOptionTitle,
                                                final OnAlertDialogOptionListener listener) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.alertdialog2, null);
        TextView onetext=(TextView) view.findViewById(R.id.alertdialog_option_one_text);
        TextView twotext=(TextView) view.findViewById(R.id.alertdialog_option_two_text);
        TextView threetext=(TextView) view.findViewById(R.id.alertdialog_option_three_text);
        onetext .setText(oneOptionTitle);
        twotext.setText(twoOptionTitle);
        threetext.setText(threeOptionTitle);
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp);
        onetext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClickOption(0);
            }
        });
        twotext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClickOption(1);

            }
        });
        threetext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClickOption(2);

            }
        });
        dialog.show();
    }
    public static void showAlertDialogOptionFour(Context context,
                                                String oneOptionTitle, String twoOptionTitle,
                                                 String threeOptionTitle, String fourOptionTitle,
                                                final OnAlertDialogOptionListener listener) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.alertdialog3, null);
        TextView onetext=(TextView) view.findViewById(R.id.alertdialog_option_one_text);
        TextView twotext=(TextView) view.findViewById(R.id.alertdialog_option_two_text);
        TextView threetext=(TextView) view.findViewById(R.id.alertdialog_option_three_text);
        TextView fourtext=(TextView) view.findViewById(R.id.alertdialog_option_four_text);
        onetext .setText(oneOptionTitle);
        twotext.setText(twoOptionTitle);
        threetext.setText(threeOptionTitle);
        fourtext.setText(fourOptionTitle);
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp);
        onetext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClickOption(0);
            }
        });
        twotext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClickOption(1);

            }
        });
        threetext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClickOption(2);

            }
        });
        fourtext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClickOption(3);

            }
        });
        dialog.show();
    }

    /**
     * 两横两竖菜单
     * TODO
     *
     * @param context
     * @param title
     * @param content
     * @param leftString
     * @param rightString
     */
    public static void showAlertDialogOptionFours(Context context, String title,
                                                  String content, String leftString, String rightString,
                                                  final OnAlertDialogOptionListener listener) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialog_four_option, null);
        ((TextView) view.findViewById(R.id.alertdialog_option_one_title))
                .setText(title);
        ((TextView) view.findViewById(R.id.alertdialog_option_one_content))
                .setText(content);
        TextView alertdialog_option_two_left = (TextView) view
                .findViewById(R.id.alertdialog_option_two_left);
        alertdialog_option_two_left.setText(leftString);

        TextView alertdialog_option_two_right = (TextView) view
                .findViewById(R.id.alertdialog_option_two_right);
        alertdialog_option_two_right.setText(rightString);

        final Dialog dialog = new Dialog(context, R.style.custom_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        alertdialog_option_two_left
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onClickOption(0);
                        }

                    }
                });
        alertdialog_option_two_right
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onClickOption(1);
                        }
                    }
                });
        dialog.show();
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
