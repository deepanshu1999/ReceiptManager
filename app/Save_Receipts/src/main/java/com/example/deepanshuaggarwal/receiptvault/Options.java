package com.example.deepanshuaggarwal.receiptvault;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Options extends AppCompatActivity {
    Spinner spinner;
    Button btn;
    String[] paths={"Daily","Weekly","Monthly","When new receipt added","Never"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Options.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(paths[i].equals("Daily"))
                globals.userchoicebackup="Daily";
                else if(paths[i].equals("Weekly"))
                    globals.userchoicebackup="Weekly";
                else if(paths[i].equals("Monthly"))
                    globals.userchoicebackup="Monthly";
                else if(paths[i].equals("Never"))
                    globals.userchoicebackup="Never";
                else if (paths[i].equals("When new receipt added"))
                    globals.userchoicebackup="OnItemAdd";

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take Backup
            }
        });
    }
}
