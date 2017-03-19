package fr.usg.islamiclauncher.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.Toast;

import fr.usg.islamiclauncher.KissApplication;
import fr.usg.islamiclauncher.R;

public class ResetExcludedAppsPreference extends DialogPreference {

    public ResetExcludedAppsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        if (which == DialogInterface.BUTTON_POSITIVE) {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                    .putString("excluded-apps-list", getContext().getPackageName() + ";").commit();
            KissApplication.getDataHandler(getContext()).getAppProvider().reload();
            Toast.makeText(getContext(), R.string.excluded_app_list_erased, Toast.LENGTH_LONG).show();
        }

    }

}
