package com.manthan.loginstore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

/**
 * Created by Manthan on 2/17/2015.
 */
public class Settings extends Activity {

    private NumberPicker numberPicker;
    private Button setButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        numberPicker = (NumberPicker)findViewById(R.id.numberPicker);
        setButton = (Button) findViewById(R.id.SettingSetButton);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = numberPicker.getValue();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Settings.this);
              //  int stored_value = pref.getInt("com.manthan.loginstore.value",-1);
                SharedPreferences.Editor edit = pref.edit();
                edit.putInt("com.manthan.loginstore.value",value);
//                if(stored_value==-1)
//                {
//                    edit.apply();
//                }
//                else
//                edit.commit();
                edit.apply();
                Toast toast = Toast.makeText(Settings.this,"Your new password will be current time + "+value,Toast.LENGTH_SHORT);
                toast.show();
                Intent mainLoginIntent = new Intent(Settings.this, MainLogin.class);
                startActivity(mainLoginIntent);
            }
        });
    }
}
