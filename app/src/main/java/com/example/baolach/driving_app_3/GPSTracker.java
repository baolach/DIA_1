package com.example.baolach.driving_app_3;

/**
 * Created by Baolach on 21/02/2017.
 */

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location mLocation; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    // Declaring a Location Manager
    protected LocationManager mLocationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                this.canGetLocation = false;

            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        try {
                            mLocationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                    this);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }

                   // }

                    if (mLocationManager != null) {
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (mLocation != null) {
                            latitude = mLocation.getLatitude();
                            longitude = mLocation.getLongitude();
                        }

                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (mLocation == null) {
                        try {
                            mLocationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                    this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (mLocation != null) {
                            latitude = mLocation.getLatitude();
                            longitude = mLocation.getLongitude();
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return mLocation;

    } // end getLocation class

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                try {
                    mLocationManager.removeUpdates(GPSTracker.this);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
            mLocationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (mLocation != null) {
            latitude = mLocation.getLatitude();
        }

        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (mLocation != null) {
            longitude = mLocation.getLongitude();
        }

        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /////////////////



//    }public void tryGetLocation() {
//
//        GPSTracker MyCurrentLocation;
//        MyCurrentLocation = new MyCurrentLocation(this);
//
//        if (MyCurrentLocation.canGetLocation()) {
//
//            try {
//
//                double longitude = MyCurrentLocation.getLongitude();
//                double latitude = MyCurrentLocation.getLatitude();
//
////                Log.w("Long", "" + longitude);
////                Log.w("Lat", "" + latitude);
//
//                if (longitude != 0.0 && latitude != 0.0) {
//
//
//                    // 54.636725, -8.444537 Donegal
//
//                    //54.251501, -8.474764 Sligo
//                    // longitude = -8.474764;
//                    // latitude = 54.251501;
//
//                            /*
//                            * Country
//                            * SubAdmin - County - Local if SubAdmin has null value
//                            * Address - Town - area
//                            * */
//
//
//                    Geocoder geocoder;
//                    List<Address> addresses;
//                    geocoder = new Geocoder(this, Locale.getDefault());
//
//                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//
//                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineInde
//                    String city = addresses.get(0).getLocality();
//                    String state = addresses.get(0).getAdminArea();
//                    String country = addresses.get(0).getCountryName();
//                    String postalCode = addresses.get(0).getPostalCode();
//                    String knownName = addresses.get(0).getFeatureName();
//                    String locality = addresses.get(0).getLocality();
//                    String subAdminArea = addresses.get(0).getSubAdminArea();
//                    String value3 = addresses.get(0).getSubLocality();
//                    String value4 = addresses.get(0).getPremises();
//
//                          /*  Toast.makeText(this, "Country: " + country +
//                                            "\nbla" + state+
//                                            "\naddress: " + address +
//                                            "\ncity: " + city +
//                                            "\nknownName: " + knownName +
//                                            "\nPostcode: " + postalCode +
//                                            "\nLocal: " + locality +
//                                            "\nSubAdmin: " +subAdminArea +
//                                            "\nSubLoc: " + value3 +
//                                            "\nPremises: " + value4 +
//                                            "\nLongitude: " + longitude +
//                                            "\nLatitude: " + latitude,
//                                Toast.LENGTH_SHORT).show();
//*/
//
//                    countryTextView.setText(country);
//                    countryTextView.setTextColor(Color.RED);
//
//                    try {
//
//                        Log.e("LOC", addresses.toString());
//
//                        if(state != null) {
//                            countyTextView.setText(state);
//                        } else if (subAdminArea != null) {
//                            countyTextView.setText(subAdminArea);
//                        } else if (locality != null) {
//                            countyTextView.setText(locality);
//                        }
//                        countyTextView.setTextColor(Color.RED);
//
//                        if (city.equals(locality) | city.equals(subAdminArea)) {
//                            townText.setText(address);
//                        } else {
//                            townText.setText(city);
//                        }
//                        townText.setTextColor(Color.RED);
//
//                        Toast.makeText(this, "Current location and date set", Toast.LENGTH_SHORT).show();
//
//                    } catch (NullPointerException e) {
//                        // Toast.makeText(this, "Location and date not set", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            mMyCurrentLocation.showSettingsDialog();
//        }

}

