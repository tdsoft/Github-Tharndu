package com.android.tdsoft.animationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.RadioGroup;

public class AnimateList extends AppCompatActivity implements AnimAdapter.OnItemClickListener{

    AnimAdapter animAdapter;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_list);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(500);
        itemAnimator.setRemoveDuration(500);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        animAdapter = new AnimAdapter();
        animAdapter.setOnItemClickListener(this);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(animAdapter);

        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(recyclerView);
    }

    // Extend the Callback class
    ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
        //and in your imlpementaion of
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // get the viewHolder's and target's positions in your adapter data, swap them
            animAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            // and notify the adapter that its dataset has changed
            animAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //TODO
        }

        //defines the enabled move directions in each state (idle, swiping, dragging).
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
        }
    };


    @Override
    public void onItemClick(int adapterPosition) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rdo_add:
                animAdapter.addItem(adapterPosition);
                break;
            case R.id.rdo_del:
                animAdapter.deleteItem(adapterPosition);
                break;
            case R.id.rdo_change:
                animAdapter.changeItem(adapterPosition);
                break;
        }

    }

}
