package fr.usg.islamiclauncher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import fr.usg.islamiclauncher.appSettings.AppSettings;
import fr.usg.islamiclauncher.constants.TAGS;
import fr.usg.islamiclauncher.fragments.SettingsFragmentAbout;
import fr.usg.islamiclauncher.fragments.SettingsFragmentGeneral;
import fr.usg.islamiclauncher.fragments.SettingsFragmentNamazTime;
import fr.usg.islamiclauncher.fragments.SettingsFragmentQuran;


public class IslamicSettings extends AppCompatActivity implements TAGS {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private AppSettings prefs;
    private Context context;
    public static boolean doIt = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_islamic_settings);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5485158965162894~5245396560");
        context = this;
        prefs = new AppSettings(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("0F10741D5AAB3090D21A5BC3E9089B9E")
                .build();
        mAdView.loadAd(adRequest);

        // Create the appsAdapter that will return a fragment for each of the three
        // primary sections of the activity.

//        float f = getResources().getDisplayMetrics().density;

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections appsAdapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(getIntent().getIntExtra("OPEN", 0));
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_settings, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
/*
    public static class PlaceholderFragment extends Fragment {
        *//**
     * The fragment argument representing the section number for this
     * fragment.
     *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        *//**
     * Returns a new instance of this fragment for the given section
     * number.
     *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
    */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return SettingsFragmentGeneral.newInstance(position + 1);
                case 1:
                    return SettingsFragmentQuran.newInstance(position + 1);
                case 2:
                    return SettingsFragmentNamazTime.newInstance(position + 1);
                case 3:
                    return SettingsFragmentAbout.newInstance(position + 1);
                default:
                    return SettingsFragmentGeneral.newInstance(position + 1);
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "General";
                case 1:
                    return "Quran";
                case 2:
                    return "Namaz Time";
                case 3:
                    return "About";
                default:
                    return "Settings";
            }
        }
    }

    boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
//            super.onBackPressed(); // default action.....
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(context, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            // new process handler after 2 seconds running a new thread to false
            // the exit variable again.......
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2 * 1000);
        }
    }
    @Override
    protected void onStop() {
        setResult(RESULT_OK);
        super.onStop();
    }
}
