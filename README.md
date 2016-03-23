# Cordova Progress View Plugin
=======================

Cordova+PhoneGap Extension for displaying a native determinate progress dialog.

=======================

# Contents

1. [Description](#description)
2. [Platforms](#platforms)
3. [Installation](#installation)
4. [Usage](#usage)
5. [API](#api)

=======================

## Description

Small Plugin for displaying a native progress dialog. Ideal for synchronous operations (downloading, zipping). Runnning on a background thread.

=======================

## Platforms

* iOS, 7.1.2+
* Android, 4.0.0+

=======================

## Installation

Via Cordova CLI:
```bash
cordova plugin add https://github.com/SidneyS/cordova-plugin-progressview.git
```

=======================

## Usage

1. Wait for `deviceReady`.
2. `show()` the native progress dialog.
2. Update progress via `setProgress()`.
3. `hide()` the native progress dialog.

=======================

## API

### show()

```javascript
window.plugins.ProgressView.show: function (viewLabel, viewShape, isIndeterminate, themeAndroid, successCallback, errorCallback)
```
Shows a progress dialog.

* params
     viewLabel - Dialog Title, defaults to 'Please Wait...'
     viewShape - "CIRCLE", "BAR"
     isIndeterminate - True / False
    themeAndroid -  (Android only) "TRADITIONAL", "DEVICE_DARK", "DEVICE_LIGHT", "HOLO_DARK", "HOLO_LIGHT"

### setProgress()

```javascript
window.plugins.ProgressView.setProgress: function (progressPercentage)
```
Updates displayed progress dialog percentage.

* params
 * progressPercentage - Floating point value (0.1 - 1.0), representing the percentage to be displayed.

### hide()
 
```javascript
window.plugins.ProgressView.hide: function ()
 ```
Hides progress dialog.


### setShape() Android only
 
```javascript
window.plugins.ProgressView.setShape: function (viewShape)
 ```
Change the progress style

 * params
    viewShape - "CIRCLE", "BAR"


 LOGCAT DEBUGS
 	to see logs in locat, use: adb logcat ProgressView:D *:S
