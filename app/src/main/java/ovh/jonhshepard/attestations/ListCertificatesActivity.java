package ovh.jonhshepard.attestations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import ovh.jonhshepard.attestations.parts.CertificateListContextMenu;
import ovh.jonhshepard.attestations.storage.Certificate;
import ovh.jonhshepard.attestations.wrappers.ActivityWithCertificateList;

public class ListCertificatesActivity extends ActivityWithCertificateList {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_certificates);

        ListView listView = findViewById(R.id.listCertificates);
        listView.setAdapter(getCertificateAdapter());
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Certificate certificate = (Certificate) listView.getAdapter().getItem(position);
            Util.openPdfFile(this, getDB(), certificate);
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Certificate task = getDB().getCertificateFromId((int) view.getTag());
            startActionMode(new CertificateListContextMenu(ListCertificatesActivity.this, getDB(), task, view));
            view.setSelected(true);
            getCertificateAdapter().notifyDataSetChanged();
            return true;
        });

        Button addbtn = findViewById(R.id.buttonAddCertif);
        addbtn.setOnClickListener(v -> {
            Intent nextScreen = new Intent(getApplicationContext(), AddCertificateActivity.class);
            startActivityForResult(nextScreen, 42);
        });

        updateList();
    }

    @Override
    public void updateList() {
        List<Certificate> tasks = getDB().getCertificates();
        Collections.sort(tasks);

        // Updating list
        getCertificateAdapter().clear();
        getCertificateAdapter().addAll(tasks);
        getCertificateAdapter().notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Updating calendar after showing activities
        if (requestCode == 42)
            updateList();
    }
}
