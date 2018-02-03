package com.example.shaimaaderbaz.earthquake.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shaimaaderbaz.earthquake.R;
import com.example.shaimaaderbaz.earthquake.models.Earthquake;

import java.util.ArrayList;

/**
 * Created by Shaimaa Derbaz on 2/3/2018.
 */

public class EearthquakeAdapter  extends ArrayAdapter<Earthquake> {

    public EearthquakeAdapter(Activity context, ArrayList<Earthquake> items) {

        super(context, 0, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(
                    getContext()).inflate(R.layout.list_item, null, false);

        }
        Earthquake currentItem = getItem(position);
        TextView nameItemTextView=(TextView)listItemView.findViewById(R.id.item_mgt_text_view);
        nameItemTextView.setText(currentItem.getMagnitude()+"");
        TextView aboutItemTextView = (TextView) listItemView.findViewById(R.id.item_loc_text_view);
        aboutItemTextView.setText(currentItem.getLocation());
        TextView itemTimeView = (TextView) listItemView.findViewById(R.id.item_time_text_view);
        itemTimeView.setText(currentItem.getTime()+"");
        TextView itemUrlView = (TextView) listItemView.findViewById(R.id.item_url_text_view);
        itemUrlView.setText(currentItem.getUrl());


        return listItemView;
    }

}
