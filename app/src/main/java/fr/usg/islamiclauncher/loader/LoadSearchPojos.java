package fr.usg.islamiclauncher.loader;

import android.content.Context;

import java.util.ArrayList;

import fr.usg.islamiclauncher.pojo.SearchPojo;

public class LoadSearchPojos extends LoadPojos<SearchPojo> {

    public LoadSearchPojos(Context context) {
        super(context, "none://");
    }

    @Override
    protected ArrayList<SearchPojo> doInBackground(Void... params) {
        return null;
    }
}
