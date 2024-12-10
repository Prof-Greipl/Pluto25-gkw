package de.hawlandshut.pluto25_gkw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener
{
    FirebaseAuth mAuth;
    // 3.a Declare UI Variables
    EditText mEditTextMail;
    EditText mEditTextPassword;
    Button mButtonSignIn;
    Button mButtonResetPassword;
    Button mButtonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        // 3.b Init UI Variable
        mEditTextMail = findViewById( R.id.signInEmail );
        mEditTextPassword = findViewById( R.id.signInPassword );
        mButtonSignIn = findViewById( R.id.signInButtonSignIn );
        mButtonResetPassword = findViewById( R.id.signinButtonResetPassword);
        mButtonCreateAccount = findViewById( R.id.signInButtonCreateAccount);

        // 3.c Listener für Button setzen
        mButtonSignIn.setOnClickListener( this );
        mButtonCreateAccount.setOnClickListener( this );
        mButtonResetPassword.setOnClickListener( this );

        // TODO: Only for testing - remove later
        mEditTextMail.setText("fhgreipl@gmail.com");
        mEditTextPassword.setText("123456");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.signInButtonSignIn){
            doSignIn();
        }

        if (i == R.id.signinButtonResetPassword){
            doResetPassword();
        }

        if (i == R.id.signInButtonCreateAccount){
            doCreateAccount();
        }
    }

    private void doCreateAccount() {
        Intent intent = new Intent(getApplication(), CreateAccountActivity.class);
        startActivity(intent);
    }

    private void doResetPassword() {
        String email = mEditTextMail.getText().toString();
        mAuth.sendPasswordResetEmail( email )
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

    private void doSignIn() {
        FirebaseUser user = mAuth.getCurrentUser();
        if ( user != null){ // Should never happen.
            Toast.makeText(getApplicationContext(),
                    "Please sign out first :" +  user.getEmail(),
                    Toast.LENGTH_LONG).show();
            return;
        }

        String email, password;
        email = mEditTextMail.getText().toString();
        password = mEditTextPassword.getText().toString();
        // Add validations! (Exercise)
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "User signed in.",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),
                                            "Sign in failed : " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

    }
}