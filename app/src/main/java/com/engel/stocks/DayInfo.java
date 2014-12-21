package com.engel.stocks;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tim on 12/20/2014.
 */
public class DayInfo  implements Serializable {
    private Date date;
    private double price;

    public DayInfo(Date date, double close){
        this.date = date;
        this.price = close;
    }

    public Date getDate(){
        return date;
    }

    public double getPrice(){
        return price;
    }
}
