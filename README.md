# FlickPicker

FlickPicker is a android based app, a social network and an rating and movierecommendations engine.
A project made by Chalmers University of Technology students in course: **TDA367 - Object-oriented programming project**

##### Group 22:
- Sebastian Nilsson
- Adrian Lindberg
- Jonatan Nylund
- Jonathan Sundkvist

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

