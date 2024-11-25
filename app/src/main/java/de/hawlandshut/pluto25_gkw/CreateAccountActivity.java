package de.hawlandshut.pluto25_gkw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mEditTextEMail;
    private EditText mEditTextPassword1;
    private EditText mEditTextPassword2;
    private Button mButtonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mEditTextEMail =  findViewById(R.id.createAccountEmail);
        mEditTextPassword1 =  findViewById(R.id.createAccountPassword1);
        mEditTextPassword2 =  findViewById(R.id.createAccountPassword2);
        mButtonCreateAccount = findViewById( R.id.createAccountButtonCreateAccount);

        // TODO: Presets for testing - remove for prod
        mEditTextEMail.setText("xxx@gmail.com");
        mEditTextPassword1.setText("123456");
        mEditTextPassword2.setText("123456");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.createAccountButtonCreateAccount) {
            doCreateAccount();
        }
    }

    private void doCreateAccount() {

    }
}