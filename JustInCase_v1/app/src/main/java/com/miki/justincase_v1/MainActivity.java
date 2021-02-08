package com.miki.justincase_v1;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.HandLuggage;
import com.miki.justincase_v1.db.entity.Trip;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Binding_Entity_focusEntity {
    NavController navController;
    DrawerLayout drawerLayout;
    AppBarConfiguration appBarConfiguration;
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   

        appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.mainFragment, R.id.fragment_ShowTrips,
                        R.id.fragment_ShowSuitcases, R.id.fragment_ShowItems, R.id.fragment_ShowCategories)
                        .setOpenableLayout(drawerLayout)
                        .build();

        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        //navigate to others parts.
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.ic_COVID) {
            Presented.initDB(getApplicationContext());
        }
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        onOptionsItemSelected(item);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private NavController getNav() {
        return Navigation.findNavController(this, R.id.fragment);
    }

    @Override
    public void sendTrip(Trip trip) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("trip", trip);
        getNav().navigate(R.id.fragment_showHandLaggage, bundle);
    }

    @Override
    public void sendHandLuggage(HandLuggage handLuggage) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("baggage", handLuggage);
        getNav().navigate(R.id.fragment_ShowBaggage, bundle);
    }
}