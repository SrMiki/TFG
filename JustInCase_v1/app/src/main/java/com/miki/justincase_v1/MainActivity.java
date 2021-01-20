package com.miki.justincase_v1;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.miki.justincase_v1.bindings.Binding_Baggage_focusBaggage;
import com.miki.justincase_v1.bindings.Binding_Category_focusCategory;
import com.miki.justincase_v1.bindings.Binding_Item_focusItem;
import com.miki.justincase_v1.bindings.Binding_Suitcase_focusSuitcase;
import com.miki.justincase_v1.bindings.Binding_Trip_focusTrip;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Category;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.db.entity.Trip;
import com.miki.justincase_v1.fragments.Fragment_FocusBaggage;
import com.miki.justincase_v1.fragments.Fragment_FocusCategory;
import com.miki.justincase_v1.fragments.Fragment_FocusItem;
import com.miki.justincase_v1.fragments.Fragment_FocusSuitcase;
import com.miki.justincase_v1.fragments.Fragment_FocusTrip;
import com.miki.justincase_v1.fragments.Fragment_ItemManager;
import com.miki.justincase_v1.fragments.Fragment_ShowSuitcases;
import com.miki.justincase_v1.fragments.Fragment_ShowTrips;
import com.miki.justincase_v1.fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Binding_Trip_focusTrip, Binding_Item_focusItem, Binding_Suitcase_focusSuitcase, Binding_Baggage_focusBaggage, Binding_Category_focusCategory {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Fragment_FocusTrip fragmentFocusTrip;
    Fragment_FocusSuitcase fragmentFocusSuitcase;
    Fragment_FocusItem fragmentFocusItem;
    Fragment_FocusBaggage fragmentFocusBaggage;
    Fragment_FocusCategory fragmentFocusCategory;


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

    @Override
    public void sendTrip(Trip trip) {
        //aqui va toda la logica para el paso de datos entre fragments
        fragmentFocusTrip = new Fragment_FocusTrip();
        //Bundle para transportar la informacion
        Bundle bundle = new Bundle();
        //enviamos el objeto (en serial)
        bundle.putSerializable("trip", trip);
        fragmentFocusTrip.setArguments(bundle);

        //abrimos el fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_layout, fragmentFocusTrip);
        fragmentTransaction.commit();
    }

    @Override
    public void sendItem(Item item) {
        //aqui va toda la logica para el paso de datos entre fragments
        fragmentFocusItem = new Fragment_FocusItem();
        //Bundle para transportar la informacion
        Bundle bundle = new Bundle();
        //enviamos el objeto (en serial)
        bundle.putSerializable("item", item);
        fragmentFocusItem.setArguments(bundle);

        //abrimos el fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_layout, fragmentFocusItem);
        fragmentTransaction.commit();

    }

    @Override
    public void sendSuitcase(Suitcase suitcase) {
        //aqui va toda la logica para el paso de datos entre fragments
        fragmentFocusSuitcase = new Fragment_FocusSuitcase();
        //Bundle para transportar la informacion
        Bundle bundle = new Bundle();
        //enviamos el objeto (en serial)
        bundle.putSerializable("suitcase", suitcase);
        fragmentFocusSuitcase.setArguments(bundle);

        //abrimos el fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_layout, fragmentFocusSuitcase);
        fragmentTransaction.commit();

    }

    @Override
    public void sendBaggage(Baggage baggage) {
        //aqui va toda la logica para el paso de datos entre fragments
        fragmentFocusBaggage = new Fragment_FocusBaggage();
        //Bundle para transportar la informacion
        Bundle bundle = new Bundle();
        //enviamos el objeto (en serial)
        bundle.putSerializable("baggage", baggage);
        fragmentFocusBaggage.setArguments(bundle);

        //abrimos el fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_layout, fragmentFocusBaggage);
        fragmentTransaction.commit();
    }

    @Override
    public void sendCategory(Category category) {
        //aqui va toda la logica para el paso de datos entre fragments
        fragmentFocusCategory = new Fragment_FocusCategory();
        //Bundle para transportar la informacion
        Bundle bundle = new Bundle();
        //enviamos el objeto (en serial)
        bundle.putSerializable("category", category);
        fragmentFocusCategory.setArguments(bundle);

        //abrimos el fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_layout, fragmentFocusCategory);
        fragmentTransaction.commit();
    }
}
