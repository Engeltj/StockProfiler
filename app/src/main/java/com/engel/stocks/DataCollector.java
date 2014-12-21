package com.engel.stocks;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Tim on 12/20/2014.
 */
public class DataCollector extends AsyncTask<URL, Integer, Long> {
    private OnTaskCompleted main;
    private String uri;
    private ArrayList<DayInfo> data;
    private ArrayList<String> rawData;

    public DataCollector(OnTaskCompleted main, String symbol){
        this.main = main;
        int fromMonth = 0; //from month-1
        int toMonth = 11; //to month-1
        int fromYear = 2000;
        int toYear = 2015;
        int fromDay = 1;
        int toDay = 31;
        String type = "d"; //d for day, m for month, y for yearly

        data = new ArrayList<>();
        rawData = new ArrayList<>();
        uri = "http://ichart.finance.yahoo.com/table.csv?s="+symbol+"&d="+toMonth+"&e="+toDay+"&f="+toYear+"&g="+type+"&a="+fromMonth+"&b="+fromDay+"&c="+fromYear+"&ignore=.csv";
    }

    public static ArrayList<DayInfo> parseData(ArrayList<String> rawData){
        ArrayList<DayInfo> data = new ArrayList<>();
        for (int i=0;i<rawData.size();i++){
            String[] row = rawData.get(i).split(",");
            if (row.length < 5)
                break;
            DateFormat format = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
            Date date = new Date();
            try {
                date = format.parse(row[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            data.add(0,new DayInfo(date, Double.valueOf(row[4])));
        }
        return data;
    }

    @Override
    protected Long doInBackground(URL... params) {
        System.out.println(uri);
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(uri);
        InputStream is = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            is = response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            reader.readLine(); //skip line #1

            while ((line = reader.readLine()) != null) {
                rawData.add(0, line);
            }
            data = parseData(rawData);
        }
        catch (IOException ex) {
            // handle exception
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                // handle exception
            }
        }


        return 0L;
    }

    public ArrayList<DayInfo> getData(){
        return data;
    }
    public ArrayList<String> getRawData(){
        return rawData;
    }

    @Override
    protected void onPostExecute(Long result){
        // your stuff
        main.onDataCollected(this);
    }


}
