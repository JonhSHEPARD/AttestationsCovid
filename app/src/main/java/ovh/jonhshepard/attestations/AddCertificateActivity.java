package ovh.jonhshepard.attestations;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.Calendar;

import ovh.jonhshepard.attestations.parts.IdentityAdapter;
import ovh.jonhshepard.attestations.parts.ReasonAdapter;
import ovh.jonhshepard.attestations.storage.Certificate;
import ovh.jonhshepard.attestations.storage.EnumReason;
import ovh.jonhshepard.attestations.storage.Identity;
import ovh.jonhshepard.attestations.wrappers.ActivityWithIdentityList;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

/**
 * Activity to Edit/Add a Task
 */
public class AddCertificateActivity extends ActivityWithIdentityList {

    private Spinner identity;
    private Spinner reason;
    private DatePickerDialog datePickerDialog;
    private EditText date;
    private TimePickerDialog timePickerDialog;
    private EditText time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certificate);

        // Validation system instantiation
        AwesomeValidation validation = new AwesomeValidation(UNDERLABEL);
        validation.setContext(this);

        // Getting elements of the Activity and creating validations objects
        identity = findViewById(R.id.spinnerIdentity);
        identity.setAdapter(new IdentityAdapter(this, getDB().getIdentities()));
        reason = findViewById(R.id.spinnerReason);
        reason.setAdapter(new ReasonAdapter(this));

        date = findViewById(R.id.editTextDate);
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            datePickerDialog = new DatePickerDialog(AddCertificateActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year1, monthOfYear, dayOfMonth);
                        date.setText(Util.formatDate(calendar.getTime()));
                    }, year, month, day);
            datePickerDialog.show();
        });


        time = findViewById(R.id.editTextTime);
        time.setInputType(InputType.TYPE_NULL);
        time.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int min = cldr.get(Calendar.MINUTE);
            // date picker dialog
            timePickerDialog = new TimePickerDialog(AddCertificateActivity.this,
                    (view, hourOfDay, minute) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day, hourOfDay, minute);
                        time.setText(Util.formatTime(calendar.getTime()));
                    }, hour, min, true);
            timePickerDialog.show();
        });

        Button validate = findViewById(R.id.buttonEdit);

        // Button validation
        validate.setOnClickListener(v -> {
            // Validating identity
            if (!validation.validate())
                return;
            Certificate certificate = validate();
            if (certificate == null)
                return;
            // If identity exist (id > 0), updating it, else creating it
            if (certificate.getId() == -1) {
                getDB().addCertificate(certificate);
            }

            finish();
        });
    }

    @Override
    public void updateList() {

    }

    /**
     * Function to validate inputs
     *
     * @return {@link Certificate} certificate created with input (ID = -1 if new identity), null if no changes
     */
    private Certificate validate() {
        if (identity.getSelectedItem() == null || !(identity.getSelectedItem() instanceof Identity))
            return null;
        Identity ident = (Identity) identity.getSelectedItem();
        if (reason.getSelectedItem() == null || !(reason.getSelectedItem() instanceof EnumReason))
            return null;
        EnumReason reas = (EnumReason) reason.getSelectedItem();

        Calendar dat = Util.calendarFromString(date.getText().toString());
        if (time.getText().length() != 5)
            return null;
        String[] spl = time.getText().toString().split(":");
        if (spl.length != 2)
            return null;
        try {
            dat.set(Calendar.HOUR_OF_DAY, Integer.parseInt(spl[0]));
            dat.set(Calendar.MINUTE, Integer.parseInt(spl[1]));
        } catch (Exception e) {
            return null;
        }

        if (dat.before(Calendar.getInstance().getTime()))
            return null;

        // Return new identity if not in editing
        Certificate certificate = new Certificate(ident, reas, dat.getTime(), "");
        certificate.setFile(Util.manipulatePdf(certificate));

        return certificate.getFile() == null ? null : certificate;
    }
}