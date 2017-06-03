package com.gatewayclub.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gatewayclub.app.R;
import com.gatewayclub.app.pojos.LocationDto;

import java.util.ArrayList;



public class LocationSpinAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<LocationDto>values;


    public LocationSpinAdapter(Context context, ArrayList<LocationDto> values){
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_item, null);
        }
        TextView label = (TextView) convertView.findViewById(R.id.spinnerTarget);
        label.setText(values.get(position).getLocationName());
        return convertView;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
//    @Override
//    public View getDropDownView(int position, View convertView,
//                                ViewGroup parent) {
//        TextView label = new TextView(context);
//        label.setTextColor(Color.BLACK);
//        label.setText(values[position].getName());
//
//        return label;
//    }
}