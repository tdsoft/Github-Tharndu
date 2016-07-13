package com.android.tdsoft.assignment.data;

import android.provider.BaseColumns;
import android.support.annotation.StringDef;

/**
 * Created by Admin on 5/26/2016.
 */
public class MaterialProperty implements BaseColumns {

    public static final String TABLE_NAME = "tb_material_property";
    public static final String COL_ID = "id";
    public static final String COL_MATERIAL_ID = "material_id";
    public static final String COL_PROPERTY = "property";
    public static final String COL_UNIT = "unit";
    public static final String COL_TYPE = "type";
    public static final String COL_VALUE1 = "value1";
    public static final String COL_VALUE2 = "value2";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_MATERIAL_ID + " INTEGER,"
            + COL_PROPERTY + " text,"
            + COL_UNIT + " text,"
            + COL_TYPE + " INTEGER,"
            + COL_VALUE1 + " text,"
            + COL_VALUE2 + " text,"
            +" FOREIGN KEY (" + COL_MATERIAL_ID + ") REFERENCES " + Material.TABLE_NAME + "(" + Material.COL_MATERIAL_ID + "));";


    public static final String DECIMAL = "Decimal";
    public static final String BOOLEAN = "boolean";
    public static final String INTEGER = "integer";
    private int id;
    private int materialId;
    private String property;
    private String unit;
    private String type;
    private String value1;
    private String value2;

    public MaterialProperty(int id, int materialId, String property, String type, String unit, String value1, String value2) {
        setId(id);
        setMaterialId(materialId);
        setProperty(property);
        setType(type);
        setUnit(unit);
        setValue1(value1);
        setValue2(value2);
    }

    public MaterialProperty(int materialId, String property, String type, String unit, String value1, String value2) {
        setMaterialId(materialId);
        setProperty(property);
        setType(type);
        setUnit(unit);
        setValue1(value1);
        setValue2(value2);
    }

    public void setTypeString(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public
    @ValueType
    String getType() {
        return type;
    }

    public void setType(@ValueType String type) {
        this.type = type;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    @StringDef({DECIMAL, BOOLEAN, INTEGER})
    @interface ValueType {
    }
}
