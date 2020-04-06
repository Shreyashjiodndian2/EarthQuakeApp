package com.example.earthquake;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class earthquake {
    private double mMag;
    private long mDate;
    private String mLocation;
    private String mUrl;

    public earthquake(double mag, String location, long date, String url) {
        mUrl = url;
        mMag = mag;
        mLocation = location;
        mDate = date;
    }

    public String getmMag() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(mMag);
    }

    public String getmLocationCity() {
        return mLocation.split("of")[mLocation.split("of").length - 1];
    }

    public String getmDate() {
        Date dateObject = new Date(mDate);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, ''yy");
        String dateToDisplay = dateFormatter.format(dateObject);
        //Log.d("mDate", dateObject.toString() + "  " + dateFormatter.toString() + "   " + dateToDisplay);
        return dateToDisplay;
    }

    public String getmTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(new Date(mDate));
    }

    public String getmLocationdistance() {
        int mLength = mLocation.split("of").length;
        String data = mLocation.split("of")[0];
        if (mLength != 1) {
            return data + "of";
        } else {
            return "Near The";
        }
    }

    public String getmUrl() {
        return mUrl;
    }
}
