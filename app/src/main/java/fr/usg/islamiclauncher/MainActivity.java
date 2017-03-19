package fr.usg.islamiclauncher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

import fr.usg.islamiclauncher.adapter.RecordAdapter;
import fr.usg.islamiclauncher.appSettings.AppSettings;
import fr.usg.islamiclauncher.appSettings.Helper;
import fr.usg.islamiclauncher.broadcast.IncomingCallHandler;
import fr.usg.islamiclauncher.broadcast.IncomingSmsHandler;
import fr.usg.islamiclauncher.constants.Names;
import fr.usg.islamiclauncher.constants.TAGS;
import fr.usg.islamiclauncher.dataprovider.AppProvider;
import fr.usg.islamiclauncher.db.QuranDatabase;
import fr.usg.islamiclauncher.library.AppDetail;
import fr.usg.islamiclauncher.library.DigitalClock;
import fr.usg.islamiclauncher.library.PrayTime;
import fr.usg.islamiclauncher.object.AyahObject;
import fr.usg.islamiclauncher.pojo.Pojo;
import fr.usg.islamiclauncher.result.Result;
import fr.usg.islamiclauncher.searcher.ApplicationsSearcher;
import fr.usg.islamiclauncher.searcher.HistorySearcher;
import fr.usg.islamiclauncher.searcher.NullSearcher;
import fr.usg.islamiclauncher.searcher.QueryInterface;
import fr.usg.islamiclauncher.searcher.QuerySearcher;
import fr.usg.islamiclauncher.searcher.Searcher;
import fr.usg.islamiclauncher.ui.BlockableListView;
import fr.usg.islamiclauncher.ui.BottomPullEffectView;
import fr.usg.islamiclauncher.ui.KeyboardScrollHider;
import fr.usg.islamiclauncher.utils.PackageManagerUtils;
import hollowsoft.slidingdrawer.OnDrawerCloseListener;
import hollowsoft.slidingdrawer.OnDrawerOpenListener;
import hollowsoft.slidingdrawer.OnDrawerScrollListener;
import hollowsoft.slidingdrawer.SlidingDrawer;

import static android.support.annotation.Dimension.SP;

public class MainActivity extends Activity implements TAGS, QueryInterface, KeyboardScrollHider.KeyboardHandler, OnDrawerScrollListener, OnDrawerOpenListener, OnDrawerCloseListener {

    Context context = this;
    SlidingDrawer drawer;

    public static final String START_LOAD = "fr.neamar.summon.START_LOAD";
    public static final String LOAD_OVER = "fr.neamar.summon.LOAD_OVER";
    public static final String FULL_LOAD_OVER = "fr.neamar.summon.FULL_LOAD_OVER";
    /**
     * InputType that behaves as if the consuming IME is a standard-obeying
     * soft-keyboard
     * <p>
     * *Auto Complete* means "we're handling auto-completion ourselves". Then
     * we ignore whatever the IME thinks we should display.
     */
    private final static int INPUT_TYPE_STANDARD = InputType.TYPE_CLASS_TEXT
            | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
    /**
     * InputType that behaves as if the consuming IME is SwiftKey
     * <p>
     * *Visible Password* fields will break many non-Latin IMEs and may show
     * unexpected behaviour in numerous ways. (#454, #517)
     */
    private final static int INPUT_TYPE_WORKAROUND = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
    private static final String TAG = "MainActivity";
    /**
     * IDs for the favorites buttons
     */
    private final int[] favsIds = new int[]{R.id.favorite0, R.id.favorite1, R.id.favorite2, R.id.favorite3, R.id.favorite4, R.id.favorite5};
    /**
     * IDs for the favorites buttons on the quickbar
     */
    private final int[] favBarIds = new int[]{R.id.favoriteBar0, R.id.favoriteBar1, R.id.favoriteBar2, R.id.favoriteBar3, R.id.favoriteBar4, R.id.favoriteBar5};
    /**
     * Number of favorites to retrieve.
     * We need to pad this number to account for removed items still in history
     */
    public final int tryToRetrieve = favsIds.length + 2;
    /**
     * Adapter to display records
     */
    public RecordAdapter adapter;
    /**
     * Store user preferences
     */
    private SharedPreferences prefs;
    private BroadcastReceiver mReceiver;
    /**
     * View for the Search text
     */
    private EditText searchEditText;
    private final Runnable displayKeyboardRunnable = new Runnable() {
        @Override
        public void run() {
            showKeyboard();
        }
    };
    /**
     * Whether or not Search text should be spell checked (affects inputType)
     */
    private boolean searchEditTextWorkaround;
    /**
     * Main list view
     */
    private ListView list;
    private View listContainer;
    /**
     * View to display when list is empty
     */
    private View listEmpty;
    /**
     * Utility for automatically hiding the keyboard when scrolling down
     */
    private KeyboardScrollHider hider;
    /**
     * Menu button
     */
    private View menuButton;
    /**
     * Kiss bar
     */
    private View kissBar;
    /**
     * Favorites bar, in the KISS bar (not the quick favorites bar from minimal UI)
     */
    private View favoritesKissBar;

