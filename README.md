# CoverSpin 🔄

**CoverSpin** is a lightweight Android utility designed to manage and force screen rotation on foldable devices.

The main goal is to allow screen orientation (Landscape/Portrait/Sensor) to work correctly on external screens (cover screens) where the native OS might impose restrictions.

## Features

- **Invisible Control Overlay:** Uses a transparent `View` of type `TYPE_APPLICATION_OVERLAY` to inject orientation parameters into the WindowManager.
- **Screen Detection:** Automatically closes if started on the main internal screen (`displayId == 0`) to save resources and focus only on the secondary screen.
- **Background Execution:** The app moves to the background (`moveTaskToBack`) immediately after initialization, keeping the overlay active without occupying the user interface.
- **Lock Screen Support:** Configured to work even when the device is locked (`FLAG_SHOW_WHEN_LOCKED`).

## The Rotation Secret
Android generally prioritizes the orientation of the top-most window. CoverSpin creates an invisible 0x0 pixel window with high priority and sets the orientation on it.

This forces the Android `WindowManager` to respect the orientation defined by the sensor (gyroscope/accelerometer), allowing rotation on screens that would normally be locked in Portrait mode.

## Requirements

- Min SDK: usually 26+ for foldables
- To work, the app requires the overlay permission (Display over other apps).

## Setup and Installation

1. Clone the repository.
2. Open in **Android Studio**.
3. Build and install on your device.
4. **Important:** On the first run, you must manually grant the "Display over other apps" permission in Android settings so that `EngineActivity` can create the overlay.

## Roadmap & Known Issues 🚧

- **Bug Fix:** The aspect ratio icon is misaligned when the cover screen is rotated to landscape mode.
- **Feature:** Plan to include the Recent Apps view

## Contribution
Contributions are welcome! If you have a foldable device (Galaxy Z Flip, Moto Razr, etc.) and want to test or improve sensor behavior, feel free to open an Issue or Pull Request.
