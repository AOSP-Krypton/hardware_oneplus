/*
 * Copyright (C) 2021 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.device

import android.content.Context
import android.media.AudioManager
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.KeyEvent
import com.android.internal.os.DeviceKeyHandler

class KeyHandler(context: Context) : DeviceKeyHandler {
    private val audioManager = context.getSystemService(AudioManager::class.java)
    private val vibrator = context.getSystemService(Vibrator::class.java)

    override fun handleKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.scanCode) {
                MODE_NORMAL -> {
                    audioManager.ringerModeInternal = AudioManager.RINGER_MODE_NORMAL
                    vibrator.vibrate(MODE_NORMAL_EFFECT)
                    vibrator.vibrate(
                        MODE_NORMAL_EFFECT,
                        HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES
                    )
                    return true
                }
                MODE_VIBRATION -> {
                    audioManager.ringerModeInternal = AudioManager.RINGER_MODE_VIBRATE
                    vibrator.vibrate(
                        MODE_VIBRATION_EFFECT,
                        HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES
                    )
                    return true
                }
                MODE_SILENCE -> {
                    audioManager.ringerModeInternal = AudioManager.RINGER_MODE_SILENT
                    return true
                }
            }
        }
        return false
    }

    companion object {
        private const val TAG = "KeyHandler"

        // Slider key codes
        private const val MODE_NORMAL = 601
        private const val MODE_VIBRATION = 602
        private const val MODE_SILENCE = 603

        // Vibration attributes
        private val HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES =
            VibrationAttributes.createForUsage(VibrationAttributes.USAGE_HARDWARE_FEEDBACK)

        // Vibration effects
        private val MODE_NORMAL_EFFECT = VibrationEffect.get(VibrationEffect.EFFECT_HEAVY_CLICK)
        private val MODE_VIBRATION_EFFECT = VibrationEffect.get(VibrationEffect.EFFECT_DOUBLE_CLICK)
    }
}
