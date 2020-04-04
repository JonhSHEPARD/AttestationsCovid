package ovh.jonhshepard.attestations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.List;

import ovh.jonhshepard.attestations.parts.IdentityListContextMenu;
import ovh.jonhshepard.attestations.storage.Identity;
import ovh.jonhshepard.attestations.wrappers.ActivityWithIdentityList;

public class ListIdentitiesActivity extends ActivityWithIdentityList {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_identities);

        ListView listView = findViewById(R.id.listIdentities);
        listView.setAdapter(getIdentityListAdapter());
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Identity task = getDB().getIdentityFromId((int) view.getTag());
            startActionMode(new IdentityListContextMenu(ListIdentitiesActivity.this, getDB(), task, view));
            view.setSelected(true);
            getIdentityListAdapter().notifyDataSetChanged();
            return true;
        });

        Button addbtn = findViewById(R.id.buttonAdd);
        addbtn.setOnClickListener(v -> {
            Intent nextScreen = new Intent(getApplicationContext(), EditAddIdentityActivity.class);
            startActivityForResult(nextScreen, 42);
        });

        updateList();
    }

    @Override
    public void updateList() {
        List<Identity> tasks = getDB().getIdentities();

        // Updating list
        getIdentityListAdapter().clear();
        getIdentityListAdapter().addAll(tasks);
        getIdentityListAdapter().notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Updating calendar after showing activities
        if (requestCode == 42)
            updateList();
    }
}
