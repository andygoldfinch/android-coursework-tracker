package uk.ac.leeds.atcg_gdl.courseworktracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    public void handleButtonSave(View view)
    {
        EditText editTextModule = (EditText) findViewById(R.id.editTextModule);
        String moduleName = editTextModule.getText().toString();

        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        String courseworkName = editTextName.getText().toString();

        DatePicker datePickerDeadline = (DatePicker) findViewById(R.id.datePickerDeadline);
        Date deadline = getDate(datePickerDeadline);

        EditText editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        int weight = getWeight(editTextWeight);
        if (weight == -1) {
            return;
        }

        EditText editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        String notes = editTextNotes.getText().toString();

        CheckBox checkBoxCompleted = (CheckBox) findViewById(R.id.checkbox);
        boolean completed = checkBoxCompleted.isChecked();

        Coursework coursework = new Coursework(moduleName, courseworkName, deadline, weight, notes, completed);
        new Database(getApplicationContext()).saveCoursework(coursework);
    }

    public int getWeight(EditText editText)
    {
        int weight;
        Toast weightErrorToast;
        if (editText.getText().toString() == "")
        {
            weight = 0;
            return weight;
        }

        weight = Integer.parseInt(editText.getText().toString());
        if (weight < 0 || weight > 100)
        {
            weightErrorToast = Toast.makeText(
                    getApplicationContext(),
                    "Please enter a weight between 0 & 100",
                    Toast.LENGTH_LONG
            );
            weightErrorToast.show();
            return -1;
        }
        return weight;
    }

    public Date getDate(DatePicker datePicker)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                datePicker.getDayOfMonth(),
                datePicker.getMonth(),
                datePicker.getYear()
        );

        return calendar.getTime();
    }
}
