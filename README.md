# CoverSpin 🔄

**CoverSpin** is a lightweight Android utility designed to manage and force screen rotation on foldable devices (specifically tested on Galaxy Z Flip 7)

The main goal is to allow screen orientation (Landscape/Portrait/Sensor) to work correctly on external screens (cover screens) where the native OS might impose restrictions.

## Setup and Installation
1. Download the latest [release](https://github.com/crispim1411/CoverSpin/releases) and extract it
2. Install de apk file
3. Accept the permissions
4. 3. **Important:** Add the app to **Good Lock (MultiStar)** on the Cover Screen launcher.
5. Open the app on the Cover Screen to initialize it.
6. Done

*OBS: If the rotation stops working, initialize CoverSpin on the cover screen again via Good Lock.*

## Features

The app intercepts **Double Clicks** on the physical volume buttons to control rotation without needing to open the app again.

| Button | Action (Double Click) | Result |
| :--- | :--- | :--- |
| **Volume Up (+)** | 2x Fast Click | **ENABLE** Forced Rotation (Sensor Mode) |
| **Volume Down (-)** | 2x Fast Click | **DISABLE** Forced Rotation (System Default) |

> *Note: Single clicks still adjust the volume normally.*

- **Shortcuts:** Toggle rotation on/off using physical volume buttons.
- **Intelligent State:** Automatically pauses rotation when the screen is locked (to prevent Lockscreen bugs) and restores your preference upon unlocking.
- **Visual Feedback:** Displays custom overlay toasts on the cover screen to confirm when rotation is enabled/disabled.
- **Invisible Control Overlay:** Uses a transparent `View` of type `TYPE_APPLICATION_OVERLAY` to inject orientation parameters into the WindowManager.
- **Background Execution:** Runs as a lightweight Accessibility Service (`EventsService`) to monitor keys and screen state.

### Requirements

- **Min SDK:** Android 8.0+ (Oreo).
- **Permissions:** Overlay Permission & Accessibility Service.

### Roadmap & Known Issues 🚧

- **Bug Fix:** The aspect ratio icon/UI elements might look misaligned when the cover screen is rotated to landscape mode (Samsung launcher limitation).
- **In Progress:** Feature to open "Recent Apps" via shortcut is partially implemented but currently disabled/under testing.

## Contribution
Contributions are welcome! If you have a foldable device (Galaxy Z Flip, Moto Razr, etc.) and want to test or improve sensor behavior, feel free to open an Issue or Pull Request.

If you want to support the development:
[Help me buy ice cream 🍦](https://www.paypal.com/paypalme/crispim1411)