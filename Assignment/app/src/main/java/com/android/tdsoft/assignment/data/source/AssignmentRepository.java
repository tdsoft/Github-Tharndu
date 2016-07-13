package com.android.tdsoft.assignment.data.source;

import android.support.annotation.NonNull;

import com.android.tdsoft.assignment.data.Material;
import com.android.tdsoft.assignment.data.MaterialProperty;
import com.android.tdsoft.assignment.data.source.local.LocalDataSource;

/**
 * Created by Admin on 5/26/2016.
 */
public class AssignmentRepository implements AssignmentDataSource {
    private static AssignmentRepository INSTANCE = null;
    private final LocalDataSource mLocalDataSource;

    // Prevent direct instantiation.
    private AssignmentRepository(@NonNull LocalDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    public static AssignmentRepository getInstance(LocalDataSource localDataSource){
        if (INSTANCE == null) {
            INSTANCE = new AssignmentRepository(localDataSource);
        }
        return INSTANCE;
    }


    @Override
    public void getAllProperties(LoadMaterialPropertyCallback loadMaterialPropertyCallback) {
        mLocalDataSource.getAllProperties(loadMaterialPropertyCallback);
    }

    @Override
    public void getAllMaterials(LoadMaterialCallback loadMaterialCallback) {
    mLocalDataSource.getAllMaterials(loadMaterialCallback);
    }

    @Override
    public void saveMaterial(Material material) {
        mLocalDataSource.saveMaterial(material);
    }

    @Override
    public void saveMaterialProperty(MaterialProperty materialProperty) {
        mLocalDataSource.saveMaterialProperty(materialProperty);
    }

    @Override
    public void getMaterialsForSql(String sql, LoadMaterialCallback callback) {
        mLocalDataSource.getMaterialsForSql(sql,callback);
    }
}
