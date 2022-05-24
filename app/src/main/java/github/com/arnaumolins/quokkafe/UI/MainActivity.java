package github.com.arnaumolins.quokkafe.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import github.com.arnaumolins.quokkafe.R;

public class MainActivity extends AppCompatActivity implements DrawerController{

    BottomNavigationView bottomNav;
    NavController navController;
    NavHostFragment navHostFragment;
    Toolbar mToolbar;
    AppBarConfiguration appBarConfiguration;
    DrawerLayout drawerLayout;
    event_interface_fragment eventFragment = new event_interface_fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // top bar
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        //nav controller
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        navController = navHostFragment.getNavController();

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.nav_drawe_open, R.string.nav_drawe_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationUI.setupWithNavController((NavigationView)findViewById(R.id.navigation_view), navController);

        //bottom bar
        bottomNav = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment,eventFragment).commit();
        bottomNav.setOnItemSelectedListener(navListener);
        //NavigationUI.setupWithNavController(bottomNav, navController);

    }

    private NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.event_interface_bottom:
                    selectedFragment = new event_interface_fragment();
                    break;
                case R.id.table_booking_bottom:
                    selectedFragment = new table_booking_fragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_navigation_menu, menu);
        return true;
    }

    @Override
    public void lockDrawerMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToolbar.setNavigationIcon(null);
    }

    @Override
    public void unlockDrawerMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mToolbar.setNavigationIcon(R.drawable.events_icon);
    }
}