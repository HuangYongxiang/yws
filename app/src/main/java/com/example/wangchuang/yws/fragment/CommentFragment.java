package com.example.wangchuang.yws.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.activity.GoodsDetailActivity;
import com.example.wangchuang.yws.bean.UserInfo;
import com.example.wangchuang.yws.utils.KeyBoardUtil;
import com.example.wangchuang.yws.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 回复框
 */

public class CommentFragment extends DialogFragment implements View.OnClickListener {

    public static final String COMMENT_BEAN = "comment_bean";

    private EditText mEditText;
    private UserInfo userInfo;
    private TextView mSendBtn;
    private View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_comment, null);
        initViews(mRootView);
        mRootView.setOnClickListener(this);
        initData();
        return mRootView;
    }

    private void initViews(View view) {
        mEditText = (EditText) view.findViewById(R.id.comment_edit_text);
        mSendBtn = (TextView) view.findViewById(R.id.send_btn);
        mSendBtn.setOnClickListener(this);
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();
        delayDoEdit();
    }

    private void initData() {
        userInfo = getArguments().getParcelable(COMMENT_BEAN);
        if (userInfo == null) {
            mEditText.setHint("回复");
        } else {
            String name = userInfo.getUser_name();
            mEditText.setHint(getString(R.string.talk_reply_hint, name));
        }
    }

    private void delayDoEdit() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (CommentFragment.this.isVisible()) {
                    KeyBoardUtil.showKeyboard(getActivity(), mEditText);
                }
            }
        }, 500);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onClick(View v) {
        if (v == mSendBtn) {
            if (TextUtils.isEmpty(mEditText.getText().toString())) {
                ToastUtil.show(getActivity(), R.string.comment_null_message);
                return;
            }
            if (getActivity() instanceof GoodsDetailActivity) {
                ((GoodsDetailActivity) getActivity()).sendCommend(userInfo
                        , mEditText.getText().toString());
            }
            mEditText.setText("");
            KeyBoardUtil.hideKeyboard(getActivity(), mEditText);
            dismiss();
        } else if (v == mRootView) {
            mEditText.setText("");
            KeyBoardUtil.hideKeyboard(getActivity(), mEditText);
            dismiss();
        }
    }
}

