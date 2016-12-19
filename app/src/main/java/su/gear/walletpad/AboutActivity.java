package su.gear.walletpad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            throw new RuntimeException("Toolbar not available");
        }
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("About");

        ((TextView) findViewById (R.id.about_build)).setText ("Version: " + BuildConfig.VERSION_CODE);
        ((TextView) findViewById (R.id.about_build)).setText ("Build: " + BuildConfig.VERSION_NAME);

        findViewById (R.id.about_link_repository).setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                String uri = ((TextView) findViewById (R.id.about_link_repository))
                                .getText ()
                                .toString ()
                                .toLowerCase ();
                Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse (uri));
                startActivity (intent);
            }
        });
    }
}
