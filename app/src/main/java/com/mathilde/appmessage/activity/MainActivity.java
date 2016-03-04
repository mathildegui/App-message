package com.mathilde.appmessage.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mathilde.appmessage.R;
import com.mathilde.appmessage.bean.User;
import com.mathilde.appmessage.fragment.ContactListFragment;
import com.mathilde.appmessage.fragment.MainFragment;
import com.mathilde.appmessage.fragment.MessageFragment;


public class MainActivity extends AppCompatActivity implements
        ContactListFragment.OnListFragmentInteractionListener,
        MainFragment.OnListFragmentInteractionListener {

    private static final int MY_PERMISSIONS = 10;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private void checkPermission() {
        String SEND_SMS        = Manifest.permission.SEND_SMS;
        String READ_CONTACTS   = Manifest.permission.READ_CONTACTS;

        String[] sArrayAll     = new String[]{READ_CONTACTS, SEND_SMS};
        String[] sArraySms     = new String[]{SEND_SMS};
        String[] sArrayContact = new String[]{READ_CONTACTS};

        int isGranted   = PackageManager.PERMISSION_GRANTED;

        int permSms     = ContextCompat.checkSelfPermission(MainActivity.this, SEND_SMS);
        int permContact = ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS);

        if (permContact != isGranted && permSms != isGranted) {
            ActivityCompat.requestPermissions(this, sArrayAll, MY_PERMISSIONS);
        } else if (permContact != isGranted) {
            ActivityCompat.requestPermissions(this, sArrayContact, MY_PERMISSIONS);
        } else if (permSms != isGranted){
            ActivityCompat.requestPermissions(this, sArraySms, MY_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab             = getSupportActionBar();
        Toolbar toolbar          = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);

        checkPermission();
        checkPlayServices();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, MainFragment.newInstance()).commit();
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.fragment_container, ContactListFragment.newInstance());
                    transaction.addToBackStack(null);

                    transaction.commit();
                } else {
                    Snackbar.make(view, "This app can't access contacts. Please check permissions", Snackbar.LENGTH_LONG)
                            .setAction("SETTINGS", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Intent i = new Intent();
                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    i.addCategory(Intent.CATEGORY_DEFAULT);
                                    i.setData(Uri.parse("package:" + getPackageName()));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(i);
                                }
                            }).show();
                }
            }
        });
    }

    @Override
    public void onListFragmentInteraction(User user) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, MessageFragment.newInstance(user));
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        } 
        return true;
    }
}
