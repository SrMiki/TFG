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

import com.google.android.material.navigation.NavigationView;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Binding_Entity_focusEntity {

    ActionBarDrawerToggle actionBarDrawerToggle;

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);


        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return false;
//    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.ic_home:
                navController.navigate(R.id.mainFragment);
                break;
            case R.id.ic_Trips:
                navController.navigate(R.id.fragment_ShowTrips);
                break;
            case R.id.ic_suitcases:
                navController.navigate(R.id.fragment_ShowSuitcases);
                break;
            case R.id.ic_items:
                navController.navigate(R.id.fragment_ItemManager);
                break;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private NavController getNav() {
        return Navigation.findNavController(this, R.id.nav_host_fragment);
    }


    @Override
    public void sendTrip(Trip trip) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("trip", trip);
        getNav().navigate(R.id.fragment_FocusTrip2, bundle);
    }


    @Override
    public void sendSuitcase(Suitcase suitcase) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("suitcase", suitcase);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getNav().navigate(R.id.fragment_FocusSuitcase, bundle);
    }

    @Override
    public void sendBaggage(Baggage baggage) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("baggage", baggage);
        getNav().navigate(R.id.fragment_FocusBaggage, bundle);
    }

    @Override
    public void sendItem(Item item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        getNav().navigate(R.id.fragment_FocusItem, bundle);
    }

    @Override
    public void sendCategory(Category category) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);

        getNav().navigate(R.id.fragment_FocusCategory, bundle);
    }

}
