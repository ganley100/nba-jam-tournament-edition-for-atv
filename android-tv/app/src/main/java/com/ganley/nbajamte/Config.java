package com.ganley.nbajamte;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;

/** Stored launcher settings plus RetroArch package detection. */
final class Config {
    static final String CORE_MAME2003_PLUS = "mame2003_plus_libretro_android.so";
    static final String CORE_MAME = "mame_libretro_android.so";

    /** RetroArch builds, in order of preference. */
    static final String[] RETROARCH_PACKAGES = {
            "com.retroarch.aarch64", "com.retroarch", "com.retroarch.ra32"
    };
    static final String RETROARCH_ACTIVITY =
            "com.retroarch.browser.retroactivity.RetroActivityFuture";

    private static final String PREFS = "launcher";
    private static final String KEY_ROM = "rom";
    private static final String KEY_CORE = "core";

    private Config() {}

    private static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    static String defaultRomPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/RetroArch/roms/mame2003-plus/nbajamte.zip";
    }

    static String romPath(Context c) {
        return prefs(c).getString(KEY_ROM, defaultRomPath());
    }

    static String core(Context c) {
        return prefs(c).getString(KEY_CORE, CORE_MAME2003_PLUS);
    }

    static void save(Context c, String rom, String core) {
        prefs(c).edit().putString(KEY_ROM, rom).putString(KEY_CORE, core).apply();
    }

    /** Returns the installed RetroArch package name, or null if none is installed. */
    static String findRetroArch(Context c) {
        PackageManager pm = c.getPackageManager();
        for (String pkg : RETROARCH_PACKAGES) {
            try {
                pm.getPackageInfo(pkg, 0);
                return pkg;
            } catch (PackageManager.NameNotFoundException ignored) {
                // try next
            }
        }
        return null;
    }
}
