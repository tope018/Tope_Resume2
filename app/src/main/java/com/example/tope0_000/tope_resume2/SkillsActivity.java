package com.example.tope0_000.tope_resume2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SkillsActivity extends AppCompatActivity {
    private SharedPreferences savedValues;
    public boolean darkTheme = false;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        //Get theme saved value
        darkTheme = savedValues.getBoolean("theme", false);


        //Set Theme
        if(darkTheme)
            this.setTheme(R.style.AppTheme_dark);

        setContentView(R.layout.activity_skills);
    }

    //Displays the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theme_switching, menu);
        return true;
    }

    //Handles the menu item events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        editor = savedValues.edit();
        switch (item.getItemId()) {
            case R.id.menu_light:
                //TODO
                darkTheme = false;
                editor.putBoolean("theme", darkTheme);
                editor.commit();
                recreate();
                return true;

            case R.id.menu_dark:
                //TODO
                darkTheme = true;
                editor.putBoolean("theme", darkTheme);
                editor.commit();
                recreate();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
