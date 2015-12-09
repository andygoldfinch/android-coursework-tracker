package uk.ac.leeds.atcg_gdl.courseworktracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;

/**
 * An activity allowing the creation and editing of courseworks.
 */
public class EditActivity extends AppCompatActivity {

    /**
     * Creates the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    /**
     * Attempts to save the coursework when the save button is pressed.
     * @param view
     */
    public void handleButtonSave(View view) {
        String moduleName = getModuleName();
        String courseworkName = getCourseworkName();
        Date deadline = getDeadline();
        int weight = getWeight();
        String notes = getNotes();
        boolean completed = getCompleted();

        if (weight == -1) {
            return;
        }

        Coursework coursework = new Coursework(moduleName, courseworkName, deadline, weight, notes, completed);
        new Database(getApplicationContext()).saveCoursework(coursework);
    }

    /**
     * @return The module name from the ui
     */
    private String getModuleName() {
        EditText text = (EditText) findViewById(R.id.editTextModule);
        return text.getText().toString();
    }

    /**
     * @return The coursework name from the ui
     */
    private String getCourseworkName() {
        EditText text = (EditText) findViewById(R.id.editTextName);
        return text.getText().toString();
    }

    /**
     * @return The deadline date from the ui
     */
    private Date getDeadline() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePickerDeadline);
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                datePicker.getDayOfMonth(),
                datePicker.getMonth(),
                datePicker.getYear()
        );

        return calendar.getTime();
    }

    /**
     * Attempts to retrived the weight value from the ui.
     * Displays a toast to the user if validation fails.
     *
     * @return The weight value, or -1 if it is not valid.
     */
    private int getWeight() {
        String weightText = ((EditText) findViewById(R.id.editTextWeight)).getText().toString();
        if (weightText.length() == 0) {
            createToast("Weight cannot be blank");
            return -1;
        }

        int weight = Integer.parseInt(weightText);
        if (weight < 0 || weight > 100) {
            createToast("Weight must be between 0 & 100");
            return -1;
        }

        return weight;
    }

    /**
     * Shows a toast for the current application context.
     *
     * @param message The message to display
     */
    private void createToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * @return The notes text from the ui
     */
    private String getNotes() {
        EditText text = (EditText) findViewById(R.id.editTextNotes);
        return text.getText().toString();
    }

    /**
     * @return Whether the coursework has been marked as completed.
     */
    private boolean getCompleted()
    {
        CheckBox checkbox = (CheckBox)findViewById(R.id.checkbox);
        return checkbox.isChecked();
    }
}
