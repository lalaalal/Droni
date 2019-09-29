package com.lalaalal.droni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.lalaalal.droni.ui.LoginDialog;
import com.lalaalal.droni.ui.user.UserPageActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        initNavigation();
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        final SharedPreferencesHandler loginSession = SharedPreferencesHandler.getSharedPreferences(this);
        setSupportActionBar(toolbar);

        ImageButton expandMenuBtn = findViewById(R.id.expand_menu_btn);
        expandMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View navView = findViewById(R.id.nav_view);
                drawer.openDrawer(navView);
            }
        });

        ImageButton userBtn = findViewById(R.id.user_btn);
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!loginSession.isLoggedIn()) {
                    LoginDialog loginDialog = new LoginDialog();
                    loginDialog.show(getSupportFragmentManager(), "Login Dialog");
                } else {
                    Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initNavigation() {
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_intro, R.id.nav_rule, R.id.nav_kp_alarm)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
