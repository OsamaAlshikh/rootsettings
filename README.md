# Root Settings

A tiny TV-friendly app with buttons that open Android's real Settings screens
(Settings, About device, Developer options, All apps, Network), which custom TV
menus often hide. It does not give root access.

## Get the APK (pick one)

**A. Android Studio (on a PC/Mac)**
1. Open this folder in Android Studio and let it sync.
2. Build > Build Bundle(s) / APK(s) > Build APK(s).
3. The file is at `app/build/outputs/apk/debug/app-debug.apk`.

**B. GitHub, nothing to install**
1. Create a free GitHub account and a new repository.
2. Upload every file in this folder to it, including the hidden `.github` folder.
   If `.github` is skipped, choose Add file > Create new file, name it
   `.github/workflows/build.yml`, and paste in the contents of that file.
3. Open the repository's Actions tab, wait for "Build APK" to turn green
   (about 3 minutes), open the run, and download `RootSettings-apk`.
4. Unzip it to get `app-debug.apk`.

## Install on the TV
1. Copy `app-debug.apk` to a USB drive and plug it into the TV.
2. Open the TV's file manager, select the APK, and allow installing from
   unknown sources if asked.
3. Open "Root Settings" from the apps list. Use the D-pad and OK.
4. Press **About device**, then press OK 7 times quickly on **Build number**.
   Then **Developer options** should open.
