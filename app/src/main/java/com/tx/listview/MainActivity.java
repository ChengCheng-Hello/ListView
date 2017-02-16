package com.tx.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.teacherList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherListActivity.launch(MainActivity.this);
            }
        });

        findViewById(R.id.teacherSwipeList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherSwipeListActivity.launch(MainActivity.this);
            }
        });
    }
}
