package com.ganley.nbajamte;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/** Remote-friendly settings screen: ROM path, core choice, link to RetroArch. */
public class SetupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        final EditText rom = findViewById(R.id.rom);
        final RadioGroup cores = findViewById(R.id.cores);
        rom.setText(Config.romPath(this));
        cores.check(Config.CORE_MAME.equals(Config.core(this))
                ? R.id.core_mame : R.id.core_mame2003p);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String core = cores.getCheckedRadioButtonId() == R.id.core_mame
                        ? Config.CORE_MAME : Config.CORE_MAME2003_PLUS;
                Config.save(SetupActivity.this, rom.getText().toString().trim(), core);
                Toast.makeText(SetupActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        findViewById(R.id.getra).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { openRetroArchListing(); }
        });

        findViewById(Config.findRetroArch(this) == null ? R.id.getra : R.id.save).requestFocus();
    }

    private void openRetroArchListing() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.retroarch")));
        } catch (ActivityNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.retroarch.com/?page=platforms")));
            } catch (ActivityNotFoundException e2) {
                Toast.makeText(this, "Install RetroArch from the Play Store",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
