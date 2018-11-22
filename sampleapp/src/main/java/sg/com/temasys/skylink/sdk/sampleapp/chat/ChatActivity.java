package sg.com.temasys.skylink.sdk.sampleapp.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import sg.com.temasys.skylink.sdk.sampleapp.R;

/**
 * Created by muoi.pham on 20/07/18.
 */
public class ChatActivity extends AppCompatActivity {

    private final String CHAT_FRAGMENT_TAG = "CHAT_FRAGMENT";

    private ChatPresenter mChatPresenter;
    private ChatFragment mChatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //create presenter
        mChatPresenter = new ChatPresenter(this);

        //check previous state in case of screen rotation
        if (savedInstanceState == null) {
            mChatFragment = ChatFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentFrameChat, mChatFragment, CHAT_FRAGMENT_TAG)
                    .commit();
        } else {
            mChatFragment = (ChatFragment) getSupportFragmentManager()
                    .findFragmentByTag(CHAT_FRAGMENT_TAG);
        }

        //link between view and presenter
        if (mChatFragment != null)
            mChatPresenter.setView(mChatFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance when changing configuration
        getSupportFragmentManager().putFragment(outState, CHAT_FRAGMENT_TAG, mChatFragment);
    }
}
