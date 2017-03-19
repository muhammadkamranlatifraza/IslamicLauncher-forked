package fr.usg.islamiclauncher;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.usg.islamiclauncher.appSettings.AppSettings;
import fr.usg.islamiclauncher.appSettings.Helper;
import fr.usg.islamiclauncher.constants.TAGS;

public class LocationActivity extends AppCompatActivity implements TAGS {

    final Context context = this;
    ArrayAdapter<String> adapter;
    EditText et_location;
    Button btn_location_search;
    ListView listView;
    List<Address> list;
    ArrayList<String> citiesList;
    AppSettings prefs;
    Calendar calendar = Calendar.getInstance();
    Geocoder geocoder;
    String textFromUser = "";
    MyCountDownTimer timer;


    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

//
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5485158965162894~5245396560");
//
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // setting Year
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 10);

        prefs = new AppSettings(context);

        et_location = (EditText) findViewById(R.id.et_current_location);
        et_location.setMaxLines(1);
        btn_location_search = (Button) findViewById(R.id.btn_search);
        listView = (ListView) findViewById(R.id.listView);

        btn_location_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Helper.hideSoftKeybord(context, v);
                if (!Helper.isNetworkAvailable(context)) {
                    Helper.showErrorDialog(context, "No Internet Connection");
                } else {
                    if (et_location.getText().toString().length() > 2) {
                        textFromUser = et_location.getText().toString();
                        timer = (MyCountDownTimer) new MyCountDownTimer(60000, 1000, new GeoCodeLocation()).start();
                    } else {
                        et_location.setError(getResources().getString(
                                R.string.error_location));
                    }
                }

            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id) {

                timer.cancel();
                String city = list.get(position).getLocality();
                Double lat = list.get(position).getLatitude();
                Double lon = list.get(position).getLongitude();

//                TAGS.LOCATION = city;
//                TAGS.CHANNEL = prefs.getString(TAGS.TAG_BLOOD_GROUP) + " "
//                        + city;
                IslamicSettings.doIt = true;

                prefs.putString(LOCATION, city);
                prefs.putDouble(LAT, lat);
                prefs.putDouble(LONG, lon);
                prefs.putBoolean(CITY_SELECTED, true);
                finish();
//
//                Intent intent = new Intent(context,
//                        MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                //                String gcm_sender_id = context
//                        .getString(R.string.gcm_sender_id);

//                Toast.makeText(context, TAGS.CHANNEL, Toast.LENGTH_SHORT)
//                        .show();
//                ArrayList<String> list = new ArrayList<String>();
//
//                list.add("default");
//                list.add(city);
//                list.add(prefs.getString(TAGS.TAG_CHANNEL));

//                Messaging.DEVICE_ID = Secure.getString(
//                        context.getContentResolver(), Secure.ANDROID_ID);

//                Backendless.Messaging.registerDevice(gcm_sender_id, list,
//                        calendar.getTime(), new AsyncCallback<Void>() {
//
//                            @Override
//                            public void handleResponse(Void arg0) {
//                                Helper.progressDialog.cancel();
//                                Intent intent = new Intent(context,
//                                        InboxActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                            }
//
//                            @Override
//                            public void handleFault(BackendlessFault fault) {
//                                Helper.progressDialog.cancel();
//                                Helper.showErrorDialog(
//                                        context,
//                                        "Network Error occurred while registering you, please Try Again");
//                            }
//                        });

            }
        });

    } // onCreate() ends

    @Override
    public void onBackPressed() {
        // coming from Main Activity so no need to stop on back pressing
        super.onBackPressed(); // default action.....
//        if (!prefs.getString(TAGS.TAG_LOCATION).equals("")
//                || prefs.getString(TAGS.TAG_CHANNEL).equals("")) {
//        } else {
//            if (exit) {
//                super.onBackPressed(); // default action.....
//            } else {
//                Toast.makeText(this, "Press Back again to Exit.",
//                        Toast.LENGTH_SHORT).show();
//                exit = true;
//                // new process handler after 2 seconds running a new thread to
//                // false
//                // the exit variable again.......
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        exit = false;
//                    }
//                }, 2 * 1000);
//
//            }
//
//        }
    }

    private class GeoCodeLocation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Helper.showProgressDialog(context);
        }

        @Override
        protected String doInBackground(String... params) {

            geocoder = new Geocoder(context, Locale.getDefault());
            try {
                list = geocoder.getFromLocationName(textFromUser, 5);
                citiesList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    String listitem = list.get(i).getLocality() + " "
                            + list.get(i).getAdminArea() + " "
                            + list.get(i).getCountryName();
                    if (!listitem.contains("null")) {
                        citiesList.add(listitem);
                    } else {
                        list.remove(i);
                    }
                } // loop ending here
            } catch (IOException e) {
                e.printStackTrace();
                cancel(true);
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Helper.progressDialog.cancel();
            if (citiesList.size() == 0) {
                Helper.showErrorDialog(context,
                        getResources().getString(R.string.error_keywords));
            }
            adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1, citiesList);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Helper.progressDialog.cancel();
            timer.cancel();
            Helper.showErrorDialog(context,
                    getResources().getString(R.string.error_location_message_try_again));
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }


    public class MyCountDownTimer extends CountDownTimer {


        MyCountDownTimer(long millisInFuture, long countDownInterval, GeoCodeLocation geoCodeLocation) {
            super(millisInFuture, countDownInterval);
            geoCodeLocation.execute();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }


        @Override
        public void onFinish() {
            finish();
            if (citiesList.size() == 0) {
                Helper.progressDialog.cancel();
                Helper.showErrorDialog(context,
                        getResources().getString(R.string.error_time_out_for_location_searach));
            }
        }
    }

}