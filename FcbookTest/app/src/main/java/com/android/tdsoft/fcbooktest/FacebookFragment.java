package com.android.tdsoft.fcbooktest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FacebookFragment extends Fragment {


    private LoginButton loginButton;
    private CallbackManager callbackManager;

    public FacebookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook, container, false);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        List<String> strings = new ArrayList<>();
        strings.add("user_friends");
        loginButton.setReadPermissions(strings);
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println(loginResult.getAccessToken().getToken());
                loadFriends();
            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println(exception.toString());
            }
        });


        loadFriends();
        return view;
    }

    private void loadFriends(){
        if (AccessToken.getCurrentAccessToken() != null) {
//            new GraphRequest(
//                    AccessToken.getCurrentAccessToken(),
//                    "/me/taggable_friends",
//                    null,
//                    HttpMethod.GET,
//                    new GraphRequest.Callback() {
//                        public void onCompleted(GraphResponse response) {
//                            /* handle the result */
//
//                            try {
//                                JSONObject jsonObject = response.getJSONObject();
//                                JSONArray jsonArray = jsonObject.getJSONArray("data");
//                                for(int i = 0; i < jsonArray.length(); i++){
//                                    System.out.println(jsonArray.getJSONObject(i).toString());
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//            ).executeAsync();
            String appLinkUrl, previewImageUrl;

            appLinkUrl = "https://fb.me/1695790947302646";
            previewImageUrl = "http://www.sp-assurance.com/images/mobile-app-testing-banner.png";

            if (AppInviteDialog.canShow()) {
                AppInviteContent content = new AppInviteContent.Builder()
                        .setApplinkUrl(appLinkUrl)
                        .setPreviewImageUrl(previewImageUrl)
                        .build();
                AppInviteDialog.show(this, content);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
