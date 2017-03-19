package fr.usg.islamiclauncher.object;

/**
 * Created by Kamran on 12/28/2016.
 */

public class AyahObject {
    String arabic = "";

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getTranslitration() {
        return translitration;
    }

    public void setTranslitration(String translitration) {
        this.translitration = translitration;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    String translitration = "";
    String translation = "";
}
