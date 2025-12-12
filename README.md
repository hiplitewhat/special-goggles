# Floating Icon Overlay - Android Application

A system-level floating icon overlay for Android that appears above all other applications. The floating icon remains accessible and can be dragged across the screen.

## Features

✅ **System-wide Visibility**: The floating icon is visible across all applications
✅ **Persistent & Non-blocking**: Persists without blocking user interaction
✅ **Draggable**: Users can drag the icon to any position on screen
✅ **Interactive**: Responds to tap/click actions with feedback
✅ **Proper Permissions**: Requests and handles SYSTEM_ALERT_WINDOW permission
✅ **Lifecycle Management**: Proper service lifecycle with foreground notification
✅ **Configurable**: Settings for appearance and behavior persistence
✅ **Crash-free**: Handles edge cases and prevents ANRs

## Architecture

### Core Components

1. **FloatingIconService** (`service/FloatingIconService.kt`)
   - Foreground service that manages the overlay lifecycle
   - Creates and maintains a foreground notification
   - Manages FloatingIconManager instance
   - Ensures overlay persistence even when app is backgrounded

2. **FloatingIconManager** (`service/FloatingIconManager.kt`)
   - Manages the WindowManager overlay window
   - Handles view inflation and attachment to window manager
   - Implements touch listener for drag and click detection
   - Uses TYPE_APPLICATION_OVERLAY for system-wide visibility
   - Properly toggles FLAG_NOT_TOUCHABLE for drag interactions

3. **FloatingIconConfig** (`config/FloatingIconConfig.kt`)
   - SharedPreferences wrapper for persistent storage
   - Stores: enabled state, opacity, size, position
   - Easy configuration interface for feature toggles

4. **MainActivity** (`MainActivity.kt`)
   - Entry point and control interface
   - Handles permission requests and checks
   - Starts/stops the FloatingIconService
   - Shows current service status

## Permissions Required

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

- **SYSTEM_ALERT_WINDOW**: Required to draw overlay above all apps (API 23+)
- **FOREGROUND_SERVICE**: Required for the background service

## Building

### Prerequisites
- Android Studio or Android SDK
- Gradle 8.0+
- Kotlin 1.9.0+
- Minimum SDK: 21
- Target SDK: 34

### Build Steps

```bash
# Clone the repository
cd FloatingIconOverlay

# Build the debug APK
./gradlew assembleDebug

# Build the release APK
./gradlew assembleRelease

# Run tests
./gradlew test
```

## Installation

1. Build the APK: `./gradlew assembleDebug`
2. Install: `adb install app/build/outputs/apk/debug/app-debug.apk`
3. Launch the app and tap "Start Overlay"
4. Grant permission when prompted

## Usage

### Starting the Overlay

1. Open the Floating Icon Overlay app
2. Tap the "Start Overlay" button
3. When prompted, grant the "Display over other apps" permission
4. The floating icon will appear on screen

### Interacting with the Icon

- **Drag**: Touch and drag the icon to move it anywhere on screen
- **Tap**: Single tap to activate action (shows toast message)
- **Automatic Persistence**: Position and settings are saved

### Stopping the Overlay

1. Open the app again
2. Tap the "Stop Overlay" button
3. The floating icon will disappear immediately

## Technical Details

### WindowManager Configuration

The overlay uses the following WindowManager.LayoutParams configuration:

```kotlin
type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY  // API 26+
format = PixelFormat.TRANSLUCENT
flags = FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCHABLE | FLAG_LAYOUT_NO_LIMITS
width = 56dp
height = 56dp
```

### Touch Handling

The implementation uses an intelligent touch handling approach:

1. On `ACTION_DOWN`: Enable touch by removing FLAG_NOT_TOUCHABLE
2. On `ACTION_MOVE`: Detect drag vs tap (threshold: 10dp), update position
3. On `ACTION_UP`: Handle click or finalize drag, restore FLAG_NOT_TOUCHABLE

### Service Lifecycle

- Service runs as foreground service with persistent notification
- Uses START_STICKY to restart if terminated by system
- Properly removes view on service destruction
- Handles edge cases (view already removed, etc.)

## Customization

### Changing Icon Size

Edit `app/src/main/res/layout/floating_icon_layout.xml`:

```xml
<FrameLayout
    android:layout_width="56dp"
    android:layout_height="56dp">
```

### Changing Icon Appearance

1. Modify `app/src/main/res/drawable/floating_icon_bg.xml` for background shape
2. Edit `floating_icon_layout.xml` to change text or add images
3. Update colors in `app/src/main/res/values/colors.xml`

### Changing Initial Position

In `FloatingIconManager.kt`:

```kotlin
x = dpToPx(20)  // Distance from left
y = dpToPx(100) // Distance from top
```

## API Level Compatibility

| Feature | Min API | Notes |
|---------|---------|-------|
| TYPE_APPLICATION_OVERLAY | 26 | Falls back to TYPE_PHONE for API 21-25 |
| SYSTEM_ALERT_WINDOW | 23+ | Runtime permission required |
| canDrawOverlays() | 23 | Requires Android M+ |

## Troubleshooting

### Permission Denied
- Ensure the app has "Display over other apps" permission
- Go to Settings > Apps > Floating Icon Overlay > Permissions
- Enable "Display over other apps"

### Icon Not Showing
- Verify service is running (check app status)
- Check logcat for errors
- Ensure minimum API level is 21

### App Crashes
- Check Android logcat for stack trace
- Verify all permissions are granted
- Clear app cache and data

### Performance Issues
- Ensure only one service instance is running
- Check for memory leaks in custom views
- Monitor CPU usage with profiler

## File Structure

```
FloatingIconOverlay/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/floatingicon/
│   │   │   ├── MainActivity.kt
│   │   │   ├── config/
│   │   │   │   └── FloatingIconConfig.kt
│   │   │   └── service/
│   │   │       ├── FloatingIconService.kt
│   │   │       └── FloatingIconManager.kt
│   │   ├── res/
│   │   │   ├── drawable/
│   │   │   │   └── floating_icon_bg.xml
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   └── floating_icon_layout.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   └── themes.xml
│   │   │   └── xml/
│   │   │       ├── backup_schemes.xml
│   │   │       └── data_extraction_rules.xml
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
├── gradlew
└── README.md
```

## Future Enhancements

- [ ] Custom notification actions
- [ ] Multiple floating icons
- [ ] Advanced animation effects
- [ ] Deep app linking from icon
- [ ] Haptic feedback on interaction
- [ ] Customizable icon themes
- [ ] More interactive gestures (long press, double tap)

## Testing

Manual testing checklist:
- [ ] App launches without crash
- [ ] Permission prompt appears
- [ ] Overlay appears after permission grant
- [ ] Icon is draggable
- [ ] Icon responds to taps
- [ ] Overlay persists when switching apps
- [ ] Stop button removes overlay
- [ ] No ANRs during operation

## License

This project is provided as-is for reference and educational purposes.

## Support

For issues or questions:
1. Check the Troubleshooting section
2. Review logcat output for error messages
3. Verify all permissions are properly granted
