package com.android.tdsoft.assignment.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.android.tdsoft.assignment.data.Material;
import com.android.tdsoft.assignment.data.MaterialProperty;
import com.android.tdsoft.assignment.data.source.AssignmentDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 5/26/2016.
 */
public class LocalDataSource implements AssignmentDataSource {
    private static LocalDataSource INSTANCE = null;
    private final DbHelper mDbHelper;

    public LocalDataSource(@NonNull Context context) {
        mDbHelper = new DbHelper(context);
    }


    @Override
    public void getAllProperties(LoadMaterialPropertyCallback callback) {
        List<MaterialProperty> materialProperties = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                MaterialProperty.COL_ID,
                MaterialProperty.COL_MATERIAL_ID,
                MaterialProperty.COL_PROPERTY,
                MaterialProperty.COL_TYPE,
                MaterialProperty.COL_UNIT,
                MaterialProperty.COL_VALUE1,
                MaterialProperty.COL_VALUE2
        };

        Cursor c =  db.query(true,MaterialProperty.TABLE_NAME, projection, null, null, null, null, null,null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndexOrThrow(MaterialProperty.COL_ID));
                int materialId = c.getInt(c.getColumnIndexOrThrow(MaterialProperty.COL_MATERIAL_ID));
                String property = c.getString(c.getColumnIndexOrThrow(MaterialProperty.COL_PROPERTY));
                String type = c.getString(c.getColumnIndexOrThrow(MaterialProperty.COL_TYPE));
                String unit = c.getString(c.getColumnIndexOrThrow(MaterialProperty.COL_UNIT));
                String value1 = c.getString(c.getColumnIndexOrThrow(MaterialProperty.COL_VALUE1));
                String value2 = c.getString(c.getColumnIndexOrThrow(MaterialProperty.COL_VALUE2));
                MaterialProperty materialProperty = new MaterialProperty(id, materialId, property, type, unit,value1,value2);
                materialProperties.add(materialProperty);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (materialProperties.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(materialProperties);
        }

    }

    @Override
    public void getAllMaterials(LoadMaterialCallback callback) {
        List<Material> materials = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Material.COL_MATERIAL_ID,
                Material.COL_SHORT_NAME,
                Material.COL_DESCRIPTION,
                Material.COL_TYPE
        };

        Cursor c = db.query(Material.TABLE_NAME, projection, null, null, null, null, null,null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int materialId = c.getInt(c.getColumnIndexOrThrow(Material.COL_MATERIAL_ID));
                String shortName = c.getString(c.getColumnIndexOrThrow(Material.COL_SHORT_NAME));
                String description = c.getString(c.getColumnIndexOrThrow(Material.COL_DESCRIPTION));
                String type = c.getString(c.getColumnIndexOrThrow(Material.COL_TYPE));
                Material material = new Material(materialId, shortName, type, description);
                materials.add(material);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (materials.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(materials);
        }
    }

    @Override
    public void saveMaterial(Material material) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Material.COL_SHORT_NAME, material.getShortName());
        values.put(Material.COL_DESCRIPTION, material.getDescription());
        values.put(Material.COL_TYPE, material.getType());
        db.insert(Material.TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void saveMaterialProperty(MaterialProperty materialProperty) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MaterialProperty.COL_MATERIAL_ID, materialProperty.getMaterialId());
        values.put(MaterialProperty.COL_PROPERTY, materialProperty.getProperty());
        values.put(MaterialProperty.COL_TYPE, materialProperty.getType());
        values.put(MaterialProperty.COL_UNIT, materialProperty.getUnit());
        values.put(MaterialProperty.COL_VALUE1, materialProperty.getValue1());
        values.put(MaterialProperty.COL_VALUE2, materialProperty.getValue2());
        db.insert(MaterialProperty.TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void getMaterialsForSql(String sql, LoadMaterialCallback callback) {
        List<Material> materials = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int materialId = c.getInt(c.getColumnIndexOrThrow(Material.COL_MATERIAL_ID));
                String shortName = c.getString(c.getColumnIndexOrThrow(Material.COL_SHORT_NAME));
                String description = c.getString(c.getColumnIndexOrThrow(Material.COL_DESCRIPTION));
                String type = c.getString(c.getColumnIndexOrThrow(Material.COL_TYPE));
                Material material = new Material(materialId, shortName, type, description);
                materials.add(material);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (materials.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(materials);
        }
    }

    public static LocalDataSource getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }
}
