package workblue.todo.app.start;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import workblue.todo.app.R;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout nameTextInputSU, emailTextInputSU, passwordTextInputSU;
    TextInputEditText nameEditTextSU, emailEditTextSU, passwordEditTextSU;
    Button btnSignUp;
    ProgressBar progressBar;
    LinearLayout ToLogPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        initUi();
        changeInProcess(false);
        createUser();
        changeToLoginPage();

    }

    private void createUser() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        String name = nameEditTextSU.getText().toString().trim();
        String email = emailEditTextSU.getText().toString().trim();
        String password = passwordEditTextSU.getText().toString().trim();

        if(TextUtils.isEmpty(name))
        {
            nameTextInputSU.setError("Name is required");
        }
        else {
            nameTextInputSU.setError(null);
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailTextInputSU.setError("Email is invalid");
        } else {
            emailEditTextSU.setError(null);
        }

        if(password.length() <=8)
        {
            passwordTextInputSU.setError("Enter at least 8 characters");
        } else {
            changeInProcess(true);
            passwordTextInputSU.setError(null);
            Auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            changeInProcess(false);
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Sign up successful!", Toast.LENGTH_LONG).show();
                                Toast.makeText(RegisterActivity.this, "Check your email to verify", Toast.LENGTH_LONG).show();
                                Auth.getCurrentUser().sendEmailVerification();
                                NavigateToLog(); //Điều hướng tới trang đăng nhập
                                finishAffinity();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void initUi() // ánh xạ
    {
        nameTextInputSU =  findViewById(R.id.nameSU_text_input);
        nameEditTextSU =  findViewById(R.id.nameSU_edit_text);
        emailTextInputSU =  findViewById(R.id.emailSU_text_input);
        emailEditTextSU =  findViewById(R.id.emailSU_edit_text);
        passwordTextInputSU =  findViewById(R.id.passwordSU_text_input);
        passwordEditTextSU = findViewById(R.id.passwordSU_edit_text);
        btnSignUp =  findViewById(R.id.btnSU);
        progressBar = findViewById(R.id.progressBarSU);
        ToLogPage =  findViewById(R.id.tvToLogin);

    }

    private void changeInProcess(boolean inProgress) // thanh progess và button
    {
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.VISIBLE);
        }
    }

    private void changeToLoginPage() //Sang trang đăng nhập
    {
        ToLogPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigateToLog();
            }
        });
    }

    private void NavigateToLog() //điều hướng
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}