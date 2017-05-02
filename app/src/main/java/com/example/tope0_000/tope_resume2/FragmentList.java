package com.example.tope0_000.tope_resume2;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FragmentList extends ListFragment {

    ArrayList<String> skillList = new ArrayList<String>();
    ArrayList<String> descriptionList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    public boolean darkTheme = false;

    // define the variables for the widgets
    private ListView listView;
    private TextView descriptionTextView;

    // define the SharedPreferences object
    private SharedPreferences savedValues;

    // define the instance variables that should be saved
    private String skillText = "";
    private int position = 0;

    // default constructor
    public FragmentList() throws JSONException {
    }


    // loads JSON file from Asset folder
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("mySkills.json");
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


    // onCreate method
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savedValues = this.getActivity().getSharedPreferences("SavedValues", MODE_PRIVATE);

        //Get theme saved value
        darkTheme = savedValues.getBoolean("theme", false);

        // get references to the widgets
        descriptionTextView = (TextView) getActivity().findViewById(R.id.descriptionText);
        listView = (ListView) getActivity().findViewById(android.R.id.list);

        // get SharedPreferences object
        savedValues = getActivity().getSharedPreferences("SavedValues",MODE_PRIVATE);

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("skillsArray");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String skill_value = jo_inside.getString("skillName");
                String description_value = jo_inside.getString("skillDescription");

                //Add your values in your `ArrayList` as below:
                skillList.add(skill_value);
                descriptionList.add(description_value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(darkTheme)
            adapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_list_layout_dark, skillList);
        else
            adapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_list_layout, skillList);
        setListAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        for (int i = 0; i < l.getChildCount(); i++) {
            if(pos == i ){
                l.getChildAt(i).setBackgroundColor(Color.LTGRAY);
            }else{
                l.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
        TextView textView = (TextView) getActivity().findViewById(R.id.descriptionText);
        textView.setText(descriptionList.get(pos));
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putInt("position", pos);
        position = pos;
        editor.commit();
        listView = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onPause() {
        descriptionTextView = (TextView) getActivity().findViewById(R.id.descriptionText);
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("skillText", descriptionTextView.getText().toString());
        editor.putInt("position", position);
        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        savedValues = getActivity().getSharedPreferences("SavedValues", MODE_PRIVATE);
        // get the instance variables
        skillText = savedValues.getString("skillText", "");
        position = savedValues.getInt("position", 0);

        if(listView != null)
            listView.getChildAt(position).setBackgroundColor(Color.LTGRAY);

        // set the instance variables on their widgets
        descriptionTextView = (TextView) getActivity().findViewById(R.id.descriptionText);
        descriptionTextView.setText(skillText);

    }

}
