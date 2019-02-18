package edu.ucsd.cse110.googlefitapp.fitness;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import edu.ucsd.cse110.googlefitapp.utils.SharedPreferencesUtil;
import edu.ucsd.cse110.googlefitapp.StepCountActivity;

public class GoogleFitAdapter implements FitnessService {
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final String TAG = "GoogleFitAdapter";

    private StepCountActivity activity;

    public GoogleFitAdapter(StepCountActivity activity) {
        this.activity = activity;
    }


    public void setup() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(activity), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(activity),
                    fitnessOptions);
        } else {
            updateStepCount();
            startRecording();
        }
    }

    private void startRecording() {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(activity);
        if (lastSignedInAccount == null) {
            return;
        }

        Fitness.getRecordingClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully subscribed!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "There was a problem subscribing.");
                    }
                });
    }


    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
     */

    public void updateStepCount() {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(activity);
        if (lastSignedInAccount == null) {
            return;
        }


        Fitness.getHistoryClient(activity, lastSignedInAccount)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @TargetApi(Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                Date date = new Date();
                                DateFormat dateFormat = DateFormat.getDateInstance();
                                dateFormat.format(date.getTime());
                                Log.d(TAG, dataSet.toString());
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                                activity.setStepCount(total);
                                Log.d(TAG, "Total steps: " + total);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }

    /*ALL CREDIT FOR THE FOLLOWING GETWEEKLYDATA CODE GOES TO THE TUTSPLUS TUTORIAL ON GOOGLE FIT API
    Title: Google Fit for Android: History API
    https://code.tutsplus.com/tutorials/google-fit-for-android-history-api--cms-25856
    Captured: 2/15/2019
    How the source was used: Copied code
    K.D.
     */

    public void getWeeklyData() {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(activity);
        if(lastSignedInAccount == null){
            return;
        }

        Log.d("BEFORE CALENDAR", "");
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        Log.d("FIRST DAY", String.valueOf(calendar.getFirstDayOfWeek()));

        int sundayTodayDiff = Calendar.SUNDAY - Calendar.DAY_OF_WEEK;

        calendar.add(Calendar.DAY_OF_WEEK,sundayTodayDiff);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();
        Log.d("HISTORY START", calendar.getTime().toString());

        calendar.add(Calendar.DAY_OF_WEEK, Math.abs(sundayTodayDiff));
        calendar.add(Calendar.SECOND, -1);
        long endTime = calendar.getTimeInMillis();
        Log.d("HISTORY STOP", calendar.getTime().toString());

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Fitness.getHistoryClient(activity, lastSignedInAccount)
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>(){
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                List<Bucket> buckets = dataReadResponse.getBuckets();
                                Log.d("History", "Number of returned DataSets: " + buckets.size());
                                for(Bucket bucket : buckets) {
                                    List<DataSet> dataSets = bucket.getDataSets();
                                    for(DataSet dataSet : dataSets) {
                                        Log.d("History", "Data returned for Data type: " + dataSet.getDataType().getName());
                                        DateFormat dateFormat = DateFormat.getDateInstance();
                                        DateFormat timeFormat = DateFormat.getTimeInstance();

                                        for (DataPoint dp : dataSet.getDataPoints()) {

                                            String startTimeString = dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS));
                                            Log.d("History", "Data point:");
                                            Log.d("History", "\tType: " + dp.getDataType().getName());
                                            Log.d("History", "\tStart: " + startTimeString + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                                            Log.d("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                                            for(Field field : dp.getDataType().getFields()) {
                                                int steps = dp.getValue(field).asInt();
                                                Log.d("History", "\tField: " + field.getName() +
                                                        " Value: " + dp.getValue(field));

                                                Calendar cal = Calendar.getInstance();
                                                cal.setTimeInMillis(dp.getStartTime(TimeUnit.MILLISECONDS));
                                                //TODO: REPLACE WITH STRING BUILDER.
                                                String year = String.valueOf(cal.get(Calendar.YEAR));
                                                String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                                                String month = String.valueOf(cal.get(Calendar.MONTH)+ 1);
                                                String date = year + "-" + month + "-" + day;
                                                String key = date + "total_steps";
                                                Log.d("KEY_BUILT", key);
                                                SharedPreferencesUtil.saveInt(activity.getApplicationContext(), key, steps);
                                            }

                                        }
                                    }
                                }
                            }
                        }
                );
    }

    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }
}
