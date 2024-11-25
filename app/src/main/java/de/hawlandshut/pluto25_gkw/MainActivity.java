package de.hawlandshut.pluto25_gkw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hawlandshut.pluto25_gkw.test.Testdata;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "xxx MainActivity";

    RecyclerView mRecyclerView;
    CustomAdapter mAdapter;

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

        // TODO: Remove later, only for testing
        mAdapter.mPostList = Testdata.createPostList(30);

        mRecyclerView = findViewById( R.id.recycler_view);
        mRecyclerView.setLayoutManager( new LinearLayoutManager(this) );
        mRecyclerView.setAdapter( mAdapter );

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.MenuMainManageAccount) {
            Intent intent = new Intent(getApplication(), ManageAccountActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.MenuMainSignIn) {
            Intent intent = new Intent(getApplication(), SignInActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.MenuMainPost) {
            Intent intent = new Intent(getApplication(), PostActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.MenuMainHelp) {
            Toast.makeText(getApplicationContext(), "Help...", Toast.LENGTH_LONG).show();
        }
        return true;
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