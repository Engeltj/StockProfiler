package com.engel.stockprofiler;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Tim on 12/21/2014.
 */
public class GraphFragment extends Fragment implements OnTaskCompleted, ClickHandler {
    private GraphView graphView;
    private ArrayList<DayInfo> data;
    private static final String TAG = "GraphFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.graph_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            data = savedInstanceState.getParcelableArrayList("graphData");
            EditText symbolField = (EditText) getView().findViewById(R.id.editText);
            symbolField.setText(savedInstanceState.getString("symbol"));
            updateGraph();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        EditText symbolField = (EditText) getView().findViewById(R.id.editText);
        outState.putParcelableArrayList("graphData", data);
        outState.putString("symbol", symbolField.getText().toString());
        Log.d(TAG, "Saved.");
    }

    @Override
    public void onClick(MainActivity context, View v) {
        switch(v.getId()){
            case R.id.btn_search:
                //Hide keyboard
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                v.requestFocus();

                //Obtain and collect user specified symbol's data
                EditText symbolField = (EditText) context.findViewById(R.id.editText);
                String symbol = symbolField.getText().toString();
                DataCollector dataCollector = new DataCollector(this, symbol);
                dataCollector.execute();

                //Remove current graph
                FrameLayout layout = (FrameLayout) context.findViewById(R.id.frameLayout);
                layout.removeAllViews();

                //Display progress circle
                ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
                progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFF000, android.graphics.PorterDuff.Mode.MULTIPLY);
                layout.addView(progressBar);

                break;
            default:
                break;
        }
    }

    public void updateGraph(){
        FrameLayout layout = (FrameLayout) getView().findViewById(R.id.frameLayout);
        layout.removeAllViews();

        if (data == null || data.size() == 0)
            return;

        GraphView.GraphViewData[] graphData = new GraphView.GraphViewData[data.size()];

        double v=0;
        for (int i=0; i<data.size(); i++) {
            graphData[i] = new GraphView.GraphViewData(data.get(i).getDate().getTime(), data.get(i).getPrice());
        }

        graphView = new LineGraphView(
                getView().getContext() // context
                , "" // heading
        );

        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd/yy");
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                }
                return null; // let graphview generate Y-axis label for us
            }
        });
        graphView.setViewPort(data.get(data.size()-1).getDate().getTime() - 5*24*60*60*1000, 5*24*60*60*1000);
        graphView.addSeries(new GraphViewSeries(graphData)); // data


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
            outputStream = getView().getContext().openFileOutput(filename, Context.MODE_PRIVATE);
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
