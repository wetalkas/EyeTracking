package com.wet.eyetracking.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wet on 14.05.17.
 */


public class MyUtils {

    public static final String DETECTED_EYES_DIR = "procc";


    public static List<String> getImagePaths() {
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Eye_Tracking");
        File[] fileList = folder.listFiles();
        List<String> paths = new ArrayList<>();
        for (File file : fileList) {
            paths.add(file.getPath());
        }
        return paths;
    }

    public static List<File> getImageFiles() {
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Eye_Tracking");
        File[] fileList = folder.listFiles();
        return Arrays.asList(fileList);
    }

    private static final String TAG = "MyUtils";


    public static File writeImage(File file, Mat mat) {

        File directory = new File("/storage/emulated/0" + "/procc");
        //Log.d(TAG, "writeImage: dir " + directory.getName());
        if (!directory.exists()){
            boolean dir = directory.mkdir();

            //Log.d(TAG, "writeImage: dir " + dir);
        }

        //Log.d(TAG, "writeImage: " + directory.getAbsolutePath() + "/" + file.getName());

        boolean write = Imgcodecs.imwrite(directory.getAbsolutePath() + "/" + file.getName(), mat);

        //Log.d(TAG, "writeImage: " + write);
        return new File(directory.getAbsolutePath() + "/" + file.getName());
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String parseDate(long initialDate, Context context) {
        Locale currentLocale = context.getResources().getConfiguration().locale;

        Date date = new Date(initialDate); //* 1000); // *1000 is to convert seconds to milliseconds

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy в H:mm", currentLocale);

        if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
            sdf = new SimpleDateFormat("сегодня в H:mm", currentLocale);

        } else if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
            sdf = new SimpleDateFormat("d MMM в H:mm", currentLocale);

        }

        return sdf.format(date);
    }




}
