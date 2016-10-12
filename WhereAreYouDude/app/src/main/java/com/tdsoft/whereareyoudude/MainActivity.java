package com.tdsoft.whereareyoudude;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.tdsoft.whereareyoudude.adapters.MyRosterAdapter;
import com.tdsoft.whereareyoudude.smack.ConnectionManager;
import com.tdsoft.whereareyoudude.smack.callbacks.OnRosterReceivedListener;
import com.tdsoft.whereareyoudude.utils.PreferenceHandler;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRosterReceivedListener {

    private RecyclerView mFriendList;
    private MyRosterAdapter myRosterAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFriendList = (RecyclerView) findViewById(R.id.friendList);
        mFriendList.setLayoutManager(new LinearLayoutManager(this));
        ConnectionManager.getInstance().getRoster(this);
    }


    public void onLogOutClick(View view) {
        ConnectionManager.getInstance().logout();
        PreferenceHandler.getInstance().logout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public void onRosterReceived(List<RosterEntry> rosters) {
        if (rosters != null) {
            myRosterAdapter = new MyRosterAdapter(rosters);
            mFriendList.setAdapter(myRosterAdapter);
        }
    }

    public void onAddFriend(View view) {
        EditText editText = (EditText) findViewById(R.id.et_jid);
        ConnectionManager.getInstance().sendFriendRequest(editText.getText().toString());
    }
}
