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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    CameraPosition cameraPosition;
    Circle circle = null;
    Localizable u;
    Localizable[] listaLocalizables;
    Localizable[] listaColisiones;
    TextView textViewNick;
    TextView textViewScore;


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
        textViewNick = (TextView) findViewById(R.id.textView3);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        textViewNick.setTypeface(face);
        textViewNick.setText(nick);

        textViewScore = (TextView) findViewById(R.id.textView2);

    }

    @Override
    protected void onDestroy() {
        Log.e("TAG", "EJECUTA ONDESTROY");
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        //Modificamos botones de la interfaz
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        //mMap.setMyLocationEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        mMap.getUiSettings().setAllGesturesEnabled(false);

        // Movemos camara a Barcelona
        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(41.417327, 2.207267))
                .zoom(17)
                .bearing(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Connection.init(null);
        u = new Localizable(10, new double[]{41.417327, 2.207267}, "user");
        Connection.postUser(u);
    }

    @Override
    public void onLocationChanged(Location location) {

        //Limpiamos mapa
        mMap.clear();

        //Actualizamos user
        u.setLocation(new double[]{location.getLatitude(), location.getLongitude()});
        try{
            Connection.updateUser(u);
        }
        catch (Exception e){
            Connection.deleteUser();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
            this.finish();
        }

        //Pintamos localizables
        listaLocalizables = Connection.getUsers(location.getLatitude(), location.getLongitude(), 250);
        for (int i = 0; i < listaLocalizables.length; i++) {
            if (listaLocalizables[i].getLocation()[0] != location.getLatitude()) {
                if (listaLocalizables[i].getType().equals("user")) {
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(listaLocalizables[i].getLocation()[0], listaLocalizables[i].getLocation()[1]))
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.rgb(244, 86, 66))
                            .radius(listaLocalizables[i].getMass()));
                } else if (listaLocalizables[i].getType().equals("ball")) {
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(listaLocalizables[i].getLocation()[0], listaLocalizables[i].getLocation()[1]))
                            .fillColor(Color.rgb(128, 244, 66))
                            .radius(5));
                } else if (listaLocalizables[i].getType().equals("bank")) {
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(listaLocalizables[i].getLocation()[0], listaLocalizables[i].getLocation()[1]))
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.argb(120,197, 66, 244))
                            .radius(30));
                }

            }
        }

        //Pintamos user
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .strokeWidth(2)
                .strokeColor(Color.BLACK)
                .fillColor(Color.rgb(244, 206, 66))
                .radius(u.getMass()));               //radio en metros

        //Movemos camara
        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(17)
                .bearing(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        //Miramos colisiones
        listaColisiones = Connection.getUsers(location.getLatitude(), location.getLongitude(), u.getMass());
        if (listaColisiones != null & listaColisiones.length != 0) {
            for (int i = 0; i < listaColisiones.length; i++) {
                if (listaColisiones[i].getType().equals("user")) {
                    if (listaColisiones[i].getMass() < u.getMass()) {
                        Connection.deleteBall(listaColisiones[i].getLocation()[0], listaColisiones[i].getLocation()[1]);
                        u.setMass(u.getMass() + listaColisiones[i].getMass());
                    } else if (listaColisiones[i].getMass() > u.getMass()) {
                        Toast.makeText(this, "Has sido eliminado. Puntuacion: " + u.getMass(), Toast.LENGTH_LONG).show();
                        Connection.deleteUser();
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        locationManager.removeUpdates(this);
                        this.finish();
                    }
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
            textViewScore.setText("Puntuacion: "+u.getMass());
        }

        listaColisiones = Connection.getUsers(location.getLatitude(), location.getLongitude(), u.getMass()*2);
        if (listaColisiones != null & listaColisiones.length != 0) {
            for (int i = 0; i < listaColisiones.length; i++) {
                if (listaColisiones[i].getType().equals("user")) {
                    if (listaColisiones[i].getMass() > u.getMass()*1.9) {
                        Toast.makeText(this, "Has sido eliminado. Puntuacion: " + u.getMass(), Toast.LENGTH_LONG).show();
                        Connection.deleteUser();
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        locationManager.removeUpdates(this);
                        this.finish();
                    }
                }
            }
        }

        //Pintamos linea a banco mas popular
        Localizable bank = Connection.getBank();
        PolylineOptions options = new PolylineOptions();
        options.color( Color.parseColor( "#CC0000FF" ) );
        options.width( 5 );
        options.visible( true );
        options.add(new LatLng(bank.getLocation()[0], bank.getLocation()[1]));
        options.add(new LatLng(u.getLocation()[0], u.getLocation()[1]));
        mMap.addPolyline(options);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Toast.makeText(this, "Estado alterado: " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        //Toast.makeText(this, "Proveedor Activo: " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(getApplicationContext(), "Proveedor Desactivado: " + provider, Toast.LENGTH_LONG).show();
        //Terminamos app
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //    Toast.makeText(this, "No tengo permisos", Toast.LENGTH_SHORT).show();
        //    return;
        //}
    }

}
