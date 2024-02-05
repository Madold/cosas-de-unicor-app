package com.markusw.cosasdeunicorapp.core.domain

/**
 * Created by Markus on 04-02-2024
 * Interface for playing sounds
 */
interface SoundPlayer {
    /**
     * Play a sound
     * @param sound the sound to play
     * @see AppSound
     */
    fun playSound(sound: AppSound)
}

