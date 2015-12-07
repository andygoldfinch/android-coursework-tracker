package uk.ac.leeds.atcg_gdl.courseworktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonAdd;
    public static final String MESSAGE_KEY = "uk.ac.leeds.atcg_gdl.courseworktracker.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
    }

    public void handleAddButtonClick(View view)
    {
        Intent addIntent = new Intent(this, EditActivity.class);
        startActivity(addIntent);
    }
}
