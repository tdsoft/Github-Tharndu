package com.android.tdsoft.fcbooktest.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;


import com.android.tdsoft.fcbooktest.MyApplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Admin on 1/4/2016.
 */
public class Utils {
    public static String getHashKey(){
        String hashKey = "";
        try {
            PackageInfo info = MyApplication.getInstance().getPackageManager().getPackageInfo("com.android.tdsoft.fcbooktest", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("Your Tag", hashKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            hashKey = "";
        } catch (NoSuchAlgorithmException e) {
            hashKey = "";
        }
        return hashKey;
    }
}
