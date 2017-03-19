package fr.usg.islamiclauncher.loader;

import android.content.Context;

import java.util.ArrayList;

import fr.usg.islamiclauncher.dataprovider.PhoneProvider;
import fr.usg.islamiclauncher.pojo.PhonePojo;

public class LoadPhonePojos extends LoadPojos<PhonePojo> {

    public LoadPhonePojos(Context context) {
        super(context, PhoneProvider.PHONE_SCHEME);
    }

    @Override
    protected ArrayList<PhonePojo> doInBackground(Void... params) {
        return null;
    }
}
