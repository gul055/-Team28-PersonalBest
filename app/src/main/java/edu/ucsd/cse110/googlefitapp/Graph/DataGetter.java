package edu.ucsd.cse110.googlefitapp.Graph;

import android.content.Context;
import android.util.Log;

import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

public class DataGetter {

    private Context context;

    public DataGetter(Context c) {
        context = c;
    }

    public float[][] get2DArrayData(String[] dateStrings, String email, String tag1, String tag2) {

        SharedPreferencesUtil util = new SharedPreferencesUtil();

        float[][] dataArray = new float[dateStrings.length][2];

        for (int i = 0; i < dateStrings.length; i++) {
            String key1 = dateStrings[i] + tag1;
            String key2 = dateStrings[i] + tag2;
            Log.d("First key being used", key1);
            Log.d("Second key being used", key2);
            Long dataPointX = util.loadLongByEmail(context, email, key1);
            dataArray[i][0] = dataPointX.floatValue();
            //dataArray[i][0] = SharedPreferencesUtil.loadLong(context, key1);
            Long dataPointY = util.loadLongByEmail(context, email, key2) - dataPointX;
            dataArray[i][1] = dataPointY.floatValue();
            Log.d("Adding data1", dataArray[i][0] + "");
            //dataArray[i][1] = SharedPreferencesUtil.loadInt(context, key2) - dataArray[i][0];
            Log.d("Adding data2", dataArray[i][1] + "");
        }

        return dataArray;
    }

    public float[] getArrayData(String[] dateStrings, String email, String tag) {

        SharedPreferencesUtil util = new SharedPreferencesUtil();

        float[] dataArray = new float[dateStrings.length];

        for (int i = 0; i < dateStrings.length; i++) {
            Long dataPoint = util.loadLongByEmail(context, email, dateStrings[i] + tag);
            dataArray[i] = dataPoint.floatValue();
            Log.d("Adding data point", dataArray[i] + "");
            //dataArray[i] = SharedPreferencesUtil.loadInt(context, dateStrings[i] + tag);
        }

        return dataArray;
    }
}
