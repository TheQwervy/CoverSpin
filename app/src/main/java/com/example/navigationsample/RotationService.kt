package com.example.navigationsample

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.IBinder

class RotationService : Service() {

    private var unlockReceiver: BroadcastReceiver? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        launchEngine(this)
        registerUnlockReceiver()
    }

    // Adicionado: Monitora mudanças de configuração (Abrir/Fechar aparelho)
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        launchEngine(this)
    }

    private fun registerUnlockReceiver() {
        unlockReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Intent.ACTION_USER_PRESENT || intent.action == Intent.ACTION_SCREEN_ON) {
                    launchEngine(context)
                }
            }
        }
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_USER_PRESENT)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        registerReceiver(unlockReceiver, filter)
    }

    private fun checkAndLaunchEngine(context: Context) {
        val config = context.resources.configuration

        // Lógica simples para detectar Cover Screen vs Tela Interna
        // Z Flip fechado geralmente tem "smallestScreenWidthDp" menor que 600dp (geralmente ~320-360)
        // Z Flip aberto é bem alto, mas a largura continua similar.
        // O melhor check para dobráveis é verificar se a tela é "longa" ou usar screenLayout.

        // No entanto, para simplificar: Se a tela está ligada e desbloqueada, aplicamos.
        // O problema da Falha 4.2 é que o overlay atrapalha a tela interna?
        // Se sim, precisamos matar o overlay quando abrir.
        // Mas como o overlay é gerenciado pela EngineActivity que já morreu, precisamos enviar um sinal para remover.

        // Vamos manter simples: Sempre lançar. A EngineActivity é inteligente o suficiente para verificar display?
        // NÃO. Vamos melhorar a EngineActivity para se matar se for tela interna.

        // Por enquanto, apenas lance:
        launchEngine(context)
    }

    private fun launchEngine(context: Context) {
        val engineIntent = Intent(context, EngineActivity::class.java)
        engineIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(engineIntent)
    }

    private fun startForegroundServiceNotif() {
        val channelId = "rotation_service_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Rotation Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Rotação Ativa")
            .setContentText("Controlando orientação da tela")
            .setSmallIcon(android.R.drawable.ic_menu_rotate)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (unlockReceiver != null) unregisterReceiver(unlockReceiver)
    }
}
