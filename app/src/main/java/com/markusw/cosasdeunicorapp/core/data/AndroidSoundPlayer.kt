package com.markusw.cosasdeunicorapp.core.data

import android.content.Context
import android.media.MediaPlayer
import com.markusw.cosasdeunicorapp.core.domain.AppSound
import com.markusw.cosasdeunicorapp.core.domain.SoundPlayer

class AndroidSoundPlayer(
    private val context: Context
): SoundPlayer {

    override fun playSound(sound: AppSound) {
        MediaPlayer.create(context, sound.resource).start()
    }

}