package uk.ac.leeds.atcg_gdl.courseworktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button buttonAdd;
    private ListView listView;

    public static final String MESSAGE_KEY = "uk.ac.leeds.atcg_gdl.courseworktracker.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        listView = (ListView) findViewById(R.id.listView);

        Coursework[] testCwArray = {new Coursework("t1", "t2", new Date(), 24, "", false),
                new Coursework("test1", "test2", new Date(), 28, "n", true)};
        populateListView(testCwArray);
    }

    public void handleButtonAdd(View view)
    {
        Intent addIntent = new Intent(this, EditActivity.class);
        startActivity(addIntent);
    }

    private void populateListView(Coursework[] courseworkArray)
    {
        ArrayList<String> courseworkProcessed = new ArrayList<String>();
        for (int i = 0; i < courseworkArray.length; ++i)
        {
            courseworkProcessed.add(
                            courseworkArray[i].getCourseworkName() + "\n" +
                            courseworkArray[i].getModuleName() + " - " +
                            courseworkArray[i].getDeadline());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, courseworkProcessed);
        listView.setAdapter(arrayAdapter);
    }
}
