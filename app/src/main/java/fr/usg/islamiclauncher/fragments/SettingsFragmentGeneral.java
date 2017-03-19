package fr.usg.islamiclauncher.fragments;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.webianks.easy_feedback.EasyFeedback;

import java.util.ArrayList;

import fr.usg.islamiclauncher.IslamicSettings;
import fr.usg.islamiclauncher.R;
import fr.usg.islamiclauncher.Splash;
import fr.usg.islamiclauncher.appSettings.AppSettings;
import fr.usg.islamiclauncher.appSettings.Helper;
import fr.usg.islamiclauncher.constants.TAGS;

/**
 * Created by Kamran on 11/15/2016.
 */

public class SettingsFragmentGeneral extends Fragment implements TAGS {
    private static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private ArrayList<String> listSettingName;
    private ArrayList<String> listSettingDescription;
    private ListView listView;
    private AppSettings prefs;

    public SettingsFragmentGeneral() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SettingsFragmentGeneral newInstance(int sectionNumber) {
        SettingsFragmentGeneral fragment = new SettingsFragmentGeneral();
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
        prefs = new AppSettings(getActivity());

        listSettingName = new ArrayList<>();
        listSettingDescription = new ArrayList<>();
        listView = (ListView) rootView.findViewById(R.id.listViewSettings);

//        listSettingName.add("Color Scheme");
//        listSettingDescription.add("Select Color Scheme in Launcher");

        listSettingName.add("What's New");
        listSettingDescription.add("Check change log of features up to recent version");

        listSettingName.add("Reset to Default");
        listSettingDescription.add("This will take you to First Run");

        listSettingName.add("Suggest Improvements");
        listSettingDescription.add("You can Share your ideas with us");

        listSettingName.add("Rate app");
        listSettingDescription.add("Rate our app in Play Store");

        listSettingName.add("Share App");
        listSettingDescription.add("You can directly share Islamic Launcher to another device");

        listSettingName.add("Recommend App");
        listSettingDescription.add("You can spread knowledge of Quran, by recommending Islamic Launcher to your Friends");

        listSettingName.add("More apps");
        listSettingDescription.add("See More apps developed by us");

        listSettingName.add("Quick Into");
        listSettingDescription.add("See a quick intro of Islamic Launcher");

        listView.setAdapter(new EfficientAdapter());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String key;
            String[] array;
            int selected;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
//                    case 0:
//                        key = FONT_COLOR;
//                        array = getActivity().getResources().getStringArray(R.array.font_color);
//                        selected = -2; //prefs.getInt(key);
//                        new AlertDialog.Builder(getActivity()).setTitle("Select")
//                                .setSingleChoiceItems(array, selected, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        switch (i) {
//                                            case 0:
//                                                prefs.setWhiteFontColor();
//                                                break;
//                                            case 1:
//                                                prefs.setDarkFontColor();
//                                                break;
//                                        }
//                                        dialogInterface.cancel();
//                                    }
//                                })
//                                .show();
//                        break;
                    case 0:
                        Helper.showSuccessDialog(getActivity(), "1.0 - Jan 2017\nInitial Release");
//                        key = CONTACTS_FIX;
//                        array = getActivity().getResources().getStringArray(R.array.fix_contacts);
//                        selected = prefs.getInt(key);
//                        new AlertDialog.Builder(getActivity()).setTitle("Select")
//                                .setSingleChoiceItems(array, selected, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        prefs.putInt(key, i);
//                                        dialogInterface.cancel();
//                                    }
//                                })
//                                .show();
                        break;
                    case 1:// open Reset dialog
                        Helper.showResetAllDialog(getActivity());
                        break;
                    case 2:
//                        Helper.showEmailDialog(getActivity(), "Islamic Launcher : Suggesting Improvements");
                        IslamicSettings.doIt = false;
                        new EasyFeedback.Builder(getActivity()).withEmail(getString(R.string.app_email)).withSystemInfo().build().start();
                        break;
                    case 3:
                        rateApp();
                        break;
                    case 4:
                        sendAppItself();
                        break;
                    case 5:
                        recommendApp();
                        break;
                    case 6:
                        moreApps();
                        break;
                    case 7:
                        IslamicSettings.doIt = false;
                        startActivity(new Intent(getActivity(), Splash.class));
                        break;
                }
            }
        });

        return rootView;
    }

    public void moreApps() {
        String devID = "6999883613235574792"; // Actual ID
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://dev?id=" + devID)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=" + devID)));
        }
    }

    public void rateApp() {
        final String appPackageName = getActivity().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void recommendApp() {
        final String appPackageName = getActivity().getPackageName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
        String sAux = RECOMMEND_APP;
        sAux = sAux + "https://play.google.com/store/apps/details?id=" + appPackageName;
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(i, "choose one"));
    }

    public void sendAppItself() {
        PackageManager pm = getActivity().getPackageManager();
        ApplicationInfo appInfo;
        try {
            appInfo = pm.getApplicationInfo(getActivity().getPackageName(),
                    PackageManager.GET_META_DATA);
            Intent sendBt = new Intent(Intent.ACTION_SEND);
            sendBt.setType("*/*");
            sendBt.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse("file://" + appInfo.publicSourceDir));

            getActivity().startActivity(Intent.createChooser(sendBt,
                    "Share APK using"));
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private static class viewHolder {
        TextView tname;
        TextView tcode;
    }

    private class EfficientAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public EfficientAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {
            return listSettingDescription.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_settings, null);
                holder = new viewHolder();
                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }
            holder.tname = (TextView) convertView.findViewById(R.id.txt_setting_label);
            holder.tcode = (TextView) convertView.findViewById(R.id.txt_setting_description);
            holder.tname.setText(listSettingName.get(position));
            holder.tcode.setText(listSettingDescription.get(position));
            return convertView;
        }

    }
}
