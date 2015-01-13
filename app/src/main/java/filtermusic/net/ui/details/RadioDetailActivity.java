package filtermusic.net.ui.details;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.R;
import filtermusic.net.common.model.Radio;
import filtermusic.net.player.IMediaPlayerServiceListener;
import filtermusic.net.player.PlayerController;

/**
 * Activity used to display information about a radio.
 */
public class RadioDetailActivity extends ActionBarActivity implements IMediaPlayerServiceListener {

    private static final String LOG_TAG = RadioDetailActivity.class.getSimpleName();

    public static final String INTENT_RADIO_PLAYING = "radio_playing";

    private ImageView mRadioImage;
    private TextView mRadioTitle;
    private TextView mRadioDescription;

    private ImageView mPlayButton;
    private ImageView mStarButton;
    private ProgressBar mLoadingProgress;

    private RadioDetailController mController;
    private PlayerController mPlayerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.radio_detail_view);

        if (getIntent().hasExtra(INTENT_RADIO_PLAYING)) {
            final String radioGson = getIntent().getStringExtra(INTENT_RADIO_PLAYING);
            try {
                Radio radio = new Gson().fromJson(radioGson, Radio.class);
                Log.d(LOG_TAG, "radio " + radio.toString());
                mController = new RadioDetailController(radio);
            }catch(JsonSyntaxException e){
                Log.d(LOG_TAG, "json syntax ex");
            }
        }

        if (mController != null && mController.getRadio() != null) {
            mPlayerController = PlayerController.getInstance();
            initUI();
            initRadioViews(mController.getRadio());
        }

    }

    /**
     * Initializes the views and sets up the click listeners
     */
    private void initUI() {

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        actionBar.setBackgroundDrawable(null);

        mRadioImage = (ImageView) findViewById(R.id.radio_image);
        mRadioTitle = (TextView) findViewById(R.id.radio_title);
        mRadioDescription = (TextView) findViewById(R.id.radio_description);

        mLoadingProgress = (ProgressBar) findViewById(R.id.loading_progress);

        mPlayButton = (ImageView) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isPlaying = mPlayerController.isRadioPlaying(mController.getRadio
                                        ());
                        if (!isPlaying) {
                            mPlayButton.setVisibility(View.GONE);
                            mLoadingProgress.setVisibility(View.VISIBLE);
                            mPlayerController.play(mController.getRadio());
                        } else {
                            mPlayerController.pause();
                        }
                    }
                });


        mStarButton = (ImageView) findViewById(R.id.star_button);
        mStarButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mController.setRadioAsFavorite();

                        int star = mController.getRadio().isFavorite() ? R.drawable.star : R
                                .drawable.star_outline;
                        mStarButton.setImageResource(star);

                    }
                });
    }

    /**
     * Initializes the views based on a radio
     * @param radio the radio from where the data for the view is taken
     */
    private void initRadioViews(final Radio radio) {
        Picasso.with(this).load(radio.getImageUrl()).placeholder(R.drawable.station_image).into
                (mRadioImage);

        mRadioTitle.setText(radio.getTitle());
        mRadioDescription.setText(Html.fromHtml(radio.getDescription()));

        int star = radio.isFavorite() ? R.drawable.star : R.drawable.star_outline;
        mStarButton.setImageResource(star);

        mPlayButton.setVisibility(View.VISIBLE);
        mLoadingProgress.setVisibility(View.GONE);
        if (mPlayerController.isRadioPlaying(radio)) {
            mPlayButton.setImageResource(R.drawable.pause_circle_fill);
        } else {
            mPlayButton.setImageResource(R.drawable.play_circle);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlayerController.getInstance().removeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlayerController.getInstance().addListener(this);
    }

    @Override
    public void onInitializePlayerStart(Radio radio) {
        Log.d(LOG_TAG, "onInitializePlayerStart");
    }

    @Override
    public void onPlaying(Radio radio) {
        if (mController.getRadio() != null && mController.getRadio().equals(radio)) {
            Log.d(LOG_TAG, "onPlaying " + radio.getTitle());
            mLoadingProgress.setVisibility(View.GONE);
            mPlayButton.setVisibility(View.VISIBLE);
            mPlayButton.setImageResource(R.drawable.pause_circle_fill);
        }
    }

    @Override
    public void onError() {
        mPlayButton.setImageResource(R.drawable.play_circle);

    }

    @Override
    public void onPlayerStop() {
        mPlayButton.setImageResource(R.drawable.play_circle);
    }

    @Override
    public void onTrackChanged(String track) {

    }
}