    /**
     * Task launched on text change
     */
    private Searcher searcher;

    AppSettings appSettings;
    QuranDatabase db;
    ListView listView;
    ArrayList<AyahObject> ayahlist;
    TextView surahName;
    Typeface tf;
    EfficientAdapter efficientAdapter;
    int index = 1;
    ImageButton btn_next, btn_previous, btn_bookmark, btn_bookmark_list;

    AdView mAdView;
    String InterstitialAdID = getString(R.string.inters_ad_unit_id);
    InterstitialAd mInterstitialAd;

    Handler handler;
    TextView tv, txt_main, txt_meaning, txt_transliteration; //, txt_explaination;
    Runnable r;
    LinearLayout timeLayout;
    DigitalClock clock;
    GridView gridView;
    RelativeLayout layout;
    TextView txt_city;
    ImageView handle;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initialize UI
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        appSettings = new AppSettings(context);
        String theme = prefs.getString("theme", "light");
        switch (theme) {
            case "light":
                appSettings.setDarkFontColor();
                break;
            case "dark":
                setTheme(R.style.AppThemeDark);
                appSettings.setWhiteFontColor();
                break;
            case "transparent":
                setTheme(R.style.AppThemeTransparent);
                appSettings.setDarkFontColor();

                break;
            case "semi-transparent":
                setTheme(R.style.AppThemeSemiTransparent);
                appSettings.setDarkFontColor();

                break;
            case "semi-transparent-dark":
                setTheme(R.style.AppThemeSemiTransparentDark);
                appSettings.setWhiteFontColor();

                break;
            case "transparent-dark":
                setTheme(R.style.AppThemeTransparentDark);
                appSettings.setWhiteFontColor();
                break;
        }
        super.onCreate(savedInstanceState);

        Log.d("ON CREATE", "ON CREATE");

        IntentFilter intentFilter = new IntentFilter(START_LOAD);
        IntentFilter intentFilterBis = new IntentFilter(LOAD_OVER);
        IntentFilter intentFilterTer = new IntentFilter(FULL_LOAD_OVER);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase(LOAD_OVER)) {
                    updateRecords(searchEditText.getText().toString());
                } else if (intent.getAction().equalsIgnoreCase(FULL_LOAD_OVER)) {
                    // Run GC once to free all the garbage accumulated during provider initialization
                    System.gc();

                    displayQuickFavoritesBar(true, false);
                    displayLoader(false);

                } else if (intent.getAction().equalsIgnoreCase(START_LOAD)) {
                    displayLoader(true);
                }
            }
        };

        this.registerReceiver(mReceiver, intentFilter);
        this.registerReceiver(mReceiver, intentFilterBis);
        this.registerReceiver(mReceiver, intentFilterTer);
        KissApplication.initDataHandler(this);

        // Initialize preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Lock launcher into portrait mode
        // Do it here (before initializing the view) to make the transition as smooth as possible
        if (prefs.getBoolean("force-portrait", true)) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }

        setContentView(R.layout.main);

        this.list = (ListView) this.findViewById(android.R.id.list);
        this.listContainer = (View) this.list.getParent();
        this.listEmpty = this.findViewById(android.R.id.empty);
        drawer = (SlidingDrawer) findViewById(R.id.drawer);
        drawer.setOnDrawerCloseListener(this);
        drawer.setOnDrawerOpenListener(this);
        drawer.setOnDrawerScrollListener(this);
        //        drawer
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest requestBanner = new AdRequest.Builder().build();
        mAdView.loadAd(requestBanner);

        tv = (TextView) findViewById(R.id.tvFragFirst);
        txt_main = (TextView) findViewById(R.id.txt_main);
        txt_main.setTextColor(appSettings.getFontColor() ? Color.BLACK : Color.WHITE);
        txt_meaning = (TextView) findViewById(R.id.txt_meaning);
        txt_meaning.setTextColor(appSettings.getFontColor() ? Color.BLACK : Color.WHITE);
        txt_transliteration = (TextView) findViewById(R.id.txt_transliteration);
        txt_transliteration.setTextColor(appSettings.getFontColor() ? Color.BLACK : Color.WHITE);
        handle = (ImageView) findViewById(R.id.handle);
