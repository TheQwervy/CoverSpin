package com.crispim.coverspin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.core.net.toUri

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                "package:$packageName".toUri()
            )
            startActivityForResult(intent, 123)
        } else {
            startEngine()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Settings.canDrawOverlays(this)) {
            startEngine()
        } else {
            finish()
        }
    }

    private fun startEngine() {
        val intent = Intent(this, EngineActivity::class.java)
        startActivity(intent)
        finish()
    }
}
