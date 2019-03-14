package edu.ucsd.cse110.googlefitapp;

import android.content.Context;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData.DataService;
import edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData.ReceiveData;
import edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData.SendData;
import edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData.StepDataAdapter;
import edu.ucsd.cse110.googlefitapp.Utils.GoogleUserUtil;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class DataTest {

    Context context;
    ReceiveData receiveData;
    SendData sendData;
    GoogleUserUtil gUtil;
    SharedPreferencesUtil sharedPref;

    // Will anyone get these references
    String memeEmailSend = "mushipan@naritai.no";
    String memeEmailReceive = "movie@strong-dattebayo.jp";

    @Before
    public void setUp() throws Exception {
        context = getInstrumentation().getTargetContext();
        //FirebaseApp.initializeApp(context);
        gUtil = mock(GoogleUserUtil.class);
    }

    @Test
    public void sendData() throws Exception {

        sharedPref = mock(SharedPreferencesUtil.class);

        // Will someone get THIS reference? :eyes:
        when(gUtil.getEmail(context)).thenReturn(memeEmailSend);
        when(sharedPref.loadLongByEmail(context, memeEmailSend, "2019-8-8recorded_steps")).thenReturn(888L);

        DataService service = mock(StepDataAdapter.class);

        // Do not do any real sending
        when(service.addData(any(HashMap.class))).thenReturn(null);

        sendData = new SendData(context, gUtil, sharedPref, service);

        // Call the fake send
        sendData.SendLong("2019-8-8recorded_steps");

        // Check if the addData was actually called
        // Also snag the HashMap
        ArgumentCaptor<HashMap<String, Object>> captor = ArgumentCaptor.forClass(HashMap.class);
        verify(service).addData(captor.capture());

        // Check that contents of the HashMap are correct
        assertEquals(888L, captor.getValue().get("2019-8-8recorded_steps"));
    }

    @Test
    public void receiveData() throws Exception {

        DataService service = mock(StepDataAdapter.class);
        sharedPref = new SharedPreferencesUtil();

        receiveData = new ReceiveData(context, memeEmailReceive, sharedPref, service);

        when(service.getLong("2019-7-7recorded_steps")).thenReturn(777L);
        when(service.getLong("2019-7-7goal")).thenReturn(7777L);
        when(service.getLong("2019-7-7total_steps")).thenReturn(77777L);

        Long recordSteps = receiveData.receiveLong("2019-7-7recorded_steps");
        Long goalSteps = receiveData.receiveLong("2019-7-7goal");
        Long totalSteps = receiveData.receiveLong("2019-7-7total_steps");
        assertEquals(777L, sharedPref.loadLongByEmail(context, memeEmailReceive, "2019-7-7recorded_steps"));
        assertEquals(7777L, sharedPref.loadLongByEmail(context, memeEmailReceive, "2019-7-7goal"));
        assertEquals(77777L, sharedPref.loadLongByEmail(context, memeEmailReceive, "2019-7-7total_steps"));
        assertEquals(777L, (long) recordSteps);
        assertEquals(7777L, (long) goalSteps);
        assertEquals(77777L, (long) totalSteps);
    }
}
