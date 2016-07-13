package com.android.tdsoft.androidneurophtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.neuroph.contrib.imgrec.ImageRecognitionPlugin;
import org.neuroph.core.NeuralNetwork;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnMyItemClickListener {
    NeuralNetwork nnet;
    RecyclerView recyclerView;
    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.fileList);
        textView = (TextView) findViewById(R.id.txtStatus);
        imageView = (ImageView) findViewById(R.id.imgSelected);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final InputStream inputStream = getResources().openRawResource(R.raw.animal_set);
        new AsyncTask<Void, Void, NeuralNetwork>() {

            @Override
            protected NeuralNetwork doInBackground(Void... params) {

                // load trained neural network saved with Neuroph Studio (specify some existing neural network file here)
                nnet = NeuralNetwork.load(inputStream); // load trained neural network saved with Neuroph Studio

                return nnet;
            }

            @Override
            protected void onPostExecute(NeuralNetwork aVoid) {
                super.onPostExecute(aVoid);
                loadFiles();
            }
        }.execute();


    }

    private void loadFiles() {
        new AsyncTask<Void, Void, File[]>() {

            @Override
            protected File[] doInBackground(Void... params) {
                File downDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File testDir = new File(downDir.getPath() + "/Test images");
                return testDir.listFiles();
            }

            @Override
            protected void onPostExecute(File[] files) {
                super.onPostExecute(files);
                MyAdapter myAdapter = new MyAdapter(files);
                myAdapter.setOnItemClickListener(MainActivity.this);
                recyclerView.setAdapter(myAdapter);
            }
        }.execute();


    }

    private void recognise(File file) {

        // get the image recognition plugin from neural network
        ImageRecognitionPlugin imageRecognition = (ImageRecognitionPlugin) nnet.getPlugin(ImageRecognitionPlugin.class); // get the image recognition plugin from neural network
        try {
            HashMap<String, Double> hashMap = imageRecognition.recognizeImage(file);
            System.out.println(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        OcrPlugin ocrPlugin = (OcrPlugin) nnet.getPlugin(OcrPlugin.class);
//        Bitmap bitmap = null;
//        ImageAndroid ImageAndroid = new ImageAndroid(bitmap);
//        Image image= ImageAndroid;
//        ocrPlugin.recognizeCharacter(image);
    }

    @Override
    public void onItemClick(File file) {
        Picasso.with(this).load(file).into(imageView);
        recognise(file);
    }
}
