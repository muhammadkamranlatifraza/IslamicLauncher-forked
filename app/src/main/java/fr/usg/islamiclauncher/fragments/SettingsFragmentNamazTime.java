package fr.usg.islamiclauncher.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;

import fr.usg.islamiclauncher.IslamicSettings;
import fr.usg.islamiclauncher.LocationActivity;
import fr.usg.islamiclauncher.R;
import fr.usg.islamiclauncher.appSettings.AppSettings;
import fr.usg.islamiclauncher.constants.TAGS;

/**
 * Created by Kamran on 11/15/2016.
 */

public class SettingsFragmentNamazTime extends Fragment implements TAGS {
    private static final String ARG_SECTION_NUMBER = "section_number";
    AppSettings prefs;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private ArrayList<String> listSettingName;
    private ArrayList<String> listSettingDescription;
    private ListView listView;

    public SettingsFragmentNamazTime() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SettingsFragmentNamazTime newInstance(int sectionNumber) {
        SettingsFragmentNamazTime fragment = new SettingsFragmentNamazTime();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void showArrayDialog(Context context, final String key, String[] array, int selected) {
        new AlertDialog.Builder(context).setTitle("Select")
                .setSingleChoiceItems(array, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (key.equals(TIME_ZONE)) {
                            prefs.putDouble(TIME_ZONE_DOUBLE, TIME_ZONE_DOUBLE_VALUES[i]);
                        }
                        prefs.putInt(key, i);
                        dialogInterface.cancel();
                    }
                })
                .show();
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

        listSettingName.add("Your City");
        listSettingDescription.add("Your City where you require Namaz Times");

        listSettingName.add("Calculation Method");
        listSettingDescription.add("Choose between several Calculation methods avaiable");

        listSettingName.add("Juristic method");
        listSettingDescription.add("Select your Fiqah for Namaz Times");

//        listSettingName.add("Time Zone");
//        listSettingDescription.add("Select time zone applied in your City");

        listSettingName.add("Time Format");
        listSettingDescription.add("Select Time format for Namaz Times");

//        listSettingName.add("Daylight Saving");
//        listSettingDescription.add("Takes You to First Run, Erases all data");

        listView.setAdapter(new EfficientAdapter(getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String key;
            String[] array;
            int selected;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        // location activity
                        IslamicSettings.doIt = false;
                        startActivity(new Intent(getActivity(), LocationActivity.class));
//                        new LocationDialog(getActivity()).show();
                        break;
                    case 1:
                        // CALCULATION_METHOD
                        key = CALCULATION_METHOD;
                        array = getActivity().getResources().getStringArray(R.array.calculation_methods);
                        selected = prefs.getInt(key);
                        showArrayDialog(getActivity(), key, array, selected);
                        break;
                    case 2:
                        // fiqah
                        key = FIQAH;
                        array = getActivity().getResources().getStringArray(R.array.juristic_method);
                        selected = prefs.getInt(key);
                        showArrayDialog(getActivity(), key, array, selected);
                        break;
                    case 3:
                        // time format
                        key = TIME_FORMAT;
                        array = getActivity().getResources().getStringArray(R.array.time_format);
                        selected = prefs.getInt(key);
                        showArrayDialog(getActivity(), key, array, selected);
                        break;
                    case 4:
                        // namaz names
                        key = NAMAZ_TIME_LANGUAGE;
                        array = getActivity().getResources().getStringArray(R.array.language_select);
                        selected = prefs.getInt(key);
                        showArrayDialog(getActivity(), key, array, selected);
                        break;
                    case 5:
                        // time zone
                        key = TIME_ZONE;
                        array = TIME_ZONE_ARRAY;
                        selected = prefs.getInt(key);
                        showArrayDialog(getActivity(), key, array, selected);
                        break;

                }
            }
        });
        return rootView;
    }

    public static class viewHolder {
        TextView settingName;
        TextView settingDescription;
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
            holder.settingName = (TextView) convertView
                    .findViewById(R.id.txt_setting_label);
            holder.settingDescription = (TextView) convertView
                    .findViewById(R.id.txt_setting_description);
            holder.settingName.setText(listSettingName.get(position));
            holder.settingDescription.setText(listSettingDescription.get(position));

            return convertView;
        }

    }
//
//    public class LocationDialog extends Dialog implements
//            android.view.View.OnClickListener {
//
//        PlacesAutocompleteTextView et_location;
//        Button donate;
//        double lat, lng;
//        String location = "";
//
//        public LocationDialog(Context context) {
//            super(context);
//
//        }
//
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            setContentView(R.layout.location_dialog);
//            et_location = (PlacesAutocompleteTextView) findViewById(R.id.et_donor_location);
//            et_location.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
//                @Override
//                public void onPlaceSelected(@NonNull final Place place) {
//                    et_location.getDetailsFor(place, new DetailsCallback() {
//                        @SuppressWarnings("ConstantConditions")
//                        @Override
//                        public void onSuccess(final PlaceDetails details) {
//                            lat = details.geometry.location.lat;
//                            lng = details.geometry.location.lng;
//
//                            for (AddressComponent component : details.address_components) {
//                                for (AddressComponentType type : component.types) {
//                                    switch (type) {
//                                        case STREET_NUMBER:
//                                            break;
//                                        case ROUTE:
//                                            break;
//                                        case NEIGHBORHOOD:
//                                            break;
//                                        case SUBLOCALITY_LEVEL_1:
//                                            break;
//                                        case SUBLOCALITY:
//                                            break;
//                                        case LOCALITY:
//                                            location += component.long_name;
//                                            break;
//                                        case ADMINISTRATIVE_AREA_LEVEL_1:
//                                            location += " " + component.long_name;
//                                            break;
//                                        case ADMINISTRATIVE_AREA_LEVEL_2:
//                                            break;
//                                        case COUNTRY:
//                                            location += " " + component.long_name;
//                                            break;
//                                        case POSTAL_CODE:
//                                            break;
//                                        case POLITICAL:
//                                            break;
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(final Throwable failure) {
//                            failure.printStackTrace();
//                        }
//                    });
//                }
//            });
//            donate = (Button) findViewById(R.id.btn_donate);
//            donate.setOnClickListener(this);
//        }
//
//
//        @Override
//        public void onClick(View view) {
//            prefs.putString(LOCATION, location);
//            prefs.putDouble(LAT, lat);
//            prefs.putDouble(LONG, lng);
//            prefs.putBoolean(CITY_SELECTED, true);
//            dismiss();
//        }
//    }
}