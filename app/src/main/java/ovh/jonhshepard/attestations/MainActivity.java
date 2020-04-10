package ovh.jonhshepard.attestations;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ovh.jonhshepard.attestations.storage.Certificate;
import ovh.jonhshepard.attestations.wrappers.ActivityWithCertificateList;

public class MainActivity extends ActivityWithCertificateList {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }

        ListView listView = findViewById(R.id.listViewRecentCertif);
        listView.setAdapter(getCertificateAdapter());
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Certificate certificate = (Certificate) listView.getAdapter().getItem(position);
            Util.openPdfFile(this, getDB(), certificate);
        });

        Button listCertif = findViewById(R.id.buttonListAllCertificates);
        listCertif.setOnClickListener(v -> {
            Intent nextScreen = new Intent(getApplicationContext(), ListCertificatesActivity.class);
            startActivityForResult(nextScreen, 42);
        });

        Button listIdent = findViewById(R.id.buttonListIdentities);
        listIdent.setOnClickListener(v -> {
            Intent nextScreen = new Intent(getApplicationContext(), ListIdentitiesActivity.class);
            startActivityForResult(nextScreen, 42);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (getDB().getIdentities().size() == 0) {
                Snackbar.make(view, R.string.no_identity, Snackbar.LENGTH_LONG).show();
                return;
            }
            Intent nextScreen = new Intent(getApplicationContext(), AddCertificateActivity.class);
            startActivityForResult(nextScreen, 42);
        });

        updateList();

        if (getDB().getIdentities().size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.no_identity_create)
                    .setTitle(R.string.no_identity_title);

            builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                Intent nextScreen = new Intent(getApplicationContext(), EditAddIdentityActivity.class);
                startActivityForResult(nextScreen, 42);
            });
            builder.setNegativeButton(R.string.no, (dialog, id) -> {
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void updateList() {
        List<Certificate> certificates = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 30);
        Calendar maxD = Calendar.getInstance();
        maxD.add(Calendar.DAY_OF_YEAR, -2);
        for (Certificate certificate : getDB().getCertificates()) {
            if (certificate.getDate().before(now.getTime())
                    && certificate.getDate().after(maxD.getTime())) {
                certificates.add(certificate);
            }
        }
        Collections.sort(certificates);

        // Updating list
        getCertificateAdapter().clear();
        getCertificateAdapter().addAll(certificates);
        getCertificateAdapter().notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Updating calendar after showing activities
        if (requestCode == 42)
            updateList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.finish();
                }
                return;
            }
        }
    }
}
