package de.hawlandshut.pluto25_gkw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManageAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "xxx ManageAccountActivity";

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
        Toast.makeText(getApplicationContext(), "Pressed Send Veri", Toast.LENGTH_LONG).show();
    }

    private void doSignOut() {
        Toast.makeText(getApplicationContext(), "Pressed SignOut", Toast.LENGTH_LONG).show();
    }

    private void doDeleteAccount() {
        Toast.makeText(getApplicationContext(), "Pressed Delete Account", Toast.LENGTH_LONG).show();
    }
}