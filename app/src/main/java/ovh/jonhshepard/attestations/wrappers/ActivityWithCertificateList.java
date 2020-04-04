package ovh.jonhshepard.attestations.wrappers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ovh.jonhshepard.attestations.parts.CertificateAdapter;
import ovh.jonhshepard.attestations.storage.Certificate;

/**
 * Intermediate layer for Activities that contains list views of certificates
 */
public abstract class ActivityWithCertificateList extends Activity {

    /**
     * Adapter for ListView
     */
    private CertificateAdapter certificateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Certificate> certificateList = new ArrayList<>();
        certificateAdapter = new CertificateAdapter(this, certificateList);
    }

    /**
     * Function to updateList (calling children class)
     */
    public abstract void updateList();

    /**
     * Updating background color for selected item
     */
    public void unselectedItem() {
        certificateAdapter.notifyDataSetChanged();
    }

    /**
     * Getting the Adapter for the ListView
     * @return {@link CertificateAdapter} object
     */
    protected CertificateAdapter getCertificateAdapter() {
        return this.certificateAdapter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Updating list after edition exited
        if (requestCode == 41)
            updateList();
    }

}