package com.eight25media.happydrawing.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.eight25media.happydrawing.R;
import com.eight25media.happydrawing.activities.MainActivity;
import com.eight25media.happydrawing.databinding.FragmentDrawingBinding;
import com.eight25media.happydrawing.views.DrawingView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class DrawingFragment extends Fragment implements View.OnClickListener {

    ImageButton currPaint;

    DrawingView drawView;
    private float smallBrush, mediumBrush, largeBrush;

    private FragmentDrawingBinding fragmentDrawingBinding;
    private View.OnClickListener onPainClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            paintClicked(v);
        }
    };

    public DrawingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentDrawingBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_drawing,container,false);

        View view = fragmentDrawingBinding.getRoot();

        ((MainActivity)getActivity()).setTagDrawingFragment(getTag());


        drawView = fragmentDrawingBinding.drawing;

        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        currPaint = fragmentDrawingBinding.paint1;

        fragmentDrawingBinding.paint1.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint2.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint3.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint4.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint5.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint6.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint7.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint8.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint9.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint10.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint11.setOnClickListener(onPainClick);
        fragmentDrawingBinding.paint12.setOnClickListener(onPainClick);


        fragmentDrawingBinding.drawBtn.setOnClickListener(this);
        fragmentDrawingBinding.saveBtn.setOnClickListener(this);
        fragmentDrawingBinding.newBtn.setOnClickListener(this);
        fragmentDrawingBinding.eraseBtn.setOnClickListener(this);

        fragmentDrawingBinding.btnRedo.setOnClickListener(this);
        fragmentDrawingBinding.btnUndo.setOnClickListener(this);


        return view;
    }


    public void setBitmap(File bitmap){
        Picasso.with(getContext()).load(bitmap).into(fragmentDrawingBinding.drawing);
    }


    public void refresh() {
        fragmentDrawingBinding.dummy.setImageResource(R.drawable.ic_empty);
    }

    //user clicked paint
    public void paintClicked(View view){
        //use chosen color

        //set erase false
        fragmentDrawingBinding.drawing.setErase(false);
        fragmentDrawingBinding.drawing.setBrushSize(fragmentDrawingBinding.drawing.getLastBrushSize());

        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            fragmentDrawingBinding.drawing.setColor(color);
            //update ui
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(getContext());
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //listen for clicks on size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            //show and wait for user interaction
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(getContext());
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(getContext());
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(getContext());
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);
                    //attempt to save
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getActivity().getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");
                    //feedback
                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getContext(), "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }else if(view.getId() == R.id.btnRedo){

        }else if(view.getId() == R.id.btnUndo){

        }
    }

    public void setBitmap(Bitmap bitmap) {
        fragmentDrawingBinding.drawing.setImageBitmap(bitmap);
    }
}
