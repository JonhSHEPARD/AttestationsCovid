package ovh.jonhshepard.attestations;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.Calendar;

import ovh.jonhshepard.attestations.storage.Identity;
import ovh.jonhshepard.attestations.wrappers.Activity;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

/**
 * Activity to Edit/Add a Task
 */
public class EditAddIdentityActivity extends Activity {

    private EditText lastname;
    private EditText firstname;
    private DatePickerDialog birthdateDialog;
    private EditText birthdate;
    private EditText birthplace;
    private EditText living_address;
    private EditText living_city;
    private EditText living_postalcode;

    private Identity identity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_identity);

        // Validation system instantiation
        AwesomeValidation validation = new AwesomeValidation(UNDERLABEL);
        validation.setContext(this);

        // Getting elements of the Activity and creating validations objects
        lastname = findViewById(R.id.editTextLastName);
        validation.addValidation(this, R.id.editTextLastName, input -> input.length() > 0, R.string.err_lastname);
        firstname = findViewById(R.id.editTextFirstName);
        validation.addValidation(this, R.id.editTextFirstName, input -> input.length() > 0, R.string.err_firstname);

        birthdate = findViewById(R.id.editTextBirthDay);
        birthdate.setInputType(InputType.TYPE_NULL);
        birthdate.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            birthdateDialog = new DatePickerDialog(EditAddIdentityActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year1, monthOfYear, dayOfMonth);
                        birthdate.setText(Util.formatDate(calendar.getTime()));
                    }, year, month, day);
            birthdateDialog.show();
        });
        birthplace = findViewById(R.id.editTextBirthPlace);
        validation.addValidation(this, R.id.editTextBirthPlace, input -> input.length() > 0, R.string.birthplace);


        living_address = findViewById(R.id.editTextLivingAddress);
        validation.addValidation(this, R.id.editTextLivingAddress, input -> input.length() > 0, R.string.err_living_address);
        living_city = findViewById(R.id.editTextLivingCity);
        validation.addValidation(this, R.id.editTextLivingCity, input -> input.length() > 0, R.string.err_living_city);
        living_postalcode = findViewById(R.id.editTextLivingPostalCode);
        validation.addValidation(this, R.id.editTextLivingPostalCode, input -> input.length() == 5, R.string.err_living_postal_code);

        Button validate = findViewById(R.id.buttonEdit);


        // If in edit mode, getting Task and setting elements
        int edit = getIntent().getIntExtra("edit", -1);

        if (edit > 0)
            identity = getDB().getIdentityFromId(edit);

        if (identity != null) {
            lastname.setText(identity.getLastName());
            firstname.setText(identity.getFirstName());
            birthdate.setText(identity.getLastName());
            birthplace.setText(identity.getBirthplace());
            living_address.setText(identity.getLivingAddress());
            living_city.setText(identity.getLivingCity());
            living_postalcode.setText(identity.getLivingPostalCode());
            validate.setText(R.string.edit);
        }

        // Button validation
        validate.setOnClickListener(v -> {
            // Validating identity
            if (!validation.validate())
                return;
            Identity identity = validate();
            if (identity == null)
                return;
            // If identity exist (id > 0), updating it, else creating it
            if (identity.getId() == -1) {
                getDB().addIdentity(identity);
            } else {
                getDB().updateIdentity(identity);
            }

            finish();
        });
    }

    /**
     * Function to validate inputs
     *
     * @return {@link Identity} identity created with input (ID = -1 if new identity), null if no changes
     */
    private Identity validate() {
        if (lastname.getText().length() == 0)
            return null;
        if (firstname.getText().length() == 0)
            return null;
        Calendar birthdayC = Util.calendarFromString(birthdate.getText().toString());
        if (birthdayC == null)
            return null;
        if (birthplace.getText().length() == 0)
            return null;
        if (living_address.getText().length() == 0)
            return null;
        if (living_city.getText().length() == 0)
            return null;
        if (living_postalcode.getText().length() != 5)
            return null;

        // If identity exists and inputs are not all equal with the original, updating object
        if (identity != null) {
            if (lastname.getText().toString().equals(identity.getLastName())
                    && firstname.getText().toString().equals(identity.getFirstName())
                    && birthdayC.getTime().compareTo(identity.getBirthday()) == 0
                    && birthplace.getText().toString().equals(identity.getBirthplace())
                    && living_address.getText().toString().equals(identity.getLivingAddress())
                    && living_city.getText().toString().equals(identity.getLivingCity())
                    && living_postalcode.getText().toString().equals(identity.getLivingPostalCode()))
                return null;
            identity.setLastName(lastname.getText().toString());
            identity.setFirstName(firstname.getText().toString());
            identity.setBirthday(birthdayC.getTime());
            identity.setBirthplace(birthplace.getText().toString());
            identity.setLivingAddress(living_address.getText().toString());
            identity.setLivingCity(living_city.getText().toString());
            identity.setLivingPostalCode(living_postalcode.getText().toString());
            return identity;
        }

        // Return new identity if not in editing
        return new Identity(lastname.getText().toString(),
                firstname.getText().toString(),
                birthdayC.getTime(),
                birthplace.getText().toString(),
                living_address.getText().toString(),
                living_city.getText().toString(),
                living_postalcode.getText().toString());
    }
}