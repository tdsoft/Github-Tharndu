package com.tdsoft.whereareyoudude.smack.callbacks;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

/**
 * Created by Admin on 7/22/2016.
 */
public interface OnRosterReceivedListener {
    void onRosterReceived(List<RosterEntry> rosters);
}
