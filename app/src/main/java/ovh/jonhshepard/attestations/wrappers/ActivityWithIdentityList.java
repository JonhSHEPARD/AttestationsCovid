package ovh.jonhshepard.attestations.wrappers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ovh.jonhshepard.attestations.EditAddIdentityActivity;
import ovh.jonhshepard.attestations.parts.IdentityAdapter;
import ovh.jonhshepard.attestations.storage.Identity;

/**
 * Intermediate layer for Activities that contains list views of identities
 */
public abstract class ActivityWithIdentityList extends Activity {

    /**
     * Adapter for ListView
     */
    private IdentityAdapter identityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Identity> identityList = new ArrayList<>();
        identityAdapter = new IdentityAdapter(this, identityList);
    }

    /**
     * Function to updateList (calling children class)
     */
    public abstract void updateList();

    /**
     * Updating background color for selected item
     */
    public void unselectedItem() {
        identityAdapter.notifyDataSetChanged();
    }

    /**
     * Getting the Adapter for the ListView
     * @return {@link IdentityAdapter} object
     */
    protected IdentityAdapter getIdentityListAdapter() {
        return this.identityAdapter;
    }

    /**
     * Launches {@link EditAddIdentityActivity} for the concerned Identity
     *
     * @param identity Identity to edit
     */
    public void editIdentity(Identity identity) {
        Intent nextScreen = new Intent(getApplicationContext(), EditAddIdentityActivity.class);
        nextScreen.putExtra("edit", identity.getId());
        startActivityForResult(nextScreen, 41);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Updating list after edition exited
        if (requestCode == 41)
            updateList();
    }

}