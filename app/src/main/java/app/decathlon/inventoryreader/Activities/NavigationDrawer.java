package app.decathlon.inventoryreader.Activities;

/**
 * Created by LINTZZZ on 23/10/2017.
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import app.decathlon.inventoryreader.Fragments.AllDispositivesFragment;
import app.decathlon.inventoryreader.Fragments.PrincipalFragment;
import app.decathlon.inventoryreader.Fragments.ReadQRFragment;
import app.decathlon.inventoryreader.MainActivity;
import app.decathlon.inventoryreader.R;

public class NavigationDrawer extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentManager fm;
    private android.support.v4.app.FragmentManager fam;

    protected OnBackPressedListener onBackPressedListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        //Metodo para invocar nuestra custom toolbar
        setToolbar();

        //PARA HACER EL OnBackPressed()
        fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getFragmentManager().getBackStackEntryCount() == 0) finish();
            }
        });
        //--------------------------------

        //COLOCAR UN FRAGMENT DE INICIO:
        Fragment fr = new PrincipalFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fr)
                .commit();
        //------------------------------

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navView);

        //Método para acceder al fragment seleccionado en NAVVIEW
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                boolean fragmentTransaction = false;
                Fragment fragment = null;
                Activity act = null;

                //Según que selección...
                switch (item.getItemId()){

                    //Fragment mail
                    case R.id.menu_readQR:
                        fragment = new ReadQRFragment();
                        fragmentTransaction = true;
                        break;


                        //SALIMOS DEL NAVIGATIONVIEW
                    case R.id.menu_all:
                        fragment = new AllDispositivesFragment();
                        fragmentTransaction = true;
                        /*Intent intent = new Intent(NavigationDrawer.this, MainActivity.class);
                        startActivity(intent);*/
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

                return true;
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


    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }
}