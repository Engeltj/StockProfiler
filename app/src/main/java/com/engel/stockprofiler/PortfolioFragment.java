package com.engel.stockprofiler;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Tim on 12/22/2014.
 */
public class PortfolioFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.portfolio_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        String[] items = {
                "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7",
                "Item 8", "Item 9", "Item 10", "Item 11", "Item 12", "Item 13", "Item 14",
                "Item 15"
        };

        ListView lv = (ListView) getView().findViewById(R.id.listView);
        ListAdapter mAdapter = new ArrayAdapter(getView().getContext(), android.R.layout.simple_list_item_1, items);
        lv.setAdapter(mAdapter);

        if (savedInstanceState != null){
        }
    }
}
