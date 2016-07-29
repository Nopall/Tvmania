package com.sakutv.sakutv;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.VideoControlsButtonListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.devbrackets.android.exomedia.ui.widget.VideoControls;
import com.sakutv.sakutv.model.Channel;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EMVideoView emVideoView;
    private String Videourl, Videotitle, url2;
    private boolean pausedInOnStop = false;
    private boolean mIsFinished = false;
    private Button btn1l;
    RealmResults<Channel> channels;
    private Channel channel;
    private Realm realm;
    private VideoControls videoControls;
    private RealmConfiguration mRealmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRealm();

        realm.beginTransaction();

        channel = realm.createObject(Channel.class);

        channel.setId(1);
        channel.setNama("Channel 1");
        channel.setDeskripsi("Ini channel 1");
        channel.setUrl("http://live.kurdstream.net:1935/liveTrans//myStream_360p/playlist.m3u8");

        channel.setId(2);
        channel.setNama("Channel 2");
        channel.setDeskripsi("Ini channel 2");
        channel.setUrl("http://vstream2.hadara.ps:1935/maan/maan_1/gmswf.m3u8");

        realm.commitTransaction();

        channels = realm.where(Channel.class).findAll();

        System.out.println(channels);

        emVideoView = (EMVideoView)findViewById(R.id.video_view);

        setupVideoView();

    }

    public void setupVideoView() {

        for (final Channel ch : channels) {

            emVideoView.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared() {
                    emVideoView.start();
                }
            });

            emVideoView.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion() {
                    mIsFinished = true;
                }
            });


            videoControls = emVideoView.getVideoControls();
            videoControls.show();
//            videoControls.setTitle(ch.getNama());
            videoControls.setButtonListener(new VideoControlsButtonListener() {
                @Override
                public boolean onPlayPauseClicked() {
                    if (mIsFinished) {
                        emVideoView.setVideoURI(Uri.parse(ch.getUrl()));
                        mIsFinished = false;
                    }
                    return false;
                }

                @Override
                public boolean onPreviousClicked() {
                    return false;
                }

                @Override
                public boolean onNextClicked() {
                    return false;
                }

                @Override
                public boolean onRewindClicked() {
                    return false;
                }

                @Override
                public boolean onFastForwardClicked() {
                    return false;
                }
            });

            emVideoView.setVideoURI(Uri.parse(ch.getUrl()));

        }
    }

    public void onClick(View v) {



        switch(v.getId()){

            case R.id.btnchannel2: /** Start a new Activity MyCards.java */
                videoControls.setTitle(ch.getNama());
                emVideoView.setVideoURI(Uri.parse());
                break;

            case R.id.btnchannel3: /** AlerDialog when click on Exit */

                break;
        }

    private void initRealm() {
        mRealmConfig = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(mRealmConfig);
    }

}
