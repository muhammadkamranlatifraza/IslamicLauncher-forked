package fr.usg.islamiclauncher.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.usg.islamiclauncher.R;
import fr.usg.islamiclauncher.constants.TAGS;


/**
 * Created by Kamran on 11/15/2016.
 */

public class SettingsFragmentAbout extends Fragment implements TAGS {
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    public SettingsFragmentAbout() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SettingsFragmentAbout newInstance(int sectionNumber) {
        SettingsFragmentAbout fragment = new SettingsFragmentAbout();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fargment_about, container, false);
    }
}
