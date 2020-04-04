package ovh.jonhshepard.attestations.parts;

import android.app.AlertDialog;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ovh.jonhshepard.attestations.R;
import ovh.jonhshepard.attestations.Util;
import ovh.jonhshepard.attestations.storage.Identity;
import ovh.jonhshepard.attestations.storage.SQLiteDatabaseHandler;
import ovh.jonhshepard.attestations.wrappers.ActivityWithIdentityList;

/**
 * Context menu for acting on tasks in ListView
 */
public class IdentityListContextMenu implements ActionMode.Callback {

    /**
     * Activity containing ListView
     */
    private final ActivityWithIdentityList list;
    /**
     * Database object
     */
    private final SQLiteDatabaseHandler db;
    /**
     * Identity concerned by the context menu
     */
    private final Identity identity;
    /**
     * View of the selected identity
     */
    private final View selection;

    /**
     * Creating new context menu
     *
     * @param list Activity containing ListView
     * @param db Database handler
     * @param task Task concerned
     * @param selection View of Task
     */
    public IdentityListContextMenu(ActivityWithIdentityList list, SQLiteDatabaseHandler db, Identity task, View selection) {
        this.list = list;
        this.db = db;
        this.identity = task;
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
        mode.getMenuInflater().inflate(R.menu.listitemident_menu, menu);
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
        switch (id) {
            case R.id.menuDelete:
                // Confirmation for deletion
                String s = list.getString(R.string.updating_query)
                        + " " + identity.getLastName() + " " + identity.getFirstName()
                        + " (" + Util.formatDate(identity.getBirthday()) + ")";
                new AlertDialog.Builder(list)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.updating_title)
                        .setMessage(s)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            // If confirmed, delete in Database and update list
                            db.deleteIdentity(identity);
                            list.updateList();
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                mode.finish();
                break;
            case R.id.menuEdit:
                mode.finish();
                // Launching EditActivity
                list.editIdentity(identity);
                selection.setSelected(false);
                break;
            default:
                return false;
        }
        return false;
    }

}