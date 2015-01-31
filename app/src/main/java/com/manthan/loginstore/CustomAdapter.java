package com.manthan.loginstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Manthan on 1/28/2015.
 */
public class CustomAdapter extends ArrayAdapter {

    protected Context context;
    private int resource_id;
    private List allNames;
    private static LayoutInflater inflater = null;



    public CustomAdapter(Context main, int list_row, List names) {
        super(main, list_row, names);

        this.context = main;
        this.resource_id = list_row;
        allNames = names;
        inflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return allNames.size();
    }

    @Override
    public Object getItem(int position) {
        return allNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        TextView NameTextView;
        final ImageView fileFolderImage;
        final ImageView arrowImage;

        view = inflater.inflate(resource_id, parent, false);

        try {
            NameTextView = (TextView) view.findViewById(R.id.ListRowTextView);
            fileFolderImage = (ImageView) view.findViewById(R.id.ListRowImageView);
            arrowImage = (ImageView) view.findViewById(R.id.ListRowArrowImageView);
        } catch (ClassCastException e) {
            throw e;
        }

        ListElements item = (ListElements) getItem(position);


        NameTextView.setText(item.getName().toString());


        if (item.isFolder) {
            fileFolderImage.setImageResource(R.drawable.folder);
            arrowImage.setImageResource(R.drawable.arrow);
        }
        else{
            fileFolderImage.setImageResource(R.drawable.file);
        }

        return view;
    }

}
