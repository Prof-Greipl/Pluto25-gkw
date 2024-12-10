package de.hawlandshut.pluto25_gkw;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ManageAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "xxx ManageAccountActivity";

    FirebaseAuth mAuth;
    // 3.a Declare UI Variables
    TextView mTextViewLineEmail;
    TextView mTextViewLineId;
    TextView mTextViewLineVerificationState;

    EditText mEditTextPassword;
    Button mButtonSignOut;
    Button mButtonSendActivationMail;
    Button mButtonDeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        // 3.b Initiate UI Variables
        mTextViewLineEmail = findViewById( R.id.manageAccountTextViewEmail);
        mTextViewLineId = findViewById( R.id.manageAccountTextViewId);
        mTextViewLineVerificationState = findViewById( R.id.manageAccountTextViewAccountState );

        mButtonSignOut = findViewById( R.id.manageAccountButtonSignOut);
        mButtonSendActivationMail = findViewById( R.id.manageAccountButtonSendVerificationMail);
        mButtonDeleteAccount = findViewById( R.id.manageAccountButtonDeleteAccount);
        mEditTextPassword = findViewById( R.id.manageAccountPassword);

        // 3.c Listener "this" für an Buttons anhängen
        mButtonDeleteAccount.setOnClickListener( this );
        mButtonSignOut.setOnClickListener( this );
        mButtonSendActivationMail.setOnClickListener( this );
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            finish(); // This should never happen.
        }

        mTextViewLineEmail.setText("Email :" + user.getEmail());
        mTextViewLineId.setText("User Id : " + user.getUid());
        if (user.isEmailVerified()) {
            mTextViewLineVerificationState.setText("Your account is verified");
            mButtonSendActivationMail.setEnabled( false );
        } else {
            mTextViewLineVerificationState.setText("Your account needs verification");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i =  item.getItemId() ;
        if (i == android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.manageAccountButtonDeleteAccount){
            doDeleteAccount();
        }

        if (i == R.id.manageAccountButtonSignOut){
            doSignOut();
        }

        if (i == R.id.manageAccountButtonSendVerificationMail){
            doSendVerificationMail();
        }

    }

    private void doSendVerificationMail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            finish(); // This should never happen.
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

    private void doSignOut() {
        mAuth.signOut();
        finish();
    }

    private void doDeleteAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            return; // This should never happen
        }

        String email = user.getEmail();
        String password = mEditTextPassword.getText().toString();
        // Validations für password (Übung!)
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate( credential).addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    "Reauth fine.",
                                    Toast.LENGTH_LONG).show();
                            finalDeletion();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Reauth failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void finalDeletion( ){
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
                                        finish();
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
}