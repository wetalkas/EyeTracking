package com.wet.eyetracking.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.wet.eyetracking.model.ExperimentItem;
import com.wet.eyetracking.model.PointRealmObject;
import com.wet.eyetracking.R;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    private LineChart mChart;

    private static final String ID = "id";

    long id;

    public static ChartFragment newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(ID, id);
        ChartFragment fragment = new ChartFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        id = getArguments().getLong(ID);

        Log.d("arguments", "id: " + id);

        mChart = (LineChart) view.findViewById(R.id.chart);


        mChart.setPinchZoom(true);

        mChart.getDescription().setEnabled(false);

        mChart.getAxisRight().setEnabled(false);

        mChart.getLegend().setEnabled(false);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);


        List<Entry> entries = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        ExperimentItem experiment = realm.where(ExperimentItem.class).equalTo(ID, id).findFirst();
        List<PointRealmObject> pointRealmObjects = experiment.getPoints();
        Log.d("entry count", ": " + pointRealmObjects.size());
        for (int i = 0; i < pointRealmObjects.size(); i++) {
            if (pointRealmObjects.get(i).getX() == -12345) {
                if (i > 0 && i < pointRealmObjects.size() - 1) {
                    double old = pointRealmObjects.get(i - 1).getX();
                    double future = pointRealmObjects.get(i + 1).getX();

                    double oldY = pointRealmObjects.get(i - 1).getY();
                    double futureY = pointRealmObjects.get(i + 1).getY();

                    entries.add(new Entry((float) i,
                            (float) ((old + future) / 2)));

                }

            } else {
                entries.add(new Entry((float) i,
                        (float) pointRealmObjects.get(i).getX()));
            }
        }


        LineDataSet dataSet = new LineDataSet(entries, "График");
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
        mChart.invalidate();
    }


    public static boolean writeTextArrayToFile(Object[] textLines, boolean append) {
        boolean wroteOK = false;
        try {
            BufferedWriter writer = null;
            String lineSep = System.getProperty("line.separator");
            try {
                /*
                 * if (append) it means add more lines to file
                 * if (!append) it means delete old contents of file
                 */
                writer = new BufferedWriter(new FileWriter("/storage/emulated/0/procc/coordinates.txt", append));
                /*
                 * Modern usage would be for (String s : textLines)
                 */
                for (int i = 0; i < textLines.length; i++) {
                    writer.write(textLines[i] + lineSep);
                }// end for
                /*
                 * When you have finished writing, you can return "true"
                 */
                wroteOK = true;
            }//end inner try (might throw IOException or FileNotFoundException)
            /*
             * This finally will definitely be executed, but might throw an IOException, so it must be inside a try
             */ finally {
                /*
                 * If there has been an exception, the writer might be null, so you must test for nullity.
                 */
                if (writer != null) {
                    writer.close();
                }//end if
            }//end finally
        }//end outer try
        catch (FileNotFoundException fnf) {
        }//end catch FNFE
        catch (IOException ioe) {

        }//end catch IOE
        return wroteOK;
    }//end method

}
