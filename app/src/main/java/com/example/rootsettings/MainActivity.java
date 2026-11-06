package com.example.rootsettings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A small TV-friendly launcher: each button tries several ways of opening the
 * stock Android settings screens, because custom TV firmware often hides them.
 * Use the remote's D-pad to move between buttons and OK to press one.
 */
public class MainActivity extends Activity {

    private static final String SETTINGS_PKG = "com.android.settings";
    private static final String TV_SETTINGS_PKG = "com.android.tv.settings";

    private LinearLayout column;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        column = new LinearLayout(this);
        column.setOrientation(LinearLayout.VERTICAL);
        column.setGravity(Gravity.CENTER_HORIZONTAL);
        int pad = dp(32);
        column.setPadding(pad, pad, pad, pad);
        scroll.addView(column, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView title = new TextView(this);
        title.setText("Root Settings");
        title.setTextSize(30);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, dp(20));
        column.addView(title);

        Button first = addButton("Android Settings",
                component(SETTINGS_PKG, "com.android.settings.Settings"),
                component(TV_SETTINGS_PKG, "com.android.tv.settings.MainSettings"),
                new Intent(Settings.ACTION_SETTINGS));

        addButton("About device (tap Build number 7 times)",
                new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS),
                component(SETTINGS_PKG, "com.android.settings.Settings$MyDeviceInfoActivity"),
                component(SETTINGS_PKG, "com.android.settings.DeviceInfoSettings"));

        addButton("Developer options",
                new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
                component(SETTINGS_PKG, "com.android.settings.Settings$DevelopmentSettingsDashboardActivity"),
                component(TV_SETTINGS_PKG, "com.android.tv.settings.system.development.DevelopmentActivity"));

        addButton("All apps",
                new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS),
                new Intent(Settings.ACTION_APPLICATION_SETTINGS));

        addButton("Network / Wi-Fi",
                new Intent(Settings.ACTION_WIFI_SETTINGS),
                new Intent(Settings.ACTION_WIRELESS_SETTINGS));

        status = new TextView(this);
        status.setTextSize(18);
        status.setTextColor(Color.LTGRAY);
        status.setGravity(Gravity.CENTER);
        status.setPadding(0, dp(20), 0, 0);
        status.setText("Use the D-pad to pick a button, then press OK.");
        column.addView(status);

        setContentView(scroll);
        first.requestFocus();
    }

    private Button addButton(final String label, final Intent... candidates) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(22);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setFocusable(true);
        b.setBackground(buttonBackground());
        b.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                open(label, candidates);
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                dp(640), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp(8), 0, dp(8));
        column.addView(b, lp);
        return b;
    }

    /** Dark button normally, bright orange with a white outline when focused. */
    private StateListDrawable buttonBackground() {
        StateListDrawable states = new StateListDrawable();

        GradientDrawable focused = new GradientDrawable();
        focused.setColor(Color.parseColor("#FF9800"));
        focused.setCornerRadius(dp(12));
        focused.setStroke(dp(4), Color.WHITE);

        GradientDrawable normal = new GradientDrawable();
        normal.setColor(Color.parseColor("#37474F"));
        normal.setCornerRadius(dp(12));

        states.addState(new int[]{android.R.attr.state_focused}, focused);
        states.addState(new int[]{android.R.attr.state_pressed}, focused);
        states.addState(new int[]{}, normal);
        return states;
    }

    /** Tries each way of opening the screen until one works. */
    private void open(String label, Intent[] candidates) {
        for (Intent intent : candidates) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                status.setText("Opened: " + label);
                return;
            } catch (RuntimeException e) {
                // Not found, not exported, or blocked by the firmware: try the next way.
            }
        }
        status.setText("\"" + label + "\" is not available on this TV.");
    }

    private static Intent component(String pkg, String cls) {
        Intent i = new Intent();
        i.setComponent(new ComponentName(pkg, cls));
        return i;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
