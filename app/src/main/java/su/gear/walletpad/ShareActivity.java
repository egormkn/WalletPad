package su.gear.walletpad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Андрей on 16.12.2016.
 */

public class ShareActivity extends AppCompatActivity {

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_share);

        Intent intent = getIntent ();
        String action = intent.getAction ();
        String type   = intent.getType ();

        if (intent.ACTION_SEND.equals (action) && type != null) {
            if ("text/plain".equals (type)) { handleMessage (intent); }
        }
    }

    private void handleMessage (Intent intent) {
        String sharedText = intent.getStringExtra (Intent.EXTRA_TEXT);
        if (sharedText != null && sharedText.length () > 0) {
            ((TextView) findViewById (R.id.share_text)).setText ("Link: " + sharedText);
        } else {
            ((TextView) findViewById (R.id.share_text)).setText ("Empty shared text ...");
        }
    }

}
