package com.cyanogenmod.settings.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.util.Log;

public class TouchBlinkActivity extends PreferenceActivity  {

    public static final String KEY_TOUCH_BLINK_OFF = "touch_blink_off";
    public static final String KEY_TOUCH_BLINK_ON = "touch_blink_on";

    private static final String FILE = "/sys/class/misc/backlightnotification/enabled";

    private ListPreference mBlinkOff, mBlinkOn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.touch_blink);

        mBlinkOff = (ListPreference) findPreference(KEY_TOUCH_BLINK_OFF);
        mBlinkOff.setEnabled(TouchBlinkOff.isSupported());
        mBlinkOff.setOnPreferenceChangeListener(new TouchBlinkOff());
        TouchBlinkOff.updateSummary(mBlinkOff, Integer.parseInt(mBlinkOff.getValue()));

        mBlinkOn = (ListPreference) findPreference(KEY_TOUCH_BLINK_ON);
        mBlinkOn.setEnabled(TouchBlinkOn.isSupported());
        mBlinkOn.setOnPreferenceChangeListener(new TouchBlinkOn());
        TouchBlinkOn.updateSummary(mBlinkOn, Integer.parseInt(mBlinkOn.getValue()));
    }

    public static boolean isSupported() {
    	return TouchBlinkOff.isSupported() && Utils.fileExists(FILE); 
    }

    public static boolean isEnabled() {
        if (!isSupported()) return false;
        String text = Utils.readOneLine(FILE);
        return Integer.parseInt(text) != 0;
    }

    public static void restore(Context context) {
        if (isSupported()) {
            TouchBlinkOff.restore(context);
            TouchBlinkOn.restore(context);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
