package fr.usg.islamiclauncher.db;

/**
 * Created by Kamran on 12/24/2016.
 */


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

import fr.usg.islamiclauncher.constants.TAGS;
import fr.usg.islamiclauncher.object.AyahObject;

public class QuranDatabase extends SQLiteAssetHelper implements TAGS {

    public QuranDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
    }

    public ArrayList<AyahObject> getText(int index) {
        ArrayList<AyahObject> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"arabic", "translitration", "translation"};
        String table = "data";
        qb.setTables(table);
        int ayahStart = SURAH_AYAH_NUMBERS[index];
        int ayahEnd = SURAH_AYAH_NUMBERS[index + 1];
        qb.appendWhere("id >= " + ayahStart + " AND id < " + ayahEnd);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
        c.moveToFirst();
        do {
            AyahObject ayah = new AyahObject();
            ayah.setArabic(c.getString(0));
            ayah.setTranslitration(c.getString(1));
            ayah.setTranslation(c.getString(2));
            list.add(ayah);
        } while (c.moveToNext());
        return list;

    }

//    public ArrayList<AyahObject> getText() {
//        ArrayList<AyahObject> list = new ArrayList<>();
//        SQLiteDatabase db = getReadableDatabase();
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//        String[] sqlSelect = {"arabic", "translitration", "translation"};
//        String table = "data";
//        qb.setTables(table);
//        qb.appendWhere("id >= 1245 AND id < 1374");
//        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
//        c.moveToFirst();
//        do {
//            AyahObject ayah = new AyahObject();
//            ayah.setArabic(c.getString(0));
//            ayah.setTranslitration(c.getString(1));
//            ayah.setTranslation(c.getString(2));
//            list.add(ayah);
//        } while (c.moveToNext());
//        return list;
//
//    }

}
