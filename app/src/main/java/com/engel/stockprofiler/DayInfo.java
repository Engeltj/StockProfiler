package com.engel.stockprofiler;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tim on 12/20/2014.
 */
public class DayInfo  implements Parcelable {
    private Date date;
    private double price;

    public DayInfo(Date date, double close){
        this.date = date;
        this.price = close;
    }

    public DayInfo(Parcel in) {
        price = in.readDouble();
        String dateStr = in.readString();
        DateFormat df = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getDate(){
        return date;
    }

    public double getPrice(){
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DateFormat df = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
        String dateStr = df.format(date);

        dest.writeString(dateStr);
        dest.writeDouble(price);
    }

    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DayInfo createFromParcel(Parcel in)
        {
            return new DayInfo(in);
        }

        public DayInfo[] newArray(int size)
        {
            return new DayInfo[size];
        }
    };
}