//        txt_explaination = (TextView) findViewById(R.id.txt_explaination);
//        txt_explaination.setTextColor(appSettings.getFontColor() ? Color.BLACK : Color.WHITE);
        txt_city = (TextView) findViewById(R.id.city_text);
        txt_city.setTextColor(appSettings.getFontColor() ? Color.BLACK : Color.WHITE);
        txt_city.setText(appSettings.getBoolean(CITY_SELECTED) ? appSettings.getString(LOCATION) : "Adjust Namaz Time Settings");
        handle.setBackgroundResource(appSettings.getFontColor()
                ? R.drawable.background_handle_dark : R.drawable.background_handle_white);
        Calendar mCalendar = Calendar.getInstance();
        int thisMonthDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int thisDay = mCalendar.get(Calendar.DAY_OF_WEEK);
        int thisMonth = mCalendar.get(Calendar.MONTH);

        String date = nameOfDays[--thisDay] + " " + monthsNames[thisMonth] + " " + thisMonthDay;
        tv.setText(date);
        tv.setTextColor(appSettings.getFontColor() ? Color.BLACK : Color.WHITE);

        timeLayout = (LinearLayout) findViewById(R.id.timeLayout);
        clock = (DigitalClock) findViewById(R.id.textClock);
        clock.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), String.format("fonts/%s", "digital_7.ttf")));
        gridView = (GridView) findViewById(R.id.namaz_list);
        clock.setTextColor(appSettings.getFontColor() ? Color.BLACK : Color.WHITE);
        prepareNamazTiming();
        handler = new Handler();
        r = new Runnable() {
            public void run() {
                Names n = new Names();
                try {
                    JSONArray array = new JSONArray(n.array);
                    JSONObject object = array.getJSONObject(new Random().nextInt(98));
                    txt_main.setText(object.getString("Name"));
                    txt_meaning.setText(object.getString("Meaning"));
                    txt_transliteration.setText(object.getString("Transliteration"));
//                    txt_explaination.setText(object.getString("Explanation"));

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Log.d("IN THREAD ERROR", "IN THREAD ERROR");
                }
            }
        };
        handler.postDelayed(r, 0);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(InterstitialAdID);
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
//                Log.d("AD FAILED", "ON LOG : " + i);
                requestNewInterstitial();
            }
        });

        // Create adapter for records
        this.adapter = new RecordAdapter(this, this, R.layout.item_app, new ArrayList<Result>());
        this.list.setAdapter(this.adapter);

        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                adapter.onClick(position, v);
            }
        });
        this.adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                if (adapter.isEmpty()) {
                    listContainer.setVisibility(View.GONE);
                    listEmpty.setVisibility(View.VISIBLE);
                } else {
                    listContainer.setVisibility(View.VISIBLE);
                    listEmpty.setVisibility(View.GONE);
                }
            }
        });

        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                listContainer.setVisibility(View.VISIBLE);
                return false;

            }
        });
        // Listen to changes
        searchEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Auto left-trim text.
                if (s.length() > 0 && s.charAt(0) == ' ')
                    s.delete(0, 1);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                adjustInputType(text);
                updateRecords(text);
                displayClearOnInput();
            }
        });

        // On validate, launch first record
        searchEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                RecordAdapter adapter = ((RecordAdapter) list.getAdapter());

                adapter.onClick(adapter.getCount() - 1, null);

                return true;
            }
        });

        kissBar = findViewById(R.id.main_kissbar);
        favoritesKissBar = findViewById(R.id.favoritesKissBar);

        menuButton = findViewById(R.id.menuButton);
        registerForContextMenu(menuButton);

        this.list.setLongClickable(true);
        this.list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
                ((RecordAdapter) parent.getAdapter()).onLongClick(pos, v);
                return true;
            }
        });

        this.hider = new KeyboardScrollHider(this,
                (BlockableListView) this.list,
                (BottomPullEffectView) this.findViewById(R.id.listEdgeEffect)
        );
        this.hider.start();

        // Check whether user enabled spell check and adjust input type accordingly
        searchEditTextWorkaround = prefs.getBoolean("enable-keyboard-workaround", false);
        adjustInputType(null);

        //enable/disable phone/sms broadcast receiver
        PackageManagerUtils.enableComponent(this, IncomingSmsHandler.class, prefs.getBoolean("enable-sms-history", false));
        PackageManagerUtils.enableComponent(this, IncomingCallHandler.class, prefs.getBoolean("enable-phone-history", false));

        // Hide the "X" after the text field, instead displaying the menu button
        displayClearOnInput();


        index = appSettings.getReadingSurah();
        btn_previous = (ImageButton) findViewById(R.id.btn_previous);
        btn_next = (ImageButton) findViewById(R.id.btn_next);
        btn_bookmark = (ImageButton) findViewById(R.id.btn_bookmark);
        btn_bookmark_list = (ImageButton) findViewById(R.id.btn_bookmark_list);
        surahName = (TextView) findViewById(R.id.surah_name);
        surahName.setText(TAGS.SURAH_NAMES[index]);
        db = new QuranDatabase(context);
        listView = (ListView) findViewById(R.id.list);
        ayahlist = db.getText(index);
        db.close();

        btn_previous.setImageDrawable(getResources().getDrawable(appSettings.getFontColor()
                ? R.drawable.ic_skip_previous_white_24dp : R.drawable.ic_skip_previous_black_24dp));
        btn_next.setImageDrawable(getResources().getDrawable(appSettings.getFontColor()
                ? R.drawable.ic_skip_next_white_24dp : R.drawable.ic_skip_next_black_24dp));
