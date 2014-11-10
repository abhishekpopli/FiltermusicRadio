package filtermusic.net.player;

import android.content.Context;
import android.content.Intent;

/**
 * Many well-written applications that play audio automatically stop playback when an event occurs
 * that causes the audio to become noisy (ouput through external speakers). For instance,
 * this might happen when a user is listening to music through headphones and accidentally
 * disconnects the headphones from the device. However, this behavior does not happen automatically.
 * If you don't implement this feature, audio plays out of the device's external speakers,
 * which might not be what the user wants.
 */
public class MusicIntentReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
            // signal your service to stop playback
            // (via an Intent, for instance)
            Intent intentStopService = new Intent(context, MediaPlayerService.class);
            context.stopService(intentStopService);
        }
    }
}