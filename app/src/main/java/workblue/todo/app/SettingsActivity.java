package workblue.todo.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MY_REQUEST_CODE = 10;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView topNavigationViewSettings;
    private EditText edtName, edtEmail;
    private Button btnUpdateProfile , btnUpdateEmail;
    private TextView tvName, tvEmail;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initUi();
        progressBar.setVisibility(View.GONE);
        setSupportActionBar(toolbar); //kích hoạt drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setUserInformation();
        settingsProfile();
        showUserInformation();

    }


    private void initUi() // ánh xạ
    {
        toolbar = findViewById(R.id.toolbar_settings);
        progressBar = findViewById(R.id.progressBarST);
        drawerLayout = findViewById(R.id.drawer_layout_settings);
        topNavigationViewSettings = findViewById(R.id.top_navigation_settings);
        topNavigationViewSettings.setNavigationItemSelectedListener(this);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        btnUpdateProfile = findViewById(R.id.btnUpdate_profile);
        btnUpdateEmail = findViewById(R.id.btnUpdate_email);
        tvName = topNavigationViewSettings.getHeaderView(0).findViewById(R.id.profile_name);
        tvEmail = topNavigationViewSettings.getHeaderView(0).findViewById(R.id.profile_email);

    }

    //get user in4
    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }
        edtName.setText(user.getDisplayName());
        edtEmail.setText(user.getEmail());
    }

    //bắt sự kiện listener
    private void settingsProfile() {

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });

        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateEmail();
            }
        });
    }

    private void onClickUpdateProfile() { //ấn để update
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        String strName = edtName.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "Update profile successful", Toast.LENGTH_SHORT).show();
                            showUserInformation();
                        } else {
                            Toast.makeText(SettingsActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //get profile users
    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrI = user.getPhotoUrl();

        // Check xem có tên chưa
        if(name == null) {
            tvName.setVisibility(View.GONE);
        } else {
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(name);
        }
        tvEmail.setText(email);
    }

    //Bắt sự kiện change email
    private void onClickUpdateEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        String strNewEmail = edtEmail.getText().toString().trim();
        user.updateEmail(strNewEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "User email Address updated", Toast.LENGTH_SHORT).show();
                            showUserInformation();
                        } else {
                            Toast.makeText(SettingsActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override // hiển thị menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.main) {
            Intent intentProfile = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intentProfile);
        } else if(id == R.id.settings) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }  else if(id == R.id.help_feedback) {
            Intent intentProfile = new Intent(SettingsActivity.this, HelpFeedActivity.class);
            startActivity(intentProfile);
        }  else if(id == R.id.terms) {
            Intent intentProfile = new Intent(SettingsActivity.this, TermsActivity.class);
            startActivity(intentProfile);
        }  else if(id == R.id.contact) {
            Intent intentProfile = new Intent(SettingsActivity.this, ContactActivity.class);
            startActivity(intentProfile);
        } else if(id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        return true;
    }

    @Override // Xử lý khi ấn back drawer
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}