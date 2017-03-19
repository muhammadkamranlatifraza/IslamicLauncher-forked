package fr.usg.islamiclauncher;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class Splash extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new SimpleSlide.Builder()
                .title("Welcome")
                .description("to Islamic Launcher, Read Quran Everywhere, See Namaz Times, and Powerful Features")
                .image(R.drawable.ic_new)
                .background(R.color.kiss_green)
                .backgroundDark(R.color.kiss_green_dark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Quran Drawer")
                .description("In home screen, click at bottom to Open Quran Drawer, Has all Chapters, Remembers where you left")
                .image(R.drawable.screen_drawer)
                .background(R.color.kiss_green)
                .backgroundDark(R.color.kiss_green_dark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Namaz TImes")
                .description("Namaz Times for your city right in front of your home screen")
                .image(R.drawable.screen_namaz_times)
                .background(R.color.kiss_green)
                .backgroundDark(R.color.kiss_green_dark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Islamic Settings")
                .description("Islamic Settings for Namaz Times and Quran Sharif")
                .image(R.drawable.screen_islamic_settings)
                .background(R.color.kiss_green)
                .backgroundDark(R.color.kiss_green_dark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Usage History")
                .description("Type anything, and its there, Can Direct search on web, direct call someone, send text message, or turn on torch")
                .image(R.drawable.screen_history)
                .background(R.color.kiss_green)
                .backgroundDark(R.color.kiss_green_dark)
                .build());

        IslamicSettings.doIt = true;

    }
}
