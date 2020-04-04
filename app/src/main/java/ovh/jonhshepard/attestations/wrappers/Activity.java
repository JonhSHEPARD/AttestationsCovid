package ovh.jonhshepard.attestations.wrappers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ovh.jonhshepard.attestations.storage.SQLiteDatabaseHandler;

/**
 * Default Activity Wrapper for this project
 * Allowing to have in each activity an object to handle Database
 */
public abstract class Activity extends AppCompatActivity {

    /**
     * Database object
     */
    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.db = new SQLiteDatabaseHandler(this);
    }

    /**
     * Get the Database handler for this activity
     *
     * @return {@link SQLiteDatabaseHandler} object
     */
    public SQLiteDatabaseHandler getDB() {
        return db;
    }
}