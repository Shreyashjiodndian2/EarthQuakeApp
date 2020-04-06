package com.example.earthquake;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class EarthQuakeAdapter extends ArrayAdapter<earthquake> {

    private ArrayList<earthquake> currentData = null;

    public EarthQuakeAdapter(Activity context, ArrayList<earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemlilstView = convertView;
        if (itemlilstView == null) {
            itemlilstView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);
        }
        earthquake currentData = getItem(position);
        TextView magData = itemlilstView.findViewById(R.id.mag);
        TextView locDisData = itemlilstView.findViewById(R.id.distance);
        TextView locCityData = itemlilstView.findViewById(R.id.location);
        TextView dateData = itemlilstView.findViewById(R.id.date);
        TextView timeData = itemlilstView.findViewById(R.id.listTime);
        GradientDrawable gradMagCircle = (GradientDrawable) magData.getBackground();
        int magColor = getMagColor(currentData.getmMag());
        //System.out.println(ContextCompat.getColor(getContext(), magColor));
        gradMagCircle.setColor(ContextCompat.getColor(getContext(), magColor));
        magData.setText(currentData.getmMag());
        locDisData.setText(currentData.getmLocationdistance());
        locCityData.setText(currentData.getmLocationCity());
        dateData.setText(currentData.getmDate());
        //Log.v("DAte", currentData.getmDate());
        timeData.setText(currentData.getmTime());
        return itemlilstView;
    }

    private int getMagColor(String getmMag) {
        //int mag = getmMag.split("\\.").length;
        switch (getmMag.split("\\.")[0]) {
            case "1":
                return R.color.magnitude1;
            case "2":
                return R.color.magnitude2;
            case "3":
                return R.color.magnitude3;
            case "4":
                return R.color.magnitude4;
            case "5":
                return R.color.magnitude5;
            case "6":
                return R.color.magnitude6;
            case "7":
                return R.color.magnitude7;
            case "8":
                return R.color.magnitude8;
            case "9":
                return R.color.magnitude9;
            default:
                return R.color.magnitude10plus;
        }
    }

    public void setCurrentData(ArrayList<earthquake> currentData) {
        currentData.addAll(currentData);
        notifyDataSetChanged();
    }
}
