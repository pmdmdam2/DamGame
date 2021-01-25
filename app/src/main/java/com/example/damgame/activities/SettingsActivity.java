package com.example.damgame.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.damgame.R;
import com.example.damgame.fragments.SettingsFragment;
import com.example.damgame.utils.GameUtil;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Actividad para las preferencias de la aplicación
 * @author 2º DAM - IES Antonio Gala
 * @version 1.0
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTema();
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK,getIntent());
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.imBack:
                setResult(Activity.RESULT_OK,getIntent());
                finish();
                break;
        }
        return true;
    }

    private void setTema(){
        int sceneCode = Integer.valueOf(getDefaultSharedPreferences(this).getString("theme_setting","100"));
        switch(sceneCode){
            case GameUtil.TEMA_DESIERTO:
                setTheme(R.style.Desert_DamGame);
                break;
            default:
                setTheme(R.style.Desert_DamGame);
                break;
        }

    }
}