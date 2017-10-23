package app.decathlon.inventoryreader.Activities;

/**
 * Created by LINTZZZ on 23/10/2017.
 */

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import app.decathlon.inventoryreader.Fragments.ReadQRFragment;
import app.decathlon.inventoryreader.R;

public class NavigationDrawer extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        //Metodo para invocar nuestra custom toolbar
        setToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navView);

        //Método para acceder al fragment seleccionado en NAVVIEW
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                boolean fragmentTransaction = false;
                Fragment fragment = null;

                //Según que selección...
                switch (item.getItemId()){

                    //Fragment mail
                    case R.id.menu_readQR:
                        fragment = new ReadQRFragment();
                        fragmentTransaction = true;
                        break;

                }

                //Empieza la transacción del fragment
                if (fragmentTransaction){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                    //Se verá clicada la opción en el navView
                    item.setChecked(true);
                    //Cogemos el titulo en la barra herramientas
                    getSupportActionBar().setTitle(item.getTitle());
                    //Cerramos el drawer
                    drawerLayout.closeDrawers();
                }

                return false;
            }
        });
    }

    private void setToolbar() {
        //BARRA HERRAMIENTAS ACTIN BAR
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //Imagen en la posición inicial de la barra de herramientas
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        //Mostramos esa imagen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                //Abrir el menú lateral desde el inicio del lado izquierdo(START)
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}