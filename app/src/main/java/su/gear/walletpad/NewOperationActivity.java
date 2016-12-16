package su.gear.walletpad;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import su.gear.walletpad.model.Operation;

public class NewOperationActivity extends AppCompatActivity {

    private static final String TAG = NewOperationActivity.class.getSimpleName();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Operation");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            Operation operation = new Operation(1, Operation.Type.INCOME, "RUB", 1000, "Описание", "Категория", new ArrayList<String>(), new Date().getTime());
/*
this.id = id;
        this.type = type;
        this.currency = Currency.getInstance(currencyCode);
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.date = new Date(timestamp);
 */

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*
    private void submitPost() {

        final String title = ((EditText) findViewById(R.id.description)).getText().toString();

        final String body = ((EditText) findViewById(R.id.tags)).getText().toString();



        // Title is required

        if (TextUtils.isEmpty(title)) {

            //mTitleField.setError(REQUIRED);

            return;

        }



        // Body is required

        if (TextUtils.isEmpty(body)) {

            //mBodyField.setError(REQUIRED);

            return;

        }



        // Disable button so there are no multi-posts

        setEditingEnabled(false);

        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();



        // [START single_value_read]

        final String userId = user.getUid();

        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(

                new ValueEventListener() {

                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {


                        // [START_EXCLUDE]

                        if (user == null) {

                            // User is null, error out

                            Log.e(TAG, "User " + userId + " is unexpectedly null");

                            Toast.makeText(NewOperationActivity.this,

                                    "Error: could not fetch user.",

                                    Toast.LENGTH_SHORT).show();

                        } else {

                            // Write new post

                            writeNewPost(userId, user.getDisplayName(), title, body);

                        }



                        // Finish this Activity, back to the stream

                        setEditingEnabled(true);

                        finish();

                        // [END_EXCLUDE]

                    }



                    @Override

                    public void onCancelled(DatabaseError databaseError) {

                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());

                        // [START_EXCLUDE]

                        setEditingEnabled(true);

                        // [END_EXCLUDE]

                    }

                });

        // [END single_value_read]

    }



    private void setEditingEnabled(boolean enabled) {

        //mTitleField.setEnabled(enabled);

        //mBodyField.setEnabled(enabled);

        if (enabled) {

            //mSubmitButton.setVisibility(View.VISIBLE);

        } else {

            //mSubmitButton.setVisibility(View.GONE);

        }

    }



    // [START write_fan_out]

    private void writeNewPost(String userId, String username, String title, String body) {

        // Create new post at /user-posts/$userid/$postid and at

        // /posts/$postid simultaneously

        String key = mDatabase.child("posts").push().getKey();

        /*Post post = new Post(userId, username, title, body);

        Map<String, Object> postValues = post.toMap();



        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/posts/" + key, postValues);

        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);



        mDatabase.updateChildren(childUpdates);

    }*/

}
