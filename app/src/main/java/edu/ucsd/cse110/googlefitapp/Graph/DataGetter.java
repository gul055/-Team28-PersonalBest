package edu.ucsd.cse110.googlefitapp.Graph;

import android.content.Context;
import android.util.Log;

import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

public class DataGetter {

    private Context context;

    public DataGetter(Context c) {
        context = c;
    }

    public float[][] get2DArrayData(String[] dateStrings, String tag1, String tag2) {

        float[][] dataArray = new float[dateStrings.length][2];

        for (int i = 0; i < dateStrings.length; i++) {
            String key1 = dateStrings[i] + tag1;
            String key2 = dateStrings[i] + tag2;
            Log.d("First key being used", key1);
            Log.d("Second key being used", key2);
            dataArray[i][0] = SharedPreferencesUtil.loadInt(context, key1);
            Log.d("Adding data1", dataArray[i][0] + "");
            dataArray[i][1] = SharedPreferencesUtil.loadInt(context, key2) - dataArray[i][0];
            Log.d("Adding data2", dataArray[i][1] + "");
        }

        return dataArray;
    }

    public float[] getArrayData(String[] dateStrings, String tag) {

        float[] dataArray = new float[dateStrings.length];

        for (int i = 0; i < dateStrings.length; i++) {
            dataArray[i] = SharedPreferencesUtil.loadLong(context, dateStrings[i]);
        }

        return dataArray;
    }
}
