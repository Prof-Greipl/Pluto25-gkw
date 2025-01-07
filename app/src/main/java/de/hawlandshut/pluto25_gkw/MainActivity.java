package de.hawlandshut.pluto25_gkw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hawlandshut.pluto25_gkw.model.Post;
import de.hawlandshut.pluto25_gkw.test.Testdata;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "xxx MainActivity";

    // TODO: only for testing - delete later.
    private static final String TEST_MAIL = "fhgreipl@gmail.com";
    private static final String TEST_PASSWORD ="123456";

    RecyclerView mRecyclerView;
    CustomAdapter mAdapter;

    FirebaseAuth mAuth;
    FirebaseFirestore mDb;
    ListenerRegistration mListenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Adapter managen
        mAdapter = new CustomAdapter();

        mRecyclerView = findViewById( R.id.recycler_view);
        mRecyclerView.setLayoutManager( new LinearLayoutManager(this) );
        mRecyclerView.setAdapter( mAdapter );

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            if (mListenerRegistration != null){
                // Delete the listener
                mListenerRegistration.remove();
            }
            Intent intent = new Intent(getApplication(), SignInActivity.class);
            startActivity(intent);
        }
        else{
            mListenerRegistration = createMyEventListener();
            Log.d(TAG, "Listener created.");
        }
    }

    ListenerRegistration createMyEventListener(){
        // Step 1:
        Query query = mDb.collection( "posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(5);

        // Step 2: Define, how you want to process an info from the listener
        EventListener<QuerySnapshot> listener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                Log.d(TAG, "Documents received : " + snapshot.size());

                // Delete all entries in mPostList
                mAdapter.mPostList.clear();

                for (QueryDocumentSnapshot doc: snapshot){
                    Log.d(TAG, "Received post with ID " + doc.getId()+ " Email : " + doc.get("email") );

                    // Create post from doc...
                    Post newPost = Post.fromDocument( doc );

                    // .. and add it to mPostlist.
                    mAdapter.mPostList.add( newPost );
                }

                // Inform adapter to refresh UI, since PostList has changed
                mAdapter.notifyDataSetChanged();
            }
        };
        // Step 3: Link listener qwith query and return the listenerRegistration
        return query.addSnapshotListener(listener);
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.MenuMainPost) {
            Intent intent = new Intent(getApplication(), PostActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.MenuMainManageAccount) {
            Intent intent = new Intent(getApplication(), ManageAccountActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.MenuMainTestWrite) {
            Map<String, Object> testMap = new HashMap<>();
            testMap.put("key_str", "datastring");
            testMap.put("key_float2", 1.5);
            testMap.put("mein_int", 1);
            testMap.put("key_date", new Date());
            Log.d(TAG, "Date in testwrite :  " + new Date());

            mDb.collection("users").add( testMap )
                    .addOnCompleteListener(this,
                            new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()){
                                        DocumentReference ref = task.getResult();
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + ref.getId());
                                    } else {
                                        Log.d(TAG, "Failed : "
                                                + task.getException().getMessage().toString() );
                                    }
                                }
                            });

        }

        return true;
    }

    private void doSendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            Toast.makeText(getApplicationContext(),
                    "No user signed in, mail not sent.",
                    Toast.LENGTH_LONG).show();
        } else{
            user.sendEmailVerification()
                    .addOnCompleteListener(this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),
                                                "Verification E-Mail sent",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),
                                                "Verification sending failed : " + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }

    private void doSendPasswordResetEmail(String testMail) {
        mAuth.sendPasswordResetEmail( testMail)
                .addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "E-Mail sent",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),
                                            "Sending failed : " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }

    private void doDeleteUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            Toast.makeText(getApplicationContext(),
                    "Cannot delete - no user signed in",
                    Toast.LENGTH_LONG).show();
        } else {
            user.delete()
                    .addOnCompleteListener(this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),
                                                "User deleted.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),
                                                "Deletion failed : " + task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }

    private void doSignOut() {
        FirebaseUser user = mAuth.getCurrentUser();
        if ( user != null) {
            mAuth.signOut();
            Toast.makeText(getApplicationContext(),
                    "You are signed out",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),
                    "Please sign in first!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void doSignInUser(String testMail, String testPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if ( user != null){
            Toast.makeText(getApplicationContext(),
                    "Please sign out first :" +  user.getEmail(),
                    Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(testMail, testPassword)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "User signed in.",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),
                                            "Sign in failed : " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }

    private void doCreateUser(String testMail, String testPassword) {
        mAuth.createUserWithEmailAndPassword( testMail, testPassword)
            .addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "User created",
                                        Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),
                                        "Failed : " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
    }


    // TODO: Code noch rausnehmen!
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}