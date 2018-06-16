package com.example.daniela.imovies.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


import com.example.daniela.imovies.entity.MyList;
import com.example.daniela.imovies.R;


public class ListSerieAdapter extends ArrayAdapter<MyList> {

    public ListSerieAdapter(Context context, int textViewResourceId){
        super(context, textViewResourceId);
    }

    public ListSerieAdapter(Context context, int resource, ArrayList<MyList> list){
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.series_list_item, null);
        }

        MyList list = getItem(position);

        if(list != null){
            TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);

            if(txtDescription != null){
                txtDescription.setText(list.getDescription());
            }
        }

        return v;
    }
}
