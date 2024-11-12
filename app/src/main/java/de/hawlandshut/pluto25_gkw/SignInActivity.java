package de.hawlandshut.pluto25_gkw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener
{
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
        mEditTextPassword.setText("111111");
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
        Toast.makeText(getApplicationContext(), "Pressed Create Account", Toast.LENGTH_LONG).show();
    }

    private void doResetPassword() {
        Toast.makeText(getApplicationContext(), "Pressed Reset Password", Toast.LENGTH_LONG).show();
    }

    private void doSignIn() {
        Toast.makeText(getApplicationContext(), "Pressed SignIn Button", Toast.LENGTH_LONG).show();
    }
}