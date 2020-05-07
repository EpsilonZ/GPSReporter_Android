package com.example.reportlocationdata;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends Activity implements LocationListener {
    private TextView latituteField;
    private TextView longitudeField;
    private TextView speedField;
    private EditText serverIP;
    private LocationManager locationManager;
    private String provider;
    private Socket socket;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);
        speedField = (TextView) findViewById(R.id.TextView06);
        serverIP = (EditText) findViewById(R.id.editText);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            Log.i("PERMISSIONS", "PERMISSIONSERROR");
            requestPermissionsForUser();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
            speedField.setText("Speed not available");
        }
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            requestPermissionsForUser();
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    private void requestPermissionsForUser() {
        Context mContext = MainActivity.this;

        final int REQUEST = 112;

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
            ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST);
        } else {
            //call get location here
        }
    }


    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        final double lat = (location.getLatitude());
        final double lng = (location.getLongitude());
        //also accuracymeter/s
        final double speed = (location.getSpeedAccuracyMetersPerSecond());
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
        speedField.setText(String.valueOf(speed));

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    // Your implementation
                    try {
                        Log.i("IP location changed", serverIP.getText().toString());
                        socket = new Socket(serverIP.getText().toString(), 15000);
                        String datagramData = String.valueOf(lat) + "," + String.valueOf(lng) + "," + String.valueOf(speed) + "\n";
                        Log.i("datagramdata", datagramData);
                        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                        outToServer.writeBytes(datagramData);
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}