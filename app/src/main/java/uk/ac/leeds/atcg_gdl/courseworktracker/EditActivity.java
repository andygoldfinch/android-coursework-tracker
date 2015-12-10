package uk.ac.leeds.atcg_gdl.courseworktracker;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * An activity allowing the creation and editing of courseworks.
 */
public class EditActivity extends AppCompatActivity {

    private EditText textModuleName;
    private EditText textCourseworkName;
    private DatePicker datePicker;
    private EditText weightText;
    private EditText textNotes;
    private CheckBox checkbox;

    private Coursework coursework;

    /**
     * Creates the activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        loadWidgets();

        // Edit any coursework passed to the activity.
        if (getIntent().hasExtra("coursework")) {
            coursework = (Coursework) getIntent().getSerializableExtra("coursework");

            setCoursework(coursework);
            setTitle(R.string.edit_coursework_title);

            // Prevent editing the module and coursework name.
            textCourseworkName.setEnabled(false);
            textModuleName.setEnabled(false);
        } else {
            setTitle("Add Coursework");
        }
    }

    /**
     * Get references to all of the widgets and assign them to the fields in the class.
     */
    private void loadWidgets() {
        textModuleName = (EditText) findViewById(R.id.editTextModule);
        textCourseworkName = (EditText) findViewById(R.id.editTextName);
        datePicker = (DatePicker) findViewById(R.id.datePickerDeadline);
        weightText = (EditText) findViewById(R.id.editTextWeight);
        textNotes = (EditText) findViewById(R.id.editTextNotes);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
    }

    /**
     * @return True if the view is editing an existing coursework entry.
     */
    private boolean isEditing() {
        return (coursework != null);
    }

    /**
     * Attempts to retrieve the entered coursework instance.
     * Runs validation on inputs.
     *
     * @return The coursework, or null if validation failed.
     */
    private Coursework getCoursework() {
        String courseworkName = textCourseworkName.getText().toString();
        String moduleName = textModuleName.getText().toString();
        String notes = textNotes.getText().toString();
        boolean completed = checkbox.isChecked();

        Date deadline = getDeadline();
        int weight = getWeight();

        if (moduleName.length() < 1 || courseworkName.length() < 1) {
            createToast("Module name and coursework name cannot be blank");
            return null;
        }

        if (weight < 0) {
            createToast("Weight must be a number between 0 and 100");
            return null;
        }

        return new Coursework(moduleName, courseworkName, deadline, weight, notes, completed);
    }

    /**
     * Displays details for a coursework in the ui
     *
     * @param coursework The coursework to display
     */
    private void setCoursework(Coursework coursework) {
        textCourseworkName.setText(coursework.getCourseworkName());
        textModuleName.setText(coursework.getModuleName());
        weightText.setText(String.valueOf(coursework.getWeight()));
        textNotes.setText(coursework.getNotes());
        checkbox.setChecked(coursework.getCompleted());

        setDeadline(coursework.getDeadline());
    }

    /**
     * @return The deadline date from the ui
     */
    private Date getDeadline() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth()
        );

        return calendar.getTime();
    }

    /**
     * @param date The deadline of the coursework
     */
    private void setDeadline(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        datePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    /**
     * Attempts to retrieve the weight value from the ui.
     * Displays a toast to the user if validation fails.
     *
     * @return The weight value, or -1 if it is not valid.
     */
    private int getWeight() {
        String text = weightText.getText().toString();
        if (text.length() == 0) {
            return -1;
        }

        int weight = Integer.parseInt(text);
        if (weight < 0 || weight > 100) {
            return -1;
        }

        return weight;
    }

    /**
     * Adds items to the action bar when the activity starts.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    /**
     * Called when the a button in the action bar is pressed.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Save button
            case R.id.buttonSave:
                handleButtonSave();
                break;

            // Delete button
            case R.id.buttonDelete:
                handleButtonDelete();
                break;

            // Back button
            default:
                finish();
                break;
        }

        return true;
    }

    /**
     * Attempts to save the coursework when the save button is pressed.
     */
    public void handleButtonSave() {
        Coursework coursework = getCoursework();
        if (coursework == null) {
            return;
        }

        new Database(getApplicationContext()).saveCoursework(coursework);
        createToast(getString(R.string.toast_coursework_saved));
        finish();
    }

    /**
     * Called when the delete coursework button is pressed.
     */
    public void handleButtonDelete() {
        if (isEditing()) {
            new Database(getApplicationContext()).deleteCoursework(coursework);
        }

        createToast(getString(R.string.toast_coursework_deleted));
        finish();
    }

    /**
     * Shows a toast for the current application context.
     *
     * @param message The message to display
     */
    private void createToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}