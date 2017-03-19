package fr.usg.islamiclauncher.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import fr.usg.islamiclauncher.appSettings.AppSettings;
import fr.usg.islamiclauncher.constants.TAGS;

/**
 * Created by Kamran on 11/15/2016.
 */

public class SettingsFragmentQuran extends Fragment implements TAGS {
    private static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private ArrayList<String> listSettingName;
    private ArrayList<String> listSettingDescription;
    private ListView listView;
    private AppSettings prefs;

    public SettingsFragmentQuran() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SettingsFragmentQuran newInstance(int sectionNumber) {
        SettingsFragmentQuran fragment = new SettingsFragmentQuran();
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
        listSettingName = new ArrayList<>();
        listSettingDescription = new ArrayList<>();
        prefs = new AppSettings(getActivity());

        listView = (ListView) rootView.findViewById(R.id.listViewSettings);

        listSettingName.add("Ayah Display Format");
        listSettingDescription.add("Select Format in which Ayahs should be displayed");

        listSettingName.add("Font Size Arabic");
        listSettingDescription.add("Select Font Size for Arabic Text");

        listSettingName.add("Font Size Transliteration");
        listSettingDescription.add("Select Font Size for Transliteration Text");

        listSettingName.add("Font Size Translation");
        listSettingDescription.add("Select Font Size for Translation Text");

        listSettingName.add("Font Selection");
        listSettingDescription.add("Select the type of font available for Quran display");

        listSettingName.add("Report Mistake");
        listSettingDescription.add("Report a mistake with reference, we will look into it as soon as possible");
//        listSettingName.add("Your Blood Group");
//        listSettingDescription.add(prefs.getString(TAGS.TAG_BLOOD_GROUP));
//        listSettingName.add("Your City");
//        listSettingDescription.add(prefs.getString(TAGS.TAG_LOCATION));

        listView.setAdapter(new EfficientAdapter(getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String key;
            String[] array;
            int selected;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        key = AYAH_FORMAT;
                        array = getActivity().getResources().getStringArray(R.array.ayah_format_display);
                        selected = prefs.getInt(key);
                        showArrayDialog(getActivity(), key, array, selected);
                        break;
                    case 1:
                        key = FONT_SIZE_ARABIC;
                        array = getActivity().getResources().getStringArray(R.array.font_size);
                        selected = prefs.getInt(key);
                        showArrayDialog(getActivity(), key, array, selected);
                        break;
                    case 2:
                        key = FONT_SIZE_TRANSLITRATION;
                        array = getActivity().getResources().getStringArray(R.array.font_size);
                        selected = prefs.getInt(key);
                        showArrayDialog(getActivity(), key, array, selected);
                        break;
                    case 3:
                        key = FONT_SIZE_TRANSLATION;
                        array = getActivity().getResources().getStringArray(R.array.font_size);
                        selected = prefs.getInt(key);
                        showArrayDialog(getActivity(), key, array, selected);
                        break;
                    case 4:
                        key = FONT_TYPE;
                        array = getActivity().getResources().getStringArray(R.array.font_type);
                        selected = prefs.getInt(key);
                        showArrayDialog(getActivity(), key, array, selected);
                        break;
                    case 5:
                        IslamicSettings.doIt = false;
//                        Helper.showEmailDialog(getActivity(), "Islamic Launcher : Reporting Mistake");
                        new EasyFeedback.Builder(getActivity()).withEmail(getString(R.string.app_email)).withSystemInfo().build().start();
                        break;
                }
            }
        });
        return rootView;
    }

    public void showArrayDialog(Context context, final String key, String[] array, int selected) {
        new AlertDialog.Builder(context).setTitle("Select")
                .setSingleChoiceItems(array, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        prefs.putInt(key, i);
                        dialogInterface.cancel();
                    }
                })
                .show();
    }

    public static class viewHolder {
        TextView tname;
        TextView tcode;
    }

    private class EfficientAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public EfficientAdapter(Context context) {
            inflater = LayoutInflater.from(context);

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
            holder.tname = (TextView) convertView
                    .findViewById(R.id.txt_setting_label);
            holder.tcode = (TextView) convertView
                    .findViewById(R.id.txt_setting_description);
            holder.tname.setText(listSettingName.get(position));
            holder.tcode.setText(listSettingDescription.get(position));

            return convertView;
        }

    }
}
