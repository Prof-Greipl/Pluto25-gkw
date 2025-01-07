package de.hawlandshut.pluto25_gkw;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity
implements View.OnClickListener{
    private static final String TAG = "xxx PostActivity";

    EditText mEditTextTitle;
    EditText mEditTextText;
    Button mButtonPost;
    FirebaseAuth mAuth;
    FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mButtonPost = findViewById( R.id.postButtonPost);
        mEditTextText = findViewById( R.id.postEditTextText);
        mEditTextTitle = findViewById( R.id.postEditTextTitle);

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        mButtonPost.setOnClickListener( this );
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i =  item.getItemId() ;
        if (i == android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.postButtonPost) {
            doPost();
        }
    }

    private void doPost() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            return; // This should never happen
        }
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("uid", user.getUid());
        postMap.put("email", user.getEmail());
        postMap.put("title", mEditTextTitle.getText().toString());
        postMap.put("body", mEditTextText.getText().toString());
        postMap.put("createdAt", FieldValue.serverTimestamp());

        mDb.collection("posts").add( postMap )
                .addOnCompleteListener(this,
                        new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()){
                                    DocumentReference ref = task.getResult();
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + ref.getId());
                                    Toast.makeText(getApplicationContext(),
                                            "Posted.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d(TAG, "Failed : "
                                            + task.getException().getMessage().toString() );
                                    Toast.makeText(getApplicationContext(),
                                            "Posting failed.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }
}