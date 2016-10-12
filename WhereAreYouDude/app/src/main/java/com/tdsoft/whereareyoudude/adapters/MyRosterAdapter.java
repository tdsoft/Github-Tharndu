package com.tdsoft.whereareyoudude.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tdsoft.whereareyoudude.R;
import com.tdsoft.whereareyoudude.smack.ConnectionManager;

import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;

import java.util.List;

/**
 * Created by Admin on 7/22/2016.
 */
public class MyRosterAdapter extends RecyclerView.Adapter<MyRosterAdapter.MyHolder> {
    private final List<RosterEntry> rosterEntries;

    public MyRosterAdapter(List<RosterEntry> rosterEntries) {
        this.rosterEntries = rosterEntries;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_roster, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        RosterEntry rosterEntry = rosterEntries.get(position);
        holder.txtName.setText(rosterEntry.getName() != null ? rosterEntry.getName() : "No Name");
        holder.txtJid.setText(rosterEntry.getJid());
        switch (rosterEntry.getType()) {
            case both:
                holder.txtFriendRequest.setText("Friend");
                break;
            case to:
                holder.txtFriendRequest.setText("Pending request");
                break;
            case from:
                holder.txtFriendRequest.setText("Accept Request");
                break;
            case none:
                holder.txtFriendRequest.setText("None");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return rosterEntries.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtName, txtJid, txtFriendRequest;

        public MyHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtJid = (TextView) itemView.findViewById(R.id.txt_jid);
            txtFriendRequest = (TextView) itemView.findViewById(R.id.txt_friend_request);
            txtFriendRequest.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ConnectionManager.getInstance().acceptFriendRequest(rosterEntries.get(getAdapterPosition()).getJid());
        }
    }
}
