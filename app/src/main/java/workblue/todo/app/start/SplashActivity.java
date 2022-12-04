package workblue.todo.app.start;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import workblue.todo.app.MainActivity;
import workblue.todo.app.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        halderSplash();
    }

    private void halderSplash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 3000);
    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
        {
            //Chưa login
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }else{
            //Đã login
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
        finish();
    }
}