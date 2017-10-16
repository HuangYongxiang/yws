package com.example.wangchuang.yws.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.wangchuang.yws.R;
import com.example.wangchuang.yws.fragment.ConversationListFragment;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

public class ConversationActivity extends EaseBaseActivity {
    public static ChatActivity activityInstance;
    private ConversationListFragment chatFragment;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chat);

        chatFragment = new ConversationListFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

       /* chatFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                int type=0;
                if(conversation.isGroup()){
                    type=2;//群组
                }else{
                    type=1;//单聊
                }
                startActivity(new Intent(ConversationActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId())
                        .putExtra(EaseConstant.EXTRA_CHAT_TYPE,type));
            }
        });*/
        chatFragment.refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        chatFragment.getActivity().finish();
    }

}


