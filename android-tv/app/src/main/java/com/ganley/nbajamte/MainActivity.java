package com.ganley.nbajamte;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Title screen shown from the Google TV home row. PLAY hands the ROM to RetroArch's
 * arcade core; SETUP lets the user change the ROM path / core.
 */
public class MainActivity extends Activity {
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.status);

        Button play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { launchGame(); }
        });
        findViewById(R.id.setup).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
            }
        });
        play.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String pkg = Config.findRetroArch(this);
        status.setText(pkg == null
                ? "RetroArch is not installed. Open SETUP to get it."
                : "ROM: " + Config.romPath(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // START on a gamepad jumps straight into the game.
        if (keyCode == KeyEvent.KEYCODE_BUTTON_START) {
            launchGame();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void launchGame() {
        String pkg = Config.findRetroArch(this);
        if (pkg == null) {
            startActivity(new Intent(this, SetupActivity.class));
            return;
        }

        String dataDir = "/data/data/" + pkg;
        String external = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Android/data/" + pkg + "/files";

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setComponent(new ComponentName(pkg, Config.RETROARCH_ACTIVITY));
        i.putExtra("ROM", Config.romPath(this));
        i.putExtra("LIBRETRO", dataDir + "/cores/" + Config.core(this));
        i.putExtra("CONFIGFILE", external + "/retroarch.cfg");
        i.putExtra("IME", Settings.Secure.getString(getContentResolver(),
                Settings.Secure.DEFAULT_INPUT_METHOD));
        i.putExtra("DATADIR", dataDir);
        i.putExtra("SDCARD", Environment.getExternalStorageDirectory().getAbsolutePath());
        i.putExtra("EXTERNAL", external);
        try {
            i.putExtra("APK", getPackageManager().getApplicationInfo(pkg, 0).sourceDir);
        } catch (PackageManager.NameNotFoundException ignored) {
            // RetroArch works without it
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(i);
        } catch (ActivityNotFoundException | SecurityException e) {
            status.setText("Could not start RetroArch (" + pkg + "): " + e.getMessage());
        }
    }
}
