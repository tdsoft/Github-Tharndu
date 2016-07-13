package com.android.tdsoft.assignment.data;

import android.provider.BaseColumns;

/**
 * Created by Admin on 5/26/2016.
 */
public class Material {
    public static final String TABLE_NAME = "tb_material";
    public static final String COL_MATERIAL_ID = "material_id";
    public static final String COL_SHORT_NAME = "short_name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_TYPE = "type";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + "(" + COL_MATERIAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_SHORT_NAME + "  VARCHAR(30),"
            + COL_DESCRIPTION + "  VARCHAR(255),"
            + COL_TYPE + " text);";


    private int materialId;
    private String shortName;
    private String description;
    private String type;

    public Material(String shortName, String description, String type) {
        setShortName(shortName);
        setType(type);
        setDescription(description);
    }
    public Material(int materialId, String shortName, String type, String description) {
        setMaterialId(materialId);
        setShortName(shortName);
        setType(type);
        setDescription(description);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }
}
