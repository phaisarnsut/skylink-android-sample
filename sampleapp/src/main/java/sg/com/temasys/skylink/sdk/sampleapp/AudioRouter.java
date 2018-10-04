package sg.com.temasys.skylink.sdk.sampleapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;


/**
 * Simple AudioRouter that switches between the speaker phone and the headset
 */
public class AudioRouter {

    private static AudioRouter instance = null;

    private static final String TAG = AudioRouter.class.getName();
    private static BroadcastReceiver headsetBroadcastReceiver;
    private static BroadcastReceiver blueToothBroadcastReceiver;
    private static BroadcastReceiver blueToothAudioBroadcastReceiver;

    private static AudioManager audioManager;

    private AudioRouter() {
        headsetBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                    String logTag = "[SA][AR][onReceive] ";
                    String log;
                    int state = intent.getIntExtra("state", -1);
                    switch (state) {
                        case 0:
                            log = logTag + "Headset: Unplugged";
                            Log.d(TAG, log);
                            break;
                        case 1:
                            log = logTag + "Headset: Plugged";
                            Log.d(TAG, log);
                            break;
                        default:
                            log = logTag + "Headset: Error determining state!";
                            Log.d(TAG, log);
                    }
                    // Reset audio path
                    setAudioPath();
                }
            }
        };

        blueToothBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    String logTag = "[SA][headsetBroadcastReceiver][onReceive] ";
                    String log;
                    if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                        log = logTag + "Bluetooth: off";
                        Log.d(TAG, log);
                    }
                    // Reset audio path
                    setAudioPath();
                }
            }

        };

        blueToothAudioBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)) {
                    String logTag = "[SA][headsetBroadcastReceiver][onReceive] ";
                    String log;
                    int currentAudioState = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, -1);

                    if (currentAudioState == BluetoothHeadset.STATE_DISCONNECTED) {
                        log = logTag + "Bluetooth on headset: off";
                        Log.d(TAG, log);
                    } else if (currentAudioState == BluetoothHeadset.STATE_DISCONNECTING) {
                        log = logTag + "Bluetooth on headset: off";
                        Log.d(TAG, log);
                    } else if (currentAudioState == BluetoothHeadset.STATE_CONNECTED) {
                        log = logTag + "Bluetooth on headset: on";
                        Log.d(TAG, log);
                    } else if (currentAudioState == BluetoothHeadset.STATE_CONNECTING) {
                        log = logTag + "Bluetooth on headset: on";
                        Log.d(TAG, log);
                    }

                    // Reset audio path
                    setAudioPath();
                }
            }
        };
    }

    /**
     * Gets an instance of the AudioRouter
     *
     * @return
     */
    public static synchronized AudioRouter getInstance() {
        if (instance == null) {
            instance = new AudioRouter();
        }
        return instance;
    }

    /**
     * Initialize the Audio router
     *
     * @param audioManager
     */
    public void init(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    /**
     * Initialize AudioRouter and
     * start routing audio to headset if plugged, else to the speaker phone
     */
    public static void startAudioRouting(Context context) {
        String logTag = "[SA][AR][startAudioRouting] ";
        String log = logTag + "Trying to start audio routing...";
        Log.d(TAG, log);
        if (context == null) {
            log = logTag + "Failed as provided context does not exist!";
            Log.d(TAG, log);
            return;
        }
        Context appContext = context.getApplicationContext();
        if (appContext == null) {
            log = logTag + "Failed as could not get application context from provided context!";
            Log.d(TAG, log);
            return;
        }

        log = logTag + "Initializing Audio Router...";
        Log.d(TAG, log);
        initializeAudioRouter(context);

        log = logTag + "Registering receiver...";
        Log.d(TAG, log);
        // Must use applicationContext here and not Activity context.
        appContext.registerReceiver(headsetBroadcastReceiver,
                new IntentFilter(Intent.ACTION_HEADSET_PLUG));
        appContext.registerReceiver(blueToothBroadcastReceiver,
                new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        appContext.registerReceiver(blueToothAudioBroadcastReceiver,
                new IntentFilter(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED));

        log = logTag + "Setting Audio Path...";
        Log.d(TAG, log);
        setAudioPath();

        log = logTag + "Starting audio routing is complete.";
        Log.d(TAG, log);
    }

    /**
     * Stop routing audio
     */
    public static void stopAudioRouting(Context context) {
        String logTag = "[SA][AR][stopAudioRouting] ";
        String log = logTag + "Trying to stop audio routing...";
        Log.d(TAG, log);
        if (instance == null) {
            log = logTag + "Not stopping as AudioRouter does not exist.";
            Log.d(TAG, log);
            return;
        }
        if (context == null) {
            log = logTag + "Failed as provided context does not exist!";
            Log.d(TAG, log);
            return;
        }
        Context appContext = context.getApplicationContext();
        if (appContext == null) {
            log = logTag + "Failed as could not get application context from provided context!";
            Log.d(TAG, log);
            return;
        }

        try {
            audioManager = null;

            // Must use applicationContext here and not Activity context.
            context.getApplicationContext().unregisterReceiver(headsetBroadcastReceiver);
            context.getApplicationContext().unregisterReceiver(blueToothBroadcastReceiver);
            context.getApplicationContext().unregisterReceiver(blueToothAudioBroadcastReceiver);
            log = logTag + "Unregister receiver.";
            // Catch potential exception:
            // java.lang.IllegalArgumentException: Receiver not registered
        } catch (java.lang.IllegalArgumentException e) {
            log = logTag + "Unable to unregister receiver due to: " + e.getMessage();
        }
        Log.d(TAG, log);

        instance = null;
        log = logTag + "Audio Router instance removed. Stop audio is complete.";
        Log.d(TAG, log);
    }

    /**
     * Set the audio path according to whether earphone is connected. Use ear piece if earphone is
     * connected. Use speakerphone if no earphone is connected.
     */
    private static void setAudioPath() {
        String logTag = "[SA][AR][setAudioPath] ";
        String log = logTag + "Trying to set audio path...";
        Log.d(TAG, log);

        if (audioManager == null) {
            throw new RuntimeException(
                    "Attempt to set audio path before setting AudioManager");
        }
        boolean isWiredHeadsetOn = audioManager.isWiredHeadsetOn();
        boolean isBluetoothOn = audioManager.isBluetoothA2dpOn();

        if (isWiredHeadsetOn || isBluetoothOn) {
            log = logTag + "Setting Speakerphone to off as wired headset is on.";
            audioManager.setSpeakerphoneOn(false);
        } else {
            audioManager.setSpeakerphoneOn(true);
            log = logTag + "Setting Speakerphone to on as wired headset is off.";
        }
        Log.d(TAG, log);

        log = logTag + "Setting audio path is complete.";
        Log.d(TAG, log);
    }

    static void initializeAudioRouter(Context context) {
        String logTag = "[SA][AR][initializeAudioRouter] ";
        String log = logTag + "Trying to initialize Audio Router...";
        Log.d(TAG, log);

        if (instance != null) {
            log = logTag + "Not initializing as AudioRouter already exist!";
            Log.d(TAG, log);
            return;
        }

        getInstance();

        AudioManager audioManager = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));

        instance.init(audioManager);
        log = logTag + "Initializing audio router is complete.";
        Log.d(TAG, log);
    }

}