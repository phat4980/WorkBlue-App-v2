package workblue.todo.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private NavigationView topNavigationView;
    private ViewPager2 viewPagerContent;
    private TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);



        initUi();
        setSupportActionBar(toolbar); //kích hoạt drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        bottomNavigationView.getMenu().getItem(1).setEnabled(false); //xóa placeholder trong menu bottom



        bottomNavigationChange();
        showUserInformation();
    }

    private void initUi() // ánh xạ
    {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        topNavigationView = (NavigationView) findViewById(R.id.top_navigation);
        topNavigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        tvName = topNavigationView.getHeaderView(0).findViewById(R.id.profile_name);
        tvEmail = topNavigationView.getHeaderView(0).findViewById(R.id.profile_email);
    }

    @Override // hiển thị menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void bottomNavigationChange() //Navigate bằng bottom
    {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.tasks) {
                    Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                } else if(id == R.id.calendar) {
                }
                return true;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { // điều hướng drawer
        int id = item.getItemId();
        if(id == R.id.main) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(id == R.id.settings) {
            Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
            startActivity(intent);
        }  else if(id == R.id.help_feedback) {
            Intent intent = new Intent(CalendarActivity.this, HelpFeedActivity.class);
            startActivity(intent);
        }  else if(id == R.id.terms) {
            Intent intent = new Intent(CalendarActivity.this, TermsActivity.class);
            startActivity(intent);
        }  else if(id == R.id.contact) {
            Intent intent = new Intent(CalendarActivity.this, ContactActivity.class);
            startActivity(intent);
        } else if(id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        return true;
    }

    //get user profile
    private void showUserInformation()
    {
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