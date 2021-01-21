package com.miki.justincase_v1;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.Focus.Fragment_FocusBaggage;
import com.miki.justincase_v1.fragments.Focus.Fragment_FocusCategory;
import com.miki.justincase_v1.fragments.Focus.Fragment_FocusItem;
import com.miki.justincase_v1.fragments.Focus.Fragment_FocusSuitcase;
import com.miki.justincase_v1.fragments.Focus.Fragment_FocusTrip;
import com.miki.justincase_v1.fragments.Fragment_ItemManager;
import com.miki.justincase_v1.fragments.Show.Fragment_ShowSuitcases;
import com.miki.justincase_v1.fragments.Show.Fragment_ShowTrips;
import com.miki.justincase_v1.fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Binding_Entity_focusEntity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);


        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //cargamos el fragmento principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_main_layout, new MainFragment());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.ic_home:
                fragmentTransaction.replace(R.id.content_main_layout, new MainFragment());
                break;
            case R.id.ic_Trips:
                fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ShowTrips());
                break;
            case R.id.ic_suitcases:
                fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ShowSuitcases());
                break;
            case R.id.ic_items:
                fragmentTransaction.replace(R.id.content_main_layout, new Fragment_ItemManager());
                break;


        }
        fragmentTransaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void doFragmentTransaction(Fragment fragment, Bundle bundle){
        fragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void sendTrip(Trip trip) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("trip", trip);
       doFragmentTransaction(new Fragment_FocusTrip(), bundle);
    }

    @Override
    public void sendItem(Item item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        doFragmentTransaction(new Fragment_FocusItem(), bundle);
    }

    @Override
    public void sendSuitcase(Suitcase suitcase) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("suitcase", suitcase);
        doFragmentTransaction(new Fragment_FocusSuitcase(), bundle);
    }

    @Override
    public void sendBaggage(Baggage baggage) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("baggage", baggage);
        doFragmentTransaction( new Fragment_FocusBaggage(), bundle);
    }

    @Override
    public void sendCategory(Category category) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        doFragmentTransaction(new Fragment_FocusCategory(), bundle);
    }
}
