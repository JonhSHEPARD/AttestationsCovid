package ovh.jonhshepard.attestations;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ovh.jonhshepard.attestations.parts.IdentityAdapter;
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
    private DatePickerDialog datePickerDialog;
    private EditText date;
    private TimePickerDialog timePickerDialog;
    private EditText time;
    private Map<EnumReason, CheckBox> reasons;

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

        reasons = new HashMap<>();
        reasons.put(EnumReason.WORK, (CheckBox) findViewById(R.id.checkReasonWork));
        reasons.put(EnumReason.FOOD, (CheckBox) findViewById(R.id.checkReasonFood));
        reasons.put(EnumReason.MEDIC, (CheckBox) findViewById(R.id.checkReasonMedic));
        reasons.put(EnumReason.ASSIST, (CheckBox) findViewById(R.id.checkReasonAssist));
        reasons.put(EnumReason.SPORT, (CheckBox) findViewById(R.id.checkReasonSport));
        reasons.put(EnumReason.CONVOC, (CheckBox) findViewById(R.id.checkReasonConvoc));
        reasons.put(EnumReason.MISSION, (CheckBox) findViewById(R.id.checkReasonMission));

        date = findViewById(R.id.editTextDate);
        date.setInputType(InputType.TYPE_NULL);
        date.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
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
            }
        });


        time = findViewById(R.id.editTextTime);
        time.setInputType(InputType.TYPE_NULL);
        time.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
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
            }
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

        List<EnumReason> reasonList = new ArrayList<>();
        for (EnumReason reason : reasons.keySet()) {
            CheckBox box = reasons.get(reason);

            if (box != null && box.isChecked())
                reasonList.add(reason);
        }
        if (reasonList.size() == 0)
            return null;

        // Return new identity if not in editing
        Certificate certificate = new Certificate(ident, reasonList, dat.getTime(), "");
        certificate.setFile(Util.manipulatePdf(this, certificate));

        return certificate.getFile() == null ? null : certificate;
    }
}