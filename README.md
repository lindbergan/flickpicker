# FlickPicker

FlickPicker is a android based app, a social network and an rating and movierecommendations engine.
A project made by Chalmers University of Technology students in course: **TDA367 - Object-oriented programming project**

##### Group 22:
- Sebastian Nilsson
- Adrian Lindberg
- Jonatan Nylund
- Jonathan Sundkvist

## Installation

### Without Android Studio

The application files can be cloned or downloaded from this repository. Open your terminal and navigate to the folder where you put the files.

To run the application you need to give Gradle (the tool which builds and packages the application) permission:
```
$ chmod +x gradlew
```
Make sure you have the Android SDK in your path variable. If not, add the following lines to your bash profile (replace the [username] with your computers user account):
```
$ export ANDROID_HOME=/Users/[username]/Library/Android/sdk
$ export export PATH=${PATH}:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```
Build the application with Gradle:
```
$ ./gradlew assembleDebug
```
You also need to have the the Android SDK platform tools installed on your system. Then install the application on your virtual android device: 
```
$ adb install app/build/outputs/apk/app-debug.apk
```
Find the FlickPicker app on your device and open it.

### With Android Studio

In Android Studio, click $File \rightarrow Open$ in the top menu. Select the folder you cloned from the Github repo. Gradle will build automatically. Click $Run \rightarrow Run$ 'app' in the top menu. Pick a virtual device you want to install the app on. Android Studio will then launch the virtual device and install the app. When that's done the app will launch.

## Tips & Tricks

##### Tools used: 
Gradle, SQLlite3, Android Platform Tools

##### Using Android Platform Tools:
Change emulator-5554 shell to your version of emulator

```
adb -s emulator-5554 shell
cd data/data/com.typeof.flickpicker/databases/
sqlite3 FlickPicker.db
```

Fancy database output:

```
.mode column
.headers on
```

Turn on internet connection for emulator:
```
adb shell svc data enable
```

### Contact us:
* Sebastian: sebnils@student.chalmers.se
* Adrian: adrlin@student.chalmers.se
* Jonatan: nylundj@student.chalmers.se
* Jonathan: jonran@student.chalmers.se