//        btn_bookmark.setImageDrawable(getResources().getDrawable(appSettings.getFontColor()
//                ? R.drawable.ic_play_circle_outline_white_24dp : R.drawable.ic_play_circle_outline_black_24dp));
        btn_bookmark_list.setImageDrawable(getResources().getDrawable(appSettings.getFontColor()
                ? R.drawable.ic_collections_bookmark_white_24dp : R.drawable.ic_collections_bookmark_black_24dp));

        if (appSettings.useCustomFont()) {
            tf = Typeface.createFromAsset(getApplicationContext().getAssets(), String.format("fonts/%s", appSettings.getFontType()));
        }
        efficientAdapter = new EfficientAdapter();
        listView.setAdapter(efficientAdapter);

        int[] i = appSettings.getScrollPosition();
        listView.setSelectionFromTop(i[0], i[1]);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                saveCurrentPosition();
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        btn_bookmark_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showArrayDialog(context, READING_SURAH, SURAH_NAMES, index);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new QuranDatabase(context);
                if (index == 113) {
                    index = 0;
                } else {
                    index++;
                }
                ayahlist = db.getText(index);
                efficientAdapter = new EfficientAdapter();
                db.close();
//                surahName.setText((index + 1) + ". سُّورَةُ " + TAGS.SURAH_NAMES[index]);
                surahName.setText(TAGS.SURAH_NAMES[index]);
                appSettings.putReadingSurah(index);
                listView.setAdapter(efficientAdapter);
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new QuranDatabase(context);
                if (index == 0) {
                    index = 113;
                } else {
                    index--;
                }
                ayahlist = db.getText(index);
                efficientAdapter = new EfficientAdapter();
                db.close();
//                surahName.setText((index + 1) + ". سُّورَةُ " + TAGS.SURAH_NAMES[index]);
                surahName.setText(TAGS.SURAH_NAMES[index]);
                appSettings.putReadingSurah(index);
                listView.setAdapter(efficientAdapter);
            }
        });

        if (!appSettings.getBoolean("NOT_FIRST_RUN")) {
            startActivity(new Intent(context, Splash.class));
            appSettings.putBoolean("NOT_FIRST_RUN", true);
        }
    }

    public void showArrayDialog(final Context context, final String key, String[] array, int selected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Select")
                .setSingleChoiceItems(array, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        appSettings.putInt(key, i);
                        db = new QuranDatabase(context);
                        ayahlist = db.getText(i);
                        efficientAdapter = new EfficientAdapter();
                        db.close();
                        index = i;
//                        surahName.setText((index + 1) + ". سُّورَةُ " + TAGS.SURAH_NAMES[i]);
                        surahName.setText(TAGS.SURAH_NAMES[i]);
                        listView.setAdapter(efficientAdapter);
                        dialogInterface.cancel();
                    }
                });
        builder.show();
        drawer.bringToFront();
    }

    private void adjustInputType(String currentText) {
        int currentInputType = searchEditText.getInputType();
        int requiredInputType;

        if (currentText != null && Pattern.matches("[+]\\d+", currentText)) {
            requiredInputType = InputType.TYPE_CLASS_PHONE;
        } else if (searchEditTextWorkaround) {
            requiredInputType = INPUT_TYPE_WORKAROUND;
        } else {
            requiredInputType = INPUT_TYPE_STANDARD;
        }
        if (currentInputType != requiredInputType) {
            searchEditText.setInputType(requiredInputType);
        }
    }

    private void prepareNamazTiming() {
        int[] pics = {
                R.drawable.pic_fajar,
                R.drawable.pic_sunrise,
                R.drawable.pic_dhuhr,
                R.drawable.pic_asr,
                R.drawable.pic_sunset,
                R.drawable.pic_maghrib,
                R.drawable.pic_isha};

        gridView = (GridView) findViewById(R.id.namaz_list);
        ArrayList<String> list;
        PrayTime prayTime = new PrayTime();
        int calculationMethod = appSettings.calculationMethod();
        int juristicMethod = appSettings.juristicMethod();
        int timeFormat = appSettings.timeFormat();
//        double timeZone = appSettings.getDouble(TIME_ZONE_DOUBLE);
        TimeZone defaultTz = TimeZone.getDefault();

        //Get NY calendar object with current date/time
        Calendar defaultCalc = Calendar.getInstance(defaultTz);

        //Get offset from UTC, accounting for DST
        int defaultTzOffsetMs = defaultCalc.get(Calendar.ZONE_OFFSET) + defaultCalc.get(Calendar.DST_OFFSET);
        double timezone = defaultTzOffsetMs / (1000 * 60 * 60);
        Log.d("TIME ZONE", "TIME ZONE " + timezone);
        double lat = appSettings.getDouble(LAT);
        double lon = appSettings.getDouble(LONG);
        prayTime.setCalcMethod(calculationMethod);
        prayTime.setAsrJuristic(juristicMethod);
        prayTime.setTimeFormat(timeFormat);
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayTime.tune(offsets);
        ArrayList<String> names = prayTime.getTimeNames();
        list = prayTime.getPrayerTimes(Calendar.getInstance(), lat, lon, timezone);
//        for (int i = 0; i < names.size(); i++) {
//            list.set(i, names.get(i) + " " + list.get(i));
//        }
        final ArrayList<AppDetail> namazList = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            if (i != 4) {
                AppDetail namaz = new AppDetail();
                namaz.packageName = names.get(i);
                namaz.appName = list.get(i);
                namaz.icon = getResources().getDrawable(pics[i]);
                namazList.add(namaz);
            }
        }

        ArrayAdapter<AppDetail> adapterN = new ArrayAdapter<AppDetail>(context,
                R.layout.list_item_namaz_time,
                namazList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item_namaz_time, null);
                }

                ImageView appIcon = (ImageView) convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(namazList.get(position).icon);

                TextView appLabel = (TextView) convertView.findViewById(R.id.item_app_label);
                appLabel.setText(namazList.get(position).packageName + "\n" + namazList.get(position).appName.replace(" ", "\n"));
                appLabel.setTextColor(appSettings.getFontColor() ? Color.BLACK : Color.WHITE);


                return convertView;
            }
        };

        if (appSettings.getBoolean(CITY_SELECTED))
            gridView.setAdapter(adapterN);
