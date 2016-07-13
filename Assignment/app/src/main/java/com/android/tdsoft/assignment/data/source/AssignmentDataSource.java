package com.android.tdsoft.assignment.data.source;

import com.android.tdsoft.assignment.data.Material;
import com.android.tdsoft.assignment.data.MaterialProperty;

import java.util.List;

/**
 * Created by Admin on 5/26/2016.
 */
public interface AssignmentDataSource {

    interface LoadMaterialCallback {

        void onTasksLoaded(List<Material> tasks);

        void onDataNotAvailable();
    }

    interface LoadMaterialPropertyCallback {

        void onTasksLoaded(List<MaterialProperty> tasks);

        void onDataNotAvailable();
    }


    void getAllProperties(LoadMaterialPropertyCallback loadMaterialPropertyCallback);
    void getAllMaterials(LoadMaterialCallback loadMaterialCallback);
    void saveMaterial(Material material);
    void saveMaterialProperty(MaterialProperty materialProperty);
    void getMaterialsForSql(String sql, LoadMaterialCallback callback);

}
