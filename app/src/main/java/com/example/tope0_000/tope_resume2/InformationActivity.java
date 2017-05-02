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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    private Integer images[] =
            {R.drawable.firefox, R.drawable.att, R.drawable.bmw, R.drawable.capain_america,
                    R.drawable.android, R.drawable.pg};
    private int currImage = 0;

    //define variables for the widgets
    private ImageView imageViewCycle;
    private Button previousButton;
    private Button nextButton;
    private Button skillsButton;
    private TextView testDB;

    // define the SharedPreferences object and editor
    private SharedPreferences savedValues;
    private SharedPreferences.Editor editor;

    // define global variable
    public boolean darkTheme = false;

    // define instance variables that should be saved
    private Integer currentImage;
    private boolean nextButtonClickable = true;
    private boolean previousButtonClickable;
    private boolean theme = false;

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
        setContentView(R.layout.activity_information);

        // get references to the widgets
        imageViewCycle = (ImageView) findViewById(R.id.imageViewCycle);
        previousButton = (Button) findViewById(R.id.previousButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        skillsButton = (Button) findViewById(R.id.skillsButton);
        testDB = (TextView) findViewById(R.id.testDB);

        // set previous button to initially false
        previousButton.setEnabled(false);
        editor = savedValues.edit();
        editor.putBoolean("nextButtonClickable", nextButton.isEnabled());
        editor.commit();

        // set the listeners
        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        skillsButton.setOnClickListener(this);

        // set the initial image
        setCurrentImage();

        // construct the database from json file
        SkillDB db = new SkillDB(this);
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("skillsArray");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String skill_value = jo_inside.getString("skillName");
                String description_value = jo_inside.getString("skillDescription");

                //Add your values in your database
                Skill skill = new Skill(skill_value, description_value);
                db.insertSkill(skill);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*//Test the database
        //get db and StringBuilder objects
        StringBuilder sb = new StringBuilder();

        // display all players (id + name)
        ArrayList<Skill> players = db.getSkills();
        for (Skill p : players) {
            sb.append(p.getId() + "|" + p.getName() + "\n");
        }

        // display string on UI
        testDB.setText(sb.toString());*/
    }

    @Override
    public void onPause() {
        editor = savedValues.edit();
        editor.putBoolean("nextButtonClickable", nextButton.isEnabled());
        editor.putBoolean("previousButtonClickable", previousButton.isEnabled());
        editor.putInt("currentImage", currImage);
        editor.putBoolean("theme", darkTheme);
        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
        nextButtonClickable = savedValues.getBoolean("nextButtonClickable", false);
        previousButtonClickable = savedValues.getBoolean("previousButtonClickable", false);
        currentImage = savedValues.getInt("currentImage", 0);
        imageViewCycle.setImageResource(images[currentImage]);
        nextButton.setEnabled(nextButtonClickable);
        previousButton.setEnabled(previousButtonClickable);
        currImage = currentImage;
        darkTheme = savedValues.getBoolean("theme", false);

    }

    //Handles the button events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previousButton:
                prevImage();
                break;
            case R.id.nextButton:
                nextImage();
                break;
            case R.id.skillsButton:
                //TODO
                startActivity(new Intent(this, SkillsActivity.class));
                break;
        }
    }

    //Sets previous image for the image cycler
    private void prevImage() {
        currImage--;
        imageViewCycle.setImageResource(images[currImage]);
        nextButton.setEnabled(true);
        if (currImage == 0) {
            previousButton.setEnabled(false);
        }
    }

    //Sets next image for the image cycler
    private void nextImage() {
        currImage++;
        imageViewCycle.setImageResource(images[currImage]);
        previousButton.setEnabled(true);
        if (currImage == images.length - 1) {
            nextButton.setEnabled(false);
        }
    }

    //Sets current image on image cycler
    private void setCurrentImage() {
        imageViewCycle.setImageResource(images[currImage]);
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

    // loads JSON file from Asset folder
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("mySkills.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
