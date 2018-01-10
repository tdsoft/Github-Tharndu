package com.android.tdsoft.syncadaptertest;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAccount = CreateSyncAccount(this);
    }

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.android.tdsoft.syncadaptertest";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;
    private static final String PREF_SETUP_COMPLETE = "setup_complete";

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        boolean isNewAccount = false;
        boolean setupComplete = PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(PREF_SETUP_COMPLETE, false);
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {

            isNewAccount = true;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */

            System.out.println("erro");
        }
        if (isNewAccount  || !setupComplete) {
           // TriggerRefresh(newAccount);
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_SETUP_COMPLETE, true).commit();
        }
        // Inform the system that this account supports sync
        ContentResolver.setIsSyncable(newAccount, AUTHORITY, 1);
        // Inform the system that this account is eligible for auto sync when the network is up
        ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);
        // Recommend a schedule for automatic synchronization. The system may modify this based
        // on other scheduled syncs and network utilization.
        ContentResolver.addPeriodicSync(newAccount, AUTHORITY, new Bundle(),30);

        return newAccount;
    }

    public static void TriggerRefresh(Account newAccount) {
        Bundle b = new Bundle();
        // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.addPeriodicSync(
                newAccount,      // Sync account
                AUTHORITY, // Content authority
                new Bundle(),30);                                      // Extras
    }

    public void onClick(View view) {
        TriggerRefresh(mAccount);
    }
}