//
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, IslamicSettings.class);
                intent.putExtra("OPEN", 2);
//                startActivityForResult(intent, 100);
                startActivity(intent);
            }
        });

    }

    private void displayQuickFavoritesBar(boolean initialize, boolean touched) {
        View quickFavoritesBar = findViewById(R.id.favoritesBar);
        if (searchEditText.getText().toString().length() == 0
                && prefs.getBoolean("enable-favorites-bar", false)) {
            if ((!prefs.getBoolean("favorites-hide", false) || touched)) {
                quickFavoritesBar.setVisibility(View.VISIBLE);
            }

            if (initialize) {
                Log.i(TAG, "Using quick favorites bar, filling content.");
                favoritesKissBar.setVisibility(View.INVISIBLE);
                displayFavorites();
            }
        } else {
            quickFavoritesBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return onOptionsItemSelected(item);
    }

    /**
     * Restart if required,
     * Hide the kissbar by default
     */

    public void saveCurrentPosition() {
        int index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
        appSettings.putScrollPosition(index, top);
//        Log.d("ON Save", "ON save");
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("0F10741D5AAB3090D21A5BC3E9089B9E")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    int COUNT = 0;

    @SuppressLint("CommitPrefEdits")
    protected void onResume() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded() && COUNT > 5 && Helper.isNetworkAvailable(context)) {
            mInterstitialAd.show();
            requestNewInterstitial();
            COUNT = 0;
        } else {
            COUNT++;
        }
        assert handle != null;
        handle.setBackgroundResource(appSettings.getFontColor()
                ? R.drawable.background_handle_dark : R.drawable.background_handle_white);
        Log.i(TAG, "Resuming KISS");
        if (list != null) {
//            Log.d("ON LOG", "ON Resume");
            int[] i = appSettings.getScrollPosition();
            index = appSettings.getReadingSurah();
//            listView.scrollTo(i[0], i[1]);
            listView.setSelectionFromTop(i[0], i[1]);
//            Log.d("ON Resume", "ON LOG" + i[0] + " " + i[1]);
            saveCurrentPosition();
        }
        if (handler != null)
            handler.post(r);

        if (prefs.getBoolean("require-layout-update", false)) {
            Log.i(TAG, "Restarting app after setting changes");
            // Restart current activity to refresh view, since some preferences
            // may require using a new UI
            prefs.edit().putBoolean("require-layout-update", false).commit();
            Intent i = new Intent(this, getClass());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(i);
            overridePendingTransition(0, 0);
            super.onResume();
            return;
        }

        if (kissBar.getVisibility() != View.VISIBLE) {
            updateRecords(searchEditText.getText().toString());
            displayClearOnInput();
        } else {
            displayKissBar(false);
        }

        //Show favorites above search field ONLY if AppProvider is already loaded
        //Otherwise this will get triggered by the broadcastreceiver in the onCreate
        AppProvider appProvider = KissApplication.getDataHandler(this).getAppProvider();
        if (appProvider != null && appProvider.isLoaded())
            // Favorites needs to be displayed again if the quickfavorite bar is active,
            // Not sure why exactly, but without the "true" the favorites drawable will disappear
            // (not their intent) after moving to another activity and switching back to KISS.
            displayQuickFavoritesBar(true, searchEditText.getText().toString().length() > 0);

        // Activity manifest specifies stateAlwaysHidden as windowSoftInputMode
        // so the keyboard will be hidden by default
        // we may want to display it if the setting is set
        if (prefs.getBoolean("display-keyboard", false)) {
            // Display keyboard
            showKeyboard();

            new Handler().postDelayed(displayKeyboardRunnable, 10);
            // For some weird reasons, keyboard may be hidden by the system
            // So we have to run this multiple time at different time
            // See https://github.com/Neamar/KISS/issues/119
            new Handler().postDelayed(displayKeyboardRunnable, 100);
            new Handler().postDelayed(displayKeyboardRunnable, 500);
        } else {
            // Not used (thanks windowSoftInputMode)
            // unless coming back from KISS settings
            hideKeyboard();
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregister our receiver
        this.unregisterReceiver(this.mReceiver);
        KissApplication.getCameraHandler().releaseCamera();
    }

    @Override
    protected void onPause() {
        saveCurrentPosition();

        super.onPause();
        KissApplication.getCameraHandler().releaseCamera();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // This is called when the user press Home again while already browsing MainActivity
        // onResume() will be called right after, hiding the kissbar if any.
        // http://developer.android.com/reference/android/app/Activity.html#onNewIntent(android.content.Intent)
        // Animation can't happen in this method, since the activity is not resumed yet, so they'll happen in the onResume()
        // https://github.com/Neamar/KISS/issues/569
        if (!searchEditText.getText().toString().isEmpty()) {
            Log.i(TAG, "Clearing search field");
            searchEditText.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        listContainer.setVisibility(View.GONE);
        if (drawer.isOpened()) {
            drawer.animateClose();
            displayKissBar(false);
        } else {
            // Is the kiss bar visible?
            if (kissBar.getVisibility() == View.VISIBLE) {
                displayKissBar(false);
            } else if (!searchEditText.getText().toString().isEmpty()) {
                // If no kissmenu, empty the search bar
                searchEditText.setText("");
                displayKissBar(false);
            }
            // No call to super.onBackPressed, since this would quit the launcher.
        }

    }

    @Override
    public boolean onKeyDown(int keycode, @NonNull KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                // For user with a physical menu button, we still want to display *our* contextual menu
                menuButton.showContextMenu();
                return true;
        }

        return super.onKeyDown(keycode, e);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                return true;
            case R.id.wallpaper:
                hideKeyboard();
                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(Intent.createChooser(intent, getString(R.string.menu_wallpaper)));
                return true;
            case R.id.preferences:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.QuranDisplaySettings:
                Intent i = new Intent(context, IslamicSettings.class);
                i.putExtra("OPEN", 1);
//                startActivityForResult(i, 100);
                startActivity(i);
                return true;
            case R.id.NamazTimeSettings:
                Intent i2 = new Intent(context, IslamicSettings.class);
                i2.putExtra("OPEN", 2);
//                startActivityForResult(i2, 100);
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        Toast.makeText(this, "DONE", 0).show();
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);

        return true;
    }

    /**
     * Display menu, on short or long press.
     *
     * @param menuButton "kebab" menu (3 dots)
     */
    public void onMenuButtonClicked(View menuButton) {
        // When the kiss bar is displayed, the button can still be clicked in a few areas (due to favorite margin)
        // To fix this, we discard any click event occurring when the kissbar is displayed
        if (kissBar.getVisibility() != View.VISIBLE)
            menuButton.showContextMenu();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        //if motion movement ends
        if ((event.getAction() == MotionEvent.ACTION_CANCEL) || (event.getAction() == MotionEvent.ACTION_UP)) {
            //if history is hidden
//            listContainer.setVisibility(View.VISIBLE);
            if (prefs.getBoolean("history-hide", true) && prefs.getBoolean("history-onclick", false)) {
                //if not on the application list and not searching for something
                if ((kissBar.getVisibility() != View.VISIBLE) && (searchEditText.getText().toString().isEmpty())) {
                    //if list is empty
                    if ((this.list.getAdapter() == null) || (this.list.getAdapter().getCount() == 0)) {
                        searcher = new HistorySearcher(MainActivity.this);
                        searcher.execute();
                    }
                }
            }
            if (prefs.getBoolean("history-hide", true) && prefs.getBoolean("favorites-hide", false)) {
                displayQuickFavoritesBar(false, true);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Clear text content when touching the cross button
     */
    @SuppressWarnings("UnusedParameters")
    public void onClearButtonClicked(View clearButton) {
        searchEditText.setText("");
    }

    /**
     * Display KISS menu
     */
    public void onLauncherButtonClicked(View launcherButton) {
        // Display or hide the kiss bar, according to current view tag (showMenu / hideMenu).

        displayKissBar(launcherButton.getTag().equals("showMenu"));
    }

    public void onFavoriteButtonClicked(View favorite) {
        // The bar is shown due to dispatchTouchEvent, hide it again to stop the bad ux.
        displayKissBar(false);

        int favNumber = Integer.parseInt((String) favorite.getTag());
        ArrayList<Pojo> favorites = KissApplication.getDataHandler(MainActivity.this).getFavorites(tryToRetrieve);
        if (favNumber >= favorites.size()) {
            // Clicking on a favorite before everything is loaded.
            Log.i(TAG, "Clicking on an unitialized favorite.");
            return;
        }
        // Favorites handling
        Pojo pojo = favorites.get(favNumber);
        final Result result = Result.fromPojo(MainActivity.this, pojo);

        result.fastLaunch(MainActivity.this);
    }

    private void displayClearOnInput() {
        final View clearButton = findViewById(R.id.clearButton);
        if (searchEditText.getText().length() > 0) {
            clearButton.setVisibility(View.VISIBLE);
            menuButton.setVisibility(View.INVISIBLE);
        } else {
            clearButton.setVisibility(View.INVISIBLE);
            menuButton.setVisibility(View.VISIBLE);
        }
    }

    private void displayLoader(Boolean display) {
        final View loaderBar = findViewById(R.id.loaderBar);
        final View launcherButton = findViewById(R.id.launcherButton);

        int animationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        if (!display) {
            launcherButton.setVisibility(View.VISIBLE);

            // Animate transition from loader to launch button
            launcherButton.setAlpha(0);
            launcherButton.animate()
                    .alpha(1f)
                    .setDuration(animationDuration)
                    .setListener(null);
            loaderBar.animate()
                    .alpha(0f)
                    .setDuration(animationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loaderBar.setVisibility(View.GONE);
                            loaderBar.setAlpha(1);
                        }
                    });
        } else {
            launcherButton.setVisibility(View.INVISIBLE);
            loaderBar.setVisibility(View.VISIBLE);
        }
    }

    private void displayKissBar(Boolean display) {
        final ImageView launcherButton = (ImageView) findViewById(R.id.launcherButton);

        // get the center for the clipping circle
        int cx = (launcherButton.getLeft() + launcherButton.getRight()) / 2;
        int cy = (launcherButton.getTop() + launcherButton.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(kissBar.getWidth(), kissBar.getHeight());

        if (display) {
            // Display the app list
            if (searcher != null) {
                searcher.cancel(true);
            }
            searcher = new ApplicationsSearcher(MainActivity.this);
            searcher.execute();

            // Reveal the bar
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator anim = ViewAnimationUtils.createCircularReveal(kissBar, cx, cy, 0, finalRadius);
                kissBar.setVisibility(View.VISIBLE);
                anim.start();
            } else {
                // No animation before Lollipop
                kissBar.setVisibility(View.VISIBLE);
            }

            // Only display favorites if we're not using the quick bar
            if (favoritesKissBar.getVisibility() == View.VISIBLE) {
                // Retrieve favorites. Try to retrieve more, since some favorites can't be displayed (e.g. search queries)
                displayFavorites();
            }
        } else {
            // Hide the bar
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator anim = ViewAnimationUtils.createCircularReveal(kissBar, cx, cy, finalRadius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        kissBar.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                });
                anim.start();
            } else {
                // No animation before Lollipop
                kissBar.setVisibility(View.GONE);
            }
            searchEditText.setText("");

            if (prefs.getBoolean("display-keyboard", false)) {
                // Display keyboard
                showKeyboard();
            }
        }

        // Hide the favorite bar in the kiss bar if the quick bar is enabled
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("enable-favorites-bar", false)) {
            favoritesKissBar.setVisibility(View.INVISIBLE);
        } else {
            favoritesKissBar.setVisibility(View.VISIBLE);
        }
    }

    public void displayFavorites() {
        int[] favoritesIds = favoritesKissBar.getVisibility() == View.VISIBLE ? favsIds : favBarIds;

        ArrayList<Pojo> favoritesPojo = KissApplication.getDataHandler(MainActivity.this)
                .getFavorites(tryToRetrieve);

        if (favoritesPojo.size() == 0) {
            int noFavCnt = prefs.getInt("no-favorites-tip", 0);
            if (noFavCnt < 3 && !prefs.getBoolean("enable-favorites-bar", false)) {
                Toast toast = Toast.makeText(MainActivity.this, getString(R.string.no_favorites), Toast.LENGTH_SHORT);
                toast.show();
                prefs.edit().putInt("no-favorites-tip", ++noFavCnt).apply();

            }
        }

        // Don't look for items after favIds length, we won't be able to display them
        for (int i = 0; i < Math.min(favoritesIds.length, favoritesPojo.size()); i++) {
            Pojo pojo = favoritesPojo.get(i);

            ImageView image = (ImageView) findViewById(favoritesIds[i]);

            Result result = Result.fromPojo(MainActivity.this, pojo);
            Drawable drawable = result.getDrawable(MainActivity.this);
            if (drawable != null) {
                image.setImageDrawable(drawable);
            } else {
                Log.e(TAG, "Falling back to default image for favorite.");
                // Use the default contact image otherwise
                image.setImageResource(R.drawable.ic_contact);
            }

            image.setVisibility(View.VISIBLE);
            image.setContentDescription(pojo.displayName);
        }

        // Hide empty favorites (not enough favorites yet)
        for (int i = favoritesPojo.size(); i < favoritesIds.length; i++) {
            findViewById(favoritesIds[i]).setVisibility(View.GONE);
        }
    }

    public void updateRecords() {
        updateRecords(searchEditText.getText().toString());
    }

    /**
     * This function gets called on changes. It will ask all the providers for
     * data
     *
     * @param query the query on which to search
     */
    private void updateRecords(String query) {
        if (searcher != null) {
            searcher.cancel(true);
        }
        if (query.length() == 0) {
            if (prefs.getBoolean("history-hide", false)) {
                list.setVerticalScrollBarEnabled(false);
                searchEditText.setHint("");
                searcher = new NullSearcher(this);
                //Hide default scrollview
                findViewById(R.id.main_empty).setVisibility(View.INVISIBLE);

            } else {
                list.setVerticalScrollBarEnabled(true);
                searchEditText.setHint(R.string.ui_search_hint);
                searcher = new HistorySearcher(this);
                //Show default scrollview
                findViewById(R.id.main_empty).setVisibility(View.VISIBLE);
            }
        } else {
            searcher = new QuerySearcher(this, query);
        }
        searcher.execute();
        displayQuickFavoritesBar(false, false);
    }

    public void resetTask() {
        searcher = null;
    }

    /**
     * Call this function when we're leaving the activity We can't use
     * onPause(), since it may be called for a configuration change
     */
    public void launchOccurred(int index, Result result) {
        // We selected an item on the list,
        // now we can cleanup the filter:
        if (!searchEditText.getText().toString().equals("")) {
            searchEditText.setText("");
            hideKeyboard();
        }
    }

    @Override
    public void showKeyboard() {
        searchEditText.requestFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Check if history / search or app list is visible
     *
     * @return true of history, false on app list
     */

    public boolean isOnSearchView() {
        return kissBar.getVisibility() != View.VISIBLE;
    }

    public int getFavIconsSize() {
        return favsIds.length;
    }

    @Override
    public void onDrawerClosed() {
        saveCurrentPosition();

    }

    @Override
    public void onDrawerOpened() {
        int[] i = appSettings.getScrollPosition();
        index = appSettings.getReadingSurah();
        listView.scrollTo(i[0], i[1]);
//        listView.setSelectionFromTop(i[0], i[1]);
        if (Helper.isNetworkAvailable(context)) {
            mAdView.setVisibility(View.VISIBLE);
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollStarted() {
//
    }

    @Override
    public void onScrollEnded() {
        saveCurrentPosition();
    }


    static class ViewHolder {
        TextView ayahArabic;
        TextView ayahNumber;
        TextView ayahTranslitration;
        TextView ayahTranslation;
    }

    private class EfficientAdapter extends BaseAdapter {

        LayoutInflater inflater;
        boolean[] format;


        EfficientAdapter() {
            ;
            inflater = LayoutInflater.from(context);
            format = appSettings.getAyahFormat();

        }

        @Override
        public int getCount() {
            return ayahlist.size();
        }

        @Override
        public AyahObject getItem(int position) {
            return ayahlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            AyahObject ayah = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_ayah, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ayahArabic = (TextView) convertView
                    .findViewById(R.id.txt_arabic);
            holder.ayahTranslation = (TextView) convertView
                    .findViewById(R.id.txt_translation);
            holder.ayahTranslitration = (TextView) convertView
                    .findViewById(R.id.txt_transliteration);
            holder.ayahNumber = (TextView) convertView
                    .findViewById(R.id.txt_number);
            if (position == 0) {
                holder.ayahNumber.setText("");
            } else {
                holder.ayahNumber.setText("" + (position));
            }
            holder.ayahNumber.setTextColor(appSettings.getFontColor() ? Color.WHITE : Color.BLACK);
            try {
                String Arabic;
                Arabic = ayah.getArabic();
                if (Arabic != null) {
                    Arabic = Arabic.replace("۰", "");
                    Arabic = Arabic.replace("۱", "");
                    Arabic = Arabic.replace("۲", "");
                    Arabic = Arabic.replace("۳", "");
                    Arabic = Arabic.replace("۴", "");
                    Arabic = Arabic.replace("۵", "");
                    Arabic = Arabic.replace("١ۙ۬", "");
                    Arabic = Arabic.replace("۷", "");
                    Arabic = Arabic.replace("۸", "");
                    Arabic = Arabic.replace("۹", "");
                    Arabic = Arabic.replace("\u06DD۶", "\u06DD");


                }
//            Arabic = position + ": " + Arabic;

//            if (position == 0 && index != 8) {
//                holder.ayahNumber.setText("" + position);
//            } else if (index == 8) {
//                holder.ayahNumber.setText("" + position + 1);
//
//            }
                holder.ayahArabic.setText(Arabic);
                holder.ayahArabic.setTextColor(appSettings.getFontColor() ? Color.WHITE : Color.BLACK);
                holder.ayahArabic.setTextSize(SP, appSettings.getFontSizeArabic());
                holder.ayahArabic.setVisibility(format[0] ? View.VISIBLE : View.GONE);
                if (appSettings.useCustomFont())
                    holder.ayahArabic.setTypeface(tf);

//                holder.ayahArabic.setTextDirection();

                holder.ayahTranslitration.setText(Html.fromHtml(ayah.getTranslitration()));
                holder.ayahTranslitration.setTypeface(tf);
                holder.ayahTranslitration.setTextSize(SP, appSettings.getFontSizeTranslitration());
                holder.ayahTranslitration.setTextColor(appSettings.getFontColor() ? Color.WHITE : Color.BLACK);
                holder.ayahTranslitration.setVisibility(format[1] ? View.VISIBLE : View.GONE);

                holder.ayahTranslation.setTextSize(SP, appSettings.getFontSizeTranslation());
                holder.ayahTranslation.setText(ayah.getTranslation());
                holder.ayahTranslation.setTextColor(appSettings.getFontColor() ? Color.WHITE : Color.BLACK);
                holder.ayahTranslation.setVisibility(format[2] ? View.VISIBLE : View.GONE);

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
