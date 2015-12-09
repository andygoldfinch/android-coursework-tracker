package uk.ac.leeds.atcg_gdl.courseworktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.*;

/**
 * An activity allowing the creation and editing of courseworks.
 */
public class EditActivity extends AppCompatActivity {

    private EditText textModule;
    private EditText textName;
    private DatePicker datePicker;
    private EditText weightText;
    private EditText textNotes;
    private CheckBox checkbox;

    public EditActivity() {

    }

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

        Intent startingIntent = getIntent();
        if (startingIntent.hasExtra("coursework")) {
            Coursework coursework = (Coursework) startingIntent.getSerializableExtra("coursework");
            displayCoursework(coursework);
            textName.setEnabled(false);
            textModule.setEnabled(false);

            setTitle(R.string.edit_coursework_title);
        } else {
            setTitle(R.string.add_coursework_title);
        }
    }

    /**
     * Get references to all of the widgets and assign them to the fields in the class.
     */
    private void loadWidgets() {
        textModule = (EditText) findViewById(R.id.editTextModule);
        textName = (EditText) findViewById(R.id.editTextName);
        datePicker = (DatePicker) findViewById(R.id.datePickerDeadline);
        weightText = (EditText) findViewById(R.id.editTextWeight);
        textNotes = (EditText) findViewById(R.id.editTextNotes);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
    }

    /**
     * Attempts to save the coursework when the save button is pressed.
     *
     * @param view
     */
    public void handleButtonSave(View view) {
        String moduleName = getModuleName();
        String courseworkName = getCourseworkName();
        Date deadline = getDeadline();
        int weight = getWeight();
        String notes = getNotes();
        boolean completed = getCompleted();

        if (moduleName.length() < 1 || courseworkName.length() < 1) {
            createToast("Module name and coursework name cannot be blank");
            return;
        }

        if (weight < 0) {
            createToast("Weight must be a number between 0 and 100");
            return;
        }

        Coursework coursework = new Coursework(moduleName, courseworkName, deadline, weight, notes, completed, false);
        new Database(getApplicationContext()).saveCoursework(coursework);
        createToast("Coursework saved");
        finish();
    }

    private void displayCoursework(Coursework coursework) {
        setModuleName(coursework.getModuleName());
        setCourseworkName(coursework.getCourseworkName());
        setDeadline(coursework.getDeadline());
        setWeight(coursework.getWeight());
        setNotes(coursework.getNotes());
        setCompleted(coursework.getCompleted());
    }

    /**
     * @return The module name from the ui
     */
    private String getModuleName() {
        return textModule.getText().toString();
    }

    /**
     * @param text The module name
     */
    private void setModuleName(String text) {
        textModule.setText(text);
    }

    /**
     * @return The coursework name from the ui
     */
    private String getCourseworkName() {
        return textName.getText().toString();
    }

    /**
     * @param text The coursework name
     */
    private void setCourseworkName(String text) {
        textName.setText(text);
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
     * Attempts to retrived the weight value from the ui.
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
     * @param number The weight of the coursework
     */
    private void setWeight(int number) {
        weightText.setText(String.valueOf(number));
    }


    /**
     * @return The notes text from the ui
     */
    private String getNotes() {
        return textNotes.getText().toString();
    }

    /**
     * @param text The notes for the coursework
     */
    private void setNotes(String text) {
        textNotes.setText(text);
    }

    /**
     * @return Whether the coursework has been marked as completed.
     */
    private boolean getCompleted() {
        return checkbox.isChecked();
    }

    /**
     * @param complete The value for the completed checkbox
     */
    private void setCompleted(boolean complete) {
        checkbox.setChecked(complete);
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
