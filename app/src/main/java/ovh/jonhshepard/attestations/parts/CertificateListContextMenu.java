package ovh.jonhshepard.attestations.parts;

import android.app.AlertDialog;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ovh.jonhshepard.attestations.R;
import ovh.jonhshepard.attestations.Util;
import ovh.jonhshepard.attestations.storage.Certificate;
import ovh.jonhshepard.attestations.storage.SQLiteDatabaseHandler;
import ovh.jonhshepard.attestations.wrappers.ActivityWithCertificateList;

/**
 * Context menu for acting on tasks in ListView
 */
public class CertificateListContextMenu implements ActionMode.Callback {

    /**
     * Activity containing ListView
     */
    private final ActivityWithCertificateList list;
    /**
     * Database object
     */
    private final SQLiteDatabaseHandler db;
    /**
     * Certificate concerned by the context menu
     */
    private final Certificate certificate;
    /**
     * View of the selected certificate
     */
    private final View selection;

    /**
     * Creating new context menu
     *
     * @param list        Activity containing ListView
     * @param db          Database handler
     * @param certificate Certificate concerned
     * @param selection   View of Certificate
     */
    public CertificateListContextMenu(ActivityWithCertificateList list, SQLiteDatabaseHandler db, Certificate certificate, View selection) {
        this.list = list;
        this.db = db;
        this.certificate = certificate;
        this.selection = selection;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Setting title of the context menu
        mode.setTitle(R.string.actions);
        mode.getMenuInflater().inflate(R.menu.listitemcertif_menu, menu);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // Deselecting identity object
        selection.setSelected(false);
        list.unselectedItem();
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuDelete) {// Confirmation for deletion
            String s = list.getString(R.string.updating_query_certficate)
                    + " " + certificate.getReasons().size() + " motif" + (certificate.getReasons().size() > 1 ? "s " : " ")
                    + Util.formatDateHour(certificate.getDate())
                    + " (" + certificate.getIdentity().getLastName() + " " + certificate.getIdentity().getFirstName() + ")";
            new AlertDialog.Builder(list)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.updating_title)
                    .setMessage(s)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        // If confirmed, delete in Database and update list
                        db.deleteCertificate(certificate);
                        list.updateList();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            mode.finish();
        } else {
            return false;
        }
        return false;
    }

}