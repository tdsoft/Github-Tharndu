package com.android.tdsoft.assignment;

import android.os.Bundle;
import android.view.View;

import com.android.tdsoft.assignment.base.BaseActivity;
import com.android.tdsoft.assignment.data.Material;
import com.android.tdsoft.assignment.data.MaterialProperty;
import com.android.tdsoft.assignment.data.source.AssignmentRepository;
import com.android.tdsoft.assignment.searchresult.SearchDialog;
import com.android.tdsoft.assignment.searchresult.SearchResultFragment;
import com.android.tdsoft.assignment.util.Injection;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replace(SearchResultFragment.getInstance(this),false);

        //Add initial data to db and comment this method
        addInitialData();
    }



    private void addInitialData() {
        AssignmentRepository assignmentRepository = Injection.provideChambaRepository(this);
        assignmentRepository.saveMaterial(new Material("Copper", "Copper", "Metal"));
        assignmentRepository.saveMaterial(new Material("Steel", "Constructions steel containing <0.8% C", "Metal"));
        assignmentRepository.saveMaterial(new Material("Polycarbonate Macrolon", "Polycarbonate from Supplier 1", "Plastic"));
        assignmentRepository.saveMaterial(new Material("Polycarbonate Lexan", "Polycarbonate from", "Plastic"));
        assignmentRepository.saveMaterial(new Material("Oak", "Oak wood camera dried", "Wood"));
        assignmentRepository.saveMaterial(new Material("Glass", "Normal glass", "Glass"));

        assignmentRepository.saveMaterialProperty(new MaterialProperty(1,"Density",MaterialProperty.DECIMAL,"G/cm3","8.96",""));
        assignmentRepository.saveMaterialProperty(new MaterialProperty(1,"Melting point",MaterialProperty.DECIMAL,"C","1084.62",""));
        assignmentRepository.saveMaterialProperty(new MaterialProperty(1,"Opacity",MaterialProperty.BOOLEAN,"","False",""));
        assignmentRepository.saveMaterialProperty(new MaterialProperty(1,"Young Modulus",MaterialProperty.INTEGER,"GPA","110","128"));
        assignmentRepository.saveMaterialProperty(new MaterialProperty(1,"Boiling Point",MaterialProperty.DECIMAL,"C","2562",""));
        assignmentRepository.saveMaterialProperty(new MaterialProperty(2,"Density",MaterialProperty.DECIMAL,"G/cm3","7.5","7.6"));
        assignmentRepository.saveMaterialProperty(new MaterialProperty(2,"Melting Point",MaterialProperty.DECIMAL,"C","1450","1520"));
    }

    public void onSearchClick(View view) {
        SearchDialog searchDialog = SearchDialog.getInstance(this);
        searchDialog.show(getSupportFragmentManager(), "seachr_dlg");
    }
}
