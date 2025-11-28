package com.crispim.coverspin

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

enum class VolumeDirection {
    Up,
    Down
}

class RecentAppsService : AccessibilityService() {

    private var pendingVolumeRunnable: Runnable? = null
    private val clickDelay = 400L
    private val handler = Handler(Looper.getMainLooper())

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        val action = event.action
        val keyCode = event.keyCode

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (action == KeyEvent.ACTION_DOWN) {
                if (pendingVolumeRunnable != null) {
                    handler.removeCallbacks(pendingVolumeRunnable!!)
                    pendingVolumeRunnable = null
                    openRecentApps()
                    return true
                }
                else {
                    pendingVolumeRunnable = Runnable {
                        adjustVolume(VolumeDirection.Up)
                        pendingVolumeRunnable = null
                    }
                    handler.postDelayed(pendingVolumeRunnable!!, clickDelay)
                    return true
                }
            }
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && action == KeyEvent.ACTION_DOWN) {
            if (pendingVolumeRunnable != null) {
                handler.removeCallbacks(pendingVolumeRunnable!!)
                pendingVolumeRunnable = null
                performGlobalAction(GLOBAL_ACTION_HOME)
                return true
            }
            else {
                pendingVolumeRunnable = Runnable {
                    adjustVolume(VolumeDirection.Down)
                    pendingVolumeRunnable = null
                }
                handler.postDelayed(pendingVolumeRunnable!!, clickDelay)
                return true
            }
        }

        return super.onKeyEvent(event)
    }

    private fun openRecentApps() {
        try {
            val samsungIntent = Intent()
            samsungIntent.component = android.content.ComponentName(
                "com.sec.android.app.launcher",
                "com.android.quickstep.RecentsActivity"
            )
            samsungIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            samsungIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED) // Tenta forçar reinício

            val options = android.app.ActivityOptions.makeBasic()
            val displayManager = getSystemService(Context.DISPLAY_SERVICE) as android.hardware.display.DisplayManager
            val targetDisplay = displayManager.getDisplay(1)

            if (targetDisplay != null) {
                options.launchDisplayId = targetDisplay.displayId
                startActivity(samsungIntent, options.toBundle())
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun adjustVolume(direction: VolumeDirection) {
        try {
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val adjustment = if (direction == VolumeDirection.Up) {
                AudioManager.ADJUST_RAISE
            } else {
                AudioManager.ADJUST_LOWER
            }

            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                adjustment,
                AudioManager.FLAG_SHOW_UI // Mostra a barra de volume na tela
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
