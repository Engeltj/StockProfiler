package com.engel.stockprofiler;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends FragmentActivity {
    private GraphView graphView;
    private final String TAG = "MainActivity";
    private ArrayList<DayInfo> data;
    private TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity: onCreate()");
        setContentView(R.layout.activity_main);

        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();

        TabHost.TabSpec spec = mTabHost.newTabSpec("tab_graph").setIndicator("Graph", getResources().getDrawable(R.drawable.ic_action_portfolio));
        spec.setContent(R.id.tab1);
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec("tab_portfolio").setIndicator("Portfolio", getResources().getDrawable(R.drawable.ic_action_portfolio));
        spec.setContent(R.id.tab2);
        mTabHost.addTab(spec);

        Button btn = (Button) findViewById(R.id.btn_search);
        //btn.setOnClickListener(this);


        System.out.println("onCreate saved: " + savedInstanceState);

//        if (savedInstanceState != null){
//            data = savedInstanceState.getParcelableArrayList("graphData");
//            EditText symbolField = (EditText) this.findViewById(R.id.editText);
//            symbolField.setText(savedInstanceState.getString("symbol"));
//            updateGraph();
//        }
//        System.out.println("CREATED: " + savedInstanceState);
//        if (savedInstanceState != null){
//            data = savedInstanceState.getParcelableArrayList("graphData");
//            EditText symbolField = (EditText) this.findViewById(R.id.editText);
//            symbolField.setText(savedInstanceState.getString("symbol"));
//            System.out.println("RESTORED #2");
//        }
//        ArrayList<String> rawData = new ArrayList<>();
//        try {
//            FileInputStream in = this.openFileInput("symbol_data.txt");
//            InputStreamReader inputStreamReader = new InputStreamReader(in);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                rawData.add(line);
//            }
//            in.close();
//            data = DataCollector.parseData(rawData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        updateGraph();
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d(TAG, "MainActivity: onRestart()");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(TAG, "MainActivity: onStart()");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG, "MainActivity: onResume()");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(TAG, "MainActivity: onPause()");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(TAG, "MainActivity: onStop()");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, "MainActivity: onDestroy()");
//    }
//
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        EditText symbolField = (EditText) this.findViewById(R.id.editText);
        outState.putParcelableArrayList("graphData", data);
        outState.putString("symbol", symbolField.getText().toString());
        Log.d(TAG, "Saved.");
    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedState) {
//        super.onRestoreInstanceState(savedState);
//        data = savedState.getParcelableArrayList("graphData");
//        EditText symbolField = (EditText) this.findViewById(R.id.editText);
//        symbolField.setText(savedState.getString("symbol"));
//        updateGraph();
//        System.out.println("RESTORED");
//    }


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
//            case R.id.action_portfolio:
//                Intent intent = new Intent(getApplicationContext(), PortfolioActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_FUNCTONE);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
//                break;
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

    public void onClick(View v) {
        FragmentManager fm = this.getFragmentManager();

        ClickHandler graphHandler = (ClickHandler) fm.findFragmentById(R.id.graph_fragment);

        if (graphHandler != null) {
            graphHandler.onClick(this, v);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if(requestCode==REQUEST_CODE_FUNCTONE){
//            if(resultCode==RESULT_OK){
//                Log.d("MainActivity", "Result");
//            }
//
//        }


        super.onActivityResult(requestCode, resultCode, data);
    }


}



