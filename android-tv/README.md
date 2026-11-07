# NBA Jam T.E. — Google TV / Android TV launcher

A small Android TV app that puts an **NBA Jam T.E.** tile (with the flaming-ball art)
on your Google TV home screen. Pressing **PLAY** (or START on a gamepad) boots the
**arcade** version through RetroArch's MAME arcade core, using your own ROM set.

The app does not contain the game. It is a launcher: the arcade hardware (TMS34010 CPU,
DMA blitter, DCS sound) is emulated by RetroArch's MAME core, and the game data comes
from your `nbajamte.zip`.

## Install on Google TV

1. Install **RetroArch** from the Play Store on the TV.
2. In RetroArch: *Main Menu → Load Core → Download a Core → Arcade (MAME 2003-Plus)*.
3. Copy your `nbajamte.zip` to the TV, e.g.
   `/storage/emulated/0/RetroArch/roms/mame2003-plus/nbajamte.zip`
   (an app such as *Send Files to TV* or a USB drive works).
4. Sideload `NBAJamTE-AndroidTV.apk` (repo root): enable *Settings → System → About →
   Android TV OS build* (click 7×) for Developer options, allow "Install unknown apps"
   for your file manager / Downloader, then open the APK.
5. Open **NBA Jam T.E.** from *Your apps*, choose **SETUP** if your ROM is somewhere else,
   then **PLAY**.

If the game doesn't start, open RetroArch once on its own (so it creates its config and
gets storage permission) and check the ROM path in SETUP.

## Build

```
export ANDROID_HOME=/path/to/android-sdk   # platforms;android-34, build-tools;34.0.0
./gradlew assembleRelease
# -> app/build/outputs/apk/release/app-release.apk
```
