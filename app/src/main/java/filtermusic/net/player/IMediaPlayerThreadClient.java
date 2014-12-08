package filtermusic.net.player;

/**
 * Created by android on 11/10/14.
 */
public interface IMediaPlayerThreadClient
{

    /**
     * A callback made by a MediaPlayerThread onto its clients to indicate that a player is initializing.
     */
    public void onInitializePlayerStart();

    /**
     * A callback made by a MediaPlayerThread onto its clients to indicate that a player was successfully initialized.
     */
    public void onPlaying();

    /**
     * A callback made by a MediaPlayerThread onto its clients to indicate that a player has stopped.
     */
    public void onStop();
    /**
     *  A callback made by a MediaPlayerThread onto its clients to indicate that a player encountered an error.
     */
    public void onError();
}