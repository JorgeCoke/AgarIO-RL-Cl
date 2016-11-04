package com.cokejorge.es.imaginechallenge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    CameraPosition cameraPosition;
    Circle circle = null;
    Localizable u;
    Localizable[] listaLocalizables;
    Localizable[] listaColisiones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_INDEFINITE)
        //                .setAction("Action", new View.OnClickListener() {
        //                    @Override
        //                    public void onClick(View view) {
        //                        Toast.makeText(MainActivity.this, "Has pulsado el Snackbar! :D", Toast.LENGTH_SHORT).show();
        //                    }
        //                }).show();
        //    }
        //});

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        //toggle.syncState();
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        // Obtenemos el fragment del mapa y nos notifica cuando el mapa esté disponible
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);      // Solo orientacion vertical
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // Mantener pantalla encendida
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //Iniciamos locationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Si no tenemos permisos, terminamos la app
            //  Consider calling ActivityCompat#requestPermissions here to request the missing permissions, and then overriding public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
            //  to handle the case where the user grants the permission. See the documentation for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);   //Iniciamos locationManager para recibir actualizaciones de ubicacion constantes
        //Obtenemos el nick y lo mostramos
        Bundle bundle = getIntent().getExtras();
        String nick = bundle.getString("nick");
        TextView textViewNick = (TextView) findViewById(R.id.textView3);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        textViewNick.setTypeface(face);
        textViewNick.setText(nick);

    }

    @Override
    protected void onResume() {
        Connection.init(null);
        u = new Localizable(10,new double[]{41.417327, 2.207267},"user");
        Connection.postUser(u);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Connection.deleteUser();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Iniciamos intent
        Intent intent = null;

        if (id == R.id.seleccionable1) {
            // Handle the camera action
        } else if (id == R.id.menu1) {
            intent = new Intent(this, Menu1.class);
        } else if (id == R.id.menu2) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (intent != null) {
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Modificamos botones de la interfaz
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        //mMap.setMyLocationEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        mMap.getUiSettings().setAllGesturesEnabled(false);

        // Movemos camara a Barcelona
        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(41.417327, 2.207267))
                .zoom(17)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onLocationChanged(Location location) {

        //Limpiamos mapa
        mMap.clear();

        //Actualizamos user
        u.setLocation(new double[]{location.getLatitude(), location.getLongitude()});
        Connection.updateUser(u);

        //Pintamos localizables
        listaLocalizables = Connection.getUsers(location.getLatitude(),location.getLongitude(),200);
        for (int i = 0; i< listaLocalizables.length; i++){
            Log.e("TAG",listaLocalizables[i].toString());
            if (listaLocalizables[i].getLocation()[0] != location.getLatitude()){
                if(listaLocalizables[i].getType().equals("user")){
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(listaLocalizables[i].getLocation()[0], listaLocalizables[i].getLocation()[1]))
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.RED)
                            .radius(listaLocalizables[i].getMass()));
                }
                else if (listaLocalizables[i].getType().equals("ball")){
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(listaLocalizables[i].getLocation()[0], listaLocalizables[i].getLocation()[1]))
                            .fillColor(Color.GREEN)
                            .radius(5));
                }
                else if (listaLocalizables[i].getType().equals("bank")){
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(listaLocalizables[i].getLocation()[0], listaLocalizables[i].getLocation()[1]))
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.CYAN)
                            .radius(20));
                }

            }
        }

        //Pintamos user
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .strokeWidth(2)
                .strokeColor(Color.BLACK)
                .fillColor(Color.YELLOW)
                .radius(u.getMass()));               //radio en metros

        //Movemos camara
        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(17)
                .bearing(location.getBearing())
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



        //Miramos colisiones
        listaColisiones = Connection.getUsers(location.getLatitude(),location.getLongitude(),u.getMass());
        if (listaColisiones != null & listaColisiones.length != 0){
            for (int i = 0; i< listaColisiones.length; i++){
                if (listaColisiones[i].getType().equals("user")){
                    //Toast.makeText(this, "colision con user", Toast.LENGTH_SHORT).show();
                }
                else if (listaColisiones[i].getType().equals("ball")){
                    Connection.deleteBall(listaColisiones[i].getLocation()[0],listaColisiones[i].getLocation()[1]);
                    u.setMass(u.getMass()+5);
                }
                else if (listaColisiones[i].getType().equals("bank")){
                    //Toast.makeText(this, "colision con bank", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Estado alterado: " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Proveedor Activo: " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Proveedor Desactivado: " + provider, Toast.LENGTH_LONG).show();
        //Terminamos app
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No tengo permisos", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
