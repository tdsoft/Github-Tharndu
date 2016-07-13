package com.android.tdsoft.assignment.searchresult;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tdsoft.assignment.R;
import com.android.tdsoft.assignment.data.Material;
import com.android.tdsoft.assignment.data.MaterialProperty;
import com.android.tdsoft.assignment.databinding.FragmentSearchDialogBinding;
import com.android.tdsoft.assignment.util.Injection;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SearchDialog extends DialogFragment implements SearchDialogContract.View, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FragmentSearchDialogBinding mFragmentSearchDialogBinding;
    private SearchDialogContract.Presenter mPresenter;
    private List<ListItemView> listItemViews = new ArrayList<>();
    private List<ListItemView> allItems = new ArrayList<>();
    private MyAdapter myAdapter;
    private ListItemView currentItem;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.app_name));
        mFragmentSearchDialogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_dialog, container, false);
        View view = mFragmentSearchDialogBinding.getRoot();
        init();
        return view;
    }


    public static SearchDialog getInstance(Context context) {
        SearchDialog searchDialog = new SearchDialog();
        searchDialog.setPresenter(new SearchDialogPresenter(Injection.provideChambaRepository(context), searchDialog));
        return searchDialog;
    }


    private void init() {
        mFragmentSearchDialogBinding.btnSearch.setOnClickListener(this);
        mFragmentSearchDialogBinding.btnCancel.setOnClickListener(this);
        mFragmentSearchDialogBinding.btnAddProp.setOnClickListener(this);

        this.myAdapter = new MyAdapter();
        mFragmentSearchDialogBinding.searchProperties.setAdapter(myAdapter);

        mPresenter.getAllProperties();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_add_prop:
                listItemViews.add(currentItem);
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_search:
                String sql = createSql();
                if (sql == null) {
                    Toast.makeText(getContext(), "Invalid property value found please try again", Toast.LENGTH_LONG).show();
                    return;
                }
                EventBus.getDefault().post(sql);
                dismiss();
                break;
        }
    }


    private String createSql() {
        if(listItemViews.size()==0){
            Toast.makeText(getContext(), "Please add a property", Toast.LENGTH_LONG).show();
            return null;
        }
        String sql = "SELECT DISTINCT tm.* FROM " + MaterialProperty.TABLE_NAME + " tmp INNER JOIN " + Material.TABLE_NAME + " tm ON tm." + Material.COL_MATERIAL_ID + " = tmp." + MaterialProperty.COL_MATERIAL_ID + " WHERE ";
        for (int i = 0; i < listItemViews.size(); i++) {
            ListItemView itemView = listItemViews.get(i);
            View view = mFragmentSearchDialogBinding.searchProperties.getChildAt(i);//.getAdapter().getView(i, null, mFragmentSearchDialogBinding.searchProperties);
            if (i < (listItemViews.size() - 1)) {
                switch (itemView.viewType) {
                    case BOOLEAN_VIEW:
                        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                        sql += MaterialProperty.COL_PROPERTY + " = '" + itemView.materialProperty.getProperty() + "' AND "
                                + MaterialProperty.COL_VALUE1 + " = '" + (checkBox.isChecked() ? "True" : "False") + "' AND ";
                        break;
                    case DECIMAL_VIEW:
                    case INTEGER_VIEW:
                        EditText min = (EditText) view.findViewById(R.id.etMin);
                        EditText max = (EditText) view.findViewById(R.id.etMax);
                        if (TextUtils.isEmpty(min.getText()) || TextUtils.isEmpty(max.getText())) {
                            return null;
                        }
                        System.out.println(min.getText().toString() + "' AND " + MaterialProperty.COL_VALUE1 + " <= '" + max.getText().toString());
                        sql += MaterialProperty.COL_PROPERTY + " = '" + itemView.materialProperty.getProperty() + "' AND CAST("
                                + MaterialProperty.COL_VALUE1 + " AS DECIMAL" + ") >= " + min.getText() + " AND CAST(" + MaterialProperty.COL_VALUE1 + " AS DECIMAL" + ") <= " + max.getText() + " AND ";

                        break;
                }

            } else if (i == (listItemViews.size() - 1)) {
                switch (itemView.viewType) {
                    case BOOLEAN_VIEW:
                        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                        sql += MaterialProperty.COL_PROPERTY + " = '" + itemView.materialProperty.getProperty() + "' AND "
                                + MaterialProperty.COL_VALUE1 + " = '" + (checkBox.isChecked() ? "True" : "False") + "'";
                        break;
                    case DECIMAL_VIEW:
                    case INTEGER_VIEW:
                        EditText min = (EditText) view.findViewById(R.id.etMin);
                        EditText max = (EditText) view.findViewById(R.id.etMax);
                        if (TextUtils.isEmpty(min.getText()) || TextUtils.isEmpty(max.getText())) {
                            return null;
                        }
                        sql += MaterialProperty.COL_PROPERTY + " = '" + itemView.materialProperty.getProperty() + "' AND CAST("
                                + MaterialProperty.COL_VALUE1 + " AS DECIMAL" + ") >= " + min.getText() + " AND CAST(" + MaterialProperty.COL_VALUE1 + " AS DECIMAL" + ") <= " + max.getText() + "";

                        break;
                }

            }

        }

        return sql;
    }


    @Override
    public void setPresenter(SearchDialogContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void dismissProgressIndicator() {

    }

    @Override
    public void onAllPropertiesLoaded(List<MaterialProperty> materialProperties) {

        List<String> prperties = new ArrayList<>();
        for (MaterialProperty materialProperty : materialProperties) {
            switch (materialProperty.getType()) {
                case MaterialProperty.BOOLEAN:
                    allItems.add(new ListItemView(materialProperty, BOOLEAN_VIEW));
                    break;
                case MaterialProperty.DECIMAL:
                    allItems.add(new ListItemView(materialProperty, DECIMAL_VIEW));
                    break;
                case MaterialProperty.INTEGER:
                    allItems.add(new ListItemView(materialProperty, INTEGER_VIEW));
                    break;
            }

            if (!prperties.contains(materialProperty.getProperty())) {
                prperties.add(materialProperty.getProperty());
            }

        }


        mFragmentSearchDialogBinding.spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, prperties));
        mFragmentSearchDialogBinding.spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onNoDataFound() {

    }


    public static final int INTEGER_VIEW = 0;
    public static final int DECIMAL_VIEW = 1;
    public static final int BOOLEAN_VIEW = 2;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentItem = allItems.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @IntDef({INTEGER_VIEW, DECIMAL_VIEW, BOOLEAN_VIEW})
    @interface ViewType {
    }

    public class ListItemView {
        public MaterialProperty materialProperty;
        public int viewType;

        public ListItemView(MaterialProperty materialProperty, @ViewType int viewType) {
            this.materialProperty = materialProperty;
            this.viewType = viewType;
        }
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listItemViews.size();
        }

        @Override
        public Object getItem(int position) {
            return listItemViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ListItemView listViewItem = listItemViews.get(position);
            int listViewItemType = getItemViewType(position);


            if (convertView == null) {
                switch (listViewItemType) {
                    case BOOLEAN_VIEW:
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_prop_boolean, null);
                        break;
                    case INTEGER_VIEW:
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_prop_integer, null);
                        break;
                    case DECIMAL_VIEW:
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_prop_integer, null);
                        break;
                }
            }

            Button btnRemoveProp = (Button) convertView.findViewById(R.id.btnRemoveProp);
            TextView textView = (TextView) convertView.findViewById(R.id.txtPropertyName);
            textView.setText(listViewItem.materialProperty.getProperty());
            btnRemoveProp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItemViews.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            return listItemViews.get(position).viewType;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return super.getDropDownView(position, convertView, parent);
        }
    }
}