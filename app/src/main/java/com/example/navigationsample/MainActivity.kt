package com.example.navigationsample

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurações para permitir execução sobre bloqueio (caso seja reaberta pelo receiver)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        // Verifica se temos a permissão essencial
        if (!checkOverlayPermission(this)) {
            // Se NÃO tem permissão, precisamos pedir.
            // Neste caso único, precisamos de UI ou redirecionamento direto.
            // Vamos redirecionar direto para a tela de configurações.
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            // Fecha a activity para não ficar uma tela branca vazia esperando
            finish()
            return
        }

        // Se JÁ tem permissão:
        startRotationService()

        // O "Segredo" da transparência na Main:
        // Não chamamos setContent. Apenas fechamos a Activity visual.
        finish()
    }

    private fun startRotationService() {
        val intent = Intent(this, RotationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun checkOverlayPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    // Necessário para o fluxo de "UnlockReceiver" reabrir o app sem criar pilhas visíveis
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Se for chamado novamente, garante que o serviço está rodando e fecha
        if (checkOverlayPermission(this)) {
            startRotationService()
        }
        finish()
    }
}
