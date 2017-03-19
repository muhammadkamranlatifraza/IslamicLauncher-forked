package fr.usg.islamiclauncher.appSettings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class Helper {
    public static ProgressDialog progressDialog;

    public static void showSuccessDialog(Context context, String message) {
        new AlertDialog.Builder(context).setTitle("Success")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_info).show();
    }

    public static void showEmailDialog(final Context context, final String subject) {
        new AlertDialog.Builder(context).setTitle("Email Us")
                .setMessage("Please send email with As many details as possible. We will look into it as soon as possible. \n\nPlease DO NOT change subject and email address. Continue to Email?")
                .setPositiveButton(android.R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
//                        intent.putExtra(Intent.EXTRA_EMAIL, R.string.app_email);
                        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        intent.putExtra(Intent.EXTRA_TEXT, "");

                        context.startActivity(Intent.createChooser(intent, "Send Email"));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info).show();
    }

    public static void showErrorDialog(Context context, String message) {

        new AlertDialog.Builder(context).setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }

//    public static void showErrorGoogleServicesDialog(final Context context, String message) {
//
//        new AlertDialog.Builder(context).setTitle("Error")
//                .setMessage(message)
//                .setPositiveButton(android.R.string.ok, null)
//                .setIcon(android.R.drawable.ic_dialog_alert).show();
//    }

    public static void showResetAllDialog(final Context context) {
        new AlertDialog.Builder(context).setTitle("Reset All")
//                .setMessage(R.string.dialog_message_delete_all)
                .setMessage("This cannot be undone. Are You Sure?")
                .setPositiveButton(android.R.string.ok, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppSettings prefs = new AppSettings(context);
                        prefs.resetSettings();
                    }
                }).setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }
//
//    public static void showEnterPINDialog(final Context context, final String appName) {
//        LinearLayout layout = new LinearLayout(context);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        lp.setMargins(5, 5, 5, 5);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        final EditText PIN1, PIN2 = new EditText(context);
//        PIN2.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
//        PIN2.setTransformationMethod(PasswordTransformationMethod.getInstance());
//
//        PIN1 = new EditText(context);
//        PIN1.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
//        PIN1.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        PIN1.setHint("Enter 4 Digits");
//        PIN2.setHint("Same 4 Digits again");
//
//        InputFilter[] FilterArray = new InputFilter[1];
//        FilterArray[0] = new InputFilter.LengthFilter(10);
//        PIN1.setFilters(FilterArray);
//        PIN2.setFilters(FilterArray);
//        PIN1.setMaxLines(1);
//        PIN2.setMaxLines(1);
//        layout.addView(PIN1);
//        layout.addView(PIN2);
//        new AlertDialog.Builder(context).setTitle("Reset All").setView(layout)
//                .setMessage(R.string.dialog_message_delete_all)
//                .setPositiveButton(android.R.string.ok, new OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (checkPIN(context, PIN1.getText().toString(), PIN2.getText().toString())) {
//                            AppSettings prefs = new AppSettings(context);
//                            prefs.lockApp(appName, PIN1.getText().toString());
//                            showSuccessDialog(context, context.getResources().getString(R.string.app_locked));
//                        }
//                    }
//                }).setNegativeButton(android.R.string.cancel, null)
//                .setIcon(android.R.drawable.ic_dialog_alert).show();
//    }

    public static void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("One Moment...");
        progressDialog.show();
    }

    private static boolean checkPIN(Context context, String PIN1, String PIN2) {
        boolean isOK = true;
        if (PIN1.length() != 4 && PIN2.length() != 4 || PIN1.contains(" ")) {
            String message = "Only 4 digits allowed";
            showErrorDialog(context, message);
            isOK = false;
        } else {
            if (!PIN1.equals(PIN2)) {
                isOK = false;
                String message = "Both Values should be same";
                showErrorDialog(context, message);
            }
        }
        return isOK;

    }

    public static void hideSoftKeybord(Context context, View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        // if activeNetworkInfo != null && activeNetworkInfo.isConnected()
        // then return true or else false....
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
