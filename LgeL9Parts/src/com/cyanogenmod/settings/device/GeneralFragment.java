/*
 * Copyright (C) 2011 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceCategory;

import com.cyanogenmod.settings.device.R;

public class GeneralFragment extends PreferenceFragment {

    public static final String KEY_TOUCH_LED = "touch_led_preference";
    public static final String KEY_VIBRATOR = "vibrator_intensity_preference";

    private static final String TOUCH_LED_FILE = "/sys/class/misc/backlightnotification/enable_touch_ex";

    private VibratorIntensity mVibrator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isSupported())
          return;

        addPreferencesFromResource(R.xml.general);

        if (!TouchBlinkActivity.isEnabled()) {
            PreferenceCategory category = (PreferenceCategory) getPreferenceScreen().findPreference("touch_lights_preference_category");
            category.removePreference(findPreference("touch_blink_preference"));
            //getPreferenceScreen().removePreference(category);
        }

        //

        Preference touchLedPref = findPreference(KEY_TOUCH_LED);

        touchLedPref.setOnPreferenceChangeListener(
          new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Utils.writeValue(TOUCH_LED_FILE, (Boolean) newValue ? "0" : "1"); // option is Disable, so we invert
                return true;
            }
          }
        );

	//

	if (VibratorIntensity.isSupported())
	    mVibrator = (VibratorIntensity) findPreference(KEY_VIBRATOR);

    }

    public static boolean isSupported() {
        return Utils.fileExists(TOUCH_LED_FILE);
    }

    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Utils.writeValue(TOUCH_LED_FILE, sharedPrefs.getBoolean(GeneralFragment.KEY_TOUCH_LED, false) ? "0" : "1");

        TouchBlinkActivity.restore(context);
        VibratorIntensity.restore(context);
    }

}
