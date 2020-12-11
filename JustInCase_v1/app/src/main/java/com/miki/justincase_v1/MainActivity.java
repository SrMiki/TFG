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
import com.miki.justincase_v1.db.entity.Viaje;
import com.miki.justincase_v1.fragments.FragmentDetallesViajes;
import com.miki.justincase_v1.fragments.FragmentViajes;
import com.miki.justincase_v1.fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DetallesViajeBinding {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FragmentDetallesViajes fragmentDetallesViajes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        fragmentTransaction.add(R.id.main_container, new MainFragment());
        fragmentTransaction.commit();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.ic_home:
                fragmentTransaction.replace(R.id.main_container, new MainFragment());
                break;
            case R.id.ic_cardTravel:
                fragmentTransaction.replace(R.id.main_container, new FragmentViajes());
                break;
        }
        fragmentTransaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void sendViaje(Viaje viaje) {
        //aqui va toda la logica para el paso de datos entre fragments
        fragmentDetallesViajes = new FragmentDetallesViajes();
        //Bundle para transportar la informacion
        Bundle bundle = new Bundle();
        //enviamos el objeto (en serial)
        bundle.putSerializable("objeto", viaje);
        fragmentDetallesViajes.setArguments(bundle);

        //abrimos el fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragmentDetallesViajes);
        fragmentTransaction.commit();

    }
}
