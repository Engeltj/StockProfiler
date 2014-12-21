package com.engel.stocks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.*;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, OnTaskCompleted {
    private GraphView graphView;
    private ArrayList<DayInfo> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btn_search);
        btn.setOnClickListener(this);

        ArrayList<String> rawData = new ArrayList<>();
        try {
            FileInputStream in = this.openFileInput("symbol_data.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                rawData.add(line);
            }
            in.close();
            data = DataCollector.parseData(rawData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateGraph();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                break;
            case R.id.action_portfolio:
                Intent intent = new Intent(MainActivity.this, PortfolioActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_search:
                EditText symbolField = (EditText) this.findViewById(R.id.editText);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                v.requestFocus();
                String symbol = symbolField.getText().toString();
                System.out.println("Symbol: " + symbol);
                DataCollector dataCollector = new DataCollector(this, symbol);
                dataCollector.execute();

                FrameLayout layout = (FrameLayout) findViewById(R.id.frameLayout);
                layout.removeAllViews();

                EditText message = new EditText(this);
                message.setText("Drawing, please wait ..");
                layout.addView(message);

                break;
            default:
                break;
        }
    }

    public void updateGraph(){
        FrameLayout layout = (FrameLayout) findViewById(R.id.frameLayout);
        layout.removeAllViews();

        if (data == null || data.size() == 0)
            return;

        GraphViewData[] graphData = new GraphViewData[data.size()];

        double v=0;
        for (int i=0; i<data.size(); i++) {
            graphData[i] = new GraphViewData(i, data.get(i).getPrice());
        }

        graphView = new LineGraphView(
                this // context
                , "" // heading
        );
        graphView.addSeries(new GraphViewSeries(graphData)); // data

        graphView.setViewPort(data.size()-6, data.size()-1);
        graphView.setScrollable(true);
        // optional - activate scaling / zooming
        graphView.setScalable(true);

        layout.addView(graphView);
    }

    @Override
    public void onDataCollected(DataCollector dataCollector) {
        this.data = dataCollector.getData();
        ArrayList<String> rawData = dataCollector.getRawData();
        String filename = "symbol_data.txt";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            for (int i=0;i<rawData.size();i++){
                outputStream.write(rawData.get(i).getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateGraph();
    }
}
