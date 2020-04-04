package ovh.jonhshepard.attestations.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Certificates";

    private static final String IDENT_TABLE_NAME = "identities";
    private static final String IDENT_KEY_ID = "id";
    private static final String IDENT_KEY_LNAME = "lastname";
    private static final String IDENT_KEY_FNAME = "firstname";
    private static final String IDENT_KEY_BIRTH_DAY = "birthday";
    private static final String IDENT_KEY_BIRTH_PLACE = "birthplace";
    private static final String IDENT_KEY_LIVING_ADDR = "living_address";
    private static final String IDENT_KEY_LIVING_CITY = "living_city";
    private static final String IDENT_KEY_LIVING_PSCD = "living_postal_code";
    private static final String[] IDENT_COLUMNS = {IDENT_KEY_ID, IDENT_KEY_LNAME, IDENT_KEY_FNAME,
            IDENT_KEY_BIRTH_DAY, IDENT_KEY_BIRTH_PLACE, IDENT_KEY_LIVING_ADDR, IDENT_KEY_LIVING_CITY,
            IDENT_KEY_LIVING_PSCD};

    private static final String CERTIF_TABLE_NAME = "certificates";
    private static final String CERTIF_KEY_ID = "id";
    private static final String CERTIF_KEY_IDENTIY = "identity";
    private static final String CERTIF_KEY_REASON = "reason";
    private static final String CERTIF_KEY_DATE = "date";
    private static final String CERTIF_KEY_FILE = "file";
    private static final String[] CERTIF_COLUMNS = {CERTIF_KEY_ID, CERTIF_KEY_IDENTIY,
            CERTIF_KEY_REASON, CERTIF_KEY_DATE, CERTIF_KEY_FILE};


    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE identities ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "lastname TEXT, firstname TEXT, "
                + "birthday INTEGER, birthplace TEXT, "
                + "living_address TEXT, living_city TEXT, living_postal_code TEXT); ";
        db.execSQL(CREATION_TABLE);
        CREATION_TABLE = "CREATE TABLE certificates ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "identity INTEGER, reason TEXT, "
                + "date INTEGER, file TEXT)";
        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + IDENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CERTIF_TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteIdentity(Identity identity) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(IDENT_TABLE_NAME, "id = ?", new String[]{String.valueOf(identity.getId())});
        db.close();
    }

    private Identity getIdentityFromCursor(Cursor cursor) {
        return new Identity(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                new Date(cursor.getLong(3)),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7));
    }

    public Identity getIdentityFromId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(IDENT_TABLE_NAME, // a. table
                IDENT_COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        return getIdentityFromCursor(cursor);
    }

    public List<Identity> getIdentities() {
        List<Identity> identities = new LinkedList<>();
        String query = "SELECT * FROM " + IDENT_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                identities.add(getIdentityFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        return identities;
    }

    public void addIdentity(Identity identity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDENT_KEY_LNAME, identity.getLastName());
        values.put(IDENT_KEY_FNAME, identity.getFirstName());
        values.put(IDENT_KEY_BIRTH_DAY, identity.getBirthday().getTime());
        values.put(IDENT_KEY_BIRTH_PLACE, identity.getBirthplace());
        values.put(IDENT_KEY_LIVING_ADDR, identity.getLivingAddress());
        values.put(IDENT_KEY_LIVING_CITY, identity.getLivingCity());
        values.put(IDENT_KEY_LIVING_PSCD, identity.getLivingPostalCode());
        // insert
        db.insert(IDENT_TABLE_NAME, null, values);
        db.close();
    }

    public int updateIdentity(Identity identity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDENT_KEY_LNAME, identity.getLastName());
        values.put(IDENT_KEY_FNAME, identity.getFirstName());
        values.put(IDENT_KEY_BIRTH_DAY, identity.getBirthday().getTime());
        values.put(IDENT_KEY_BIRTH_PLACE, identity.getBirthplace());
        values.put(IDENT_KEY_LIVING_ADDR, identity.getLivingAddress());
        values.put(IDENT_KEY_LIVING_CITY, identity.getLivingCity());
        values.put(IDENT_KEY_LIVING_PSCD, identity.getLivingPostalCode());

        int i = db.update(IDENT_TABLE_NAME, // table
                values, // column/value
                "id = ?", // selections
                new String[]{String.valueOf(identity.getId())});
        db.close();

        return i;
    }





    public void deleteCertificate(Certificate certificate) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CERTIF_TABLE_NAME, "id = ?", new String[]{String.valueOf(certificate.getId())});
        db.close();
    }

    private Certificate getCertificateCursor(Cursor cursor) {
        return new Certificate(cursor.getInt(0),
                getIdentityFromId(cursor.getInt(1)),
                EnumReason.valueOf(cursor.getString(2)),
                new Date(cursor.getLong(3)),
                cursor.getString(4));
    }

    public Certificate getCertificateFromId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CERTIF_TABLE_NAME, // a. table
                CERTIF_COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        return getCertificateCursor(cursor);
    }

    public List<Certificate> getCertificates() {
        List<Certificate> certificates = new LinkedList<>();
        String query = "SELECT * FROM " + CERTIF_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                certificates.add(getCertificateCursor(cursor));
            } while (cursor.moveToNext());
        }

        return certificates;
    }

    public void addCertificate(Certificate certificate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CERTIF_KEY_IDENTIY, certificate.getIdentity().getId());
        values.put(CERTIF_KEY_REASON, certificate.getReason().name());
        values.put(CERTIF_KEY_DATE, certificate.getDate().getTime());
        values.put(CERTIF_KEY_FILE, certificate.getFile());
        // insert
        db.insert(CERTIF_TABLE_NAME, null, values);
        db.close();
    }

    public int updateCertificate(Certificate certificate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CERTIF_KEY_IDENTIY, certificate.getIdentity().getId());
        values.put(CERTIF_KEY_REASON, certificate.getReason().name());
        values.put(CERTIF_KEY_DATE, certificate.getDate().getTime());
        values.put(CERTIF_KEY_FILE, certificate.getFile());

        int i = db.update(CERTIF_TABLE_NAME, // table
                values, // column/value
                "id = ?", // selections
                new String[]{String.valueOf(certificate.getId())});
        db.close();

        return i;
    }

}