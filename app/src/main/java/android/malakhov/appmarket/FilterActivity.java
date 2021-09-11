package android.malakhov.appmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String PERSISTANT_STORAGE_NAME = "Filter";
    private TextView smartphone, planshet, watch, lastBtn;
    private EditText FromOZU, ToOZU, FromFleash, ToFlesh, FromCapacity, ToCapacity,
            FromDiagonal,ToDiagonal,FromMain,ToMain,FromFront, ToFront,
            FromYear, ToYear, FromPrice, ToPrice;
    private CheckBox Black, white, silver,red,purple,pink,gold,biruz,yellow,green,blue,korall;

    private static SharedPreferences settings = null;
    public static SharedPreferences.Editor editor = null;
    private static Context context = null;



    public void init(){

        FromOZU = findViewById(R.id.FromOZU);
        ToOZU = findViewById(R.id.ToOZU);
        FromFleash = findViewById(R.id.FromFleash);
        ToFlesh = findViewById(R.id.ToFlesh);
        FromCapacity = findViewById(R.id.FromCapacity);
        ToCapacity = findViewById(R.id.ToCapacity);
        FromDiagonal = findViewById(R.id.FromDiagonal);
        ToDiagonal = findViewById(R.id.ToDiagonal);
        FromMain = findViewById(R.id.FromMain);
        ToMain = findViewById(R.id.ToMain);
        FromFront = findViewById(R.id.FromFront);
        ToFront = findViewById(R.id.ToFront);
        FromYear = findViewById(R.id.FromYear);
        ToYear = findViewById(R.id.ToYear);
        FromPrice = findViewById(R.id.FromPrice);
        ToPrice = findViewById(R.id.ToPrice);
        Black = findViewById(R.id.Black);
        white = findViewById(R.id.white);
        silver = findViewById(R.id.silver);
        red = findViewById(R.id.red);
        purple = findViewById(R.id.purple);
        pink = findViewById(R.id.pink);
        gold = findViewById(R.id.gold);
        biruz = findViewById(R.id.biruz);
        yellow = findViewById(R.id.yellow);
        green = findViewById(R.id.green);
        blue = findViewById(R.id.blue);
        korall = findViewById(R.id.korall);
        smartphone = findViewById(R.id.smartphone);
        planshet = findViewById(R.id.planshet);
        watch = findViewById(R.id.watch);
    }

    public void createSharedPref(){
        settings = this.getSharedPreferences(PERSISTANT_STORAGE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public void updateUI(){

        if (settings.getString("Type","").equals("Смартфон")) {
            lastBtn = smartphone;
            lastBtn.setTextColor(getResources().getColor(R.color.price));
        }else
        if (settings.getString("Type","").equals("Планшет")) {
            lastBtn = planshet;
            lastBtn.setTextColor(getResources().getColor(R.color.price));
        }else
        if (settings.getString("Type","").equals("Часы")) {
            lastBtn = watch;
            lastBtn.setTextColor(getResources().getColor(R.color.price));
        }



        FromOZU.setText(settings.getString("FromOZU", ""));
        ToOZU.setText(settings.getString("ToOZU", ""));
        FromFleash.setText(settings.getString("FromFleash", ""));
        ToFlesh.setText(settings.getString("ToFlesh", ""));
        FromCapacity.setText(settings.getString("FromCapacity", ""));
        ToCapacity.setText(settings.getString("ToCapacity", ""));
        FromDiagonal.setText(settings.getString("FromDiagonal", ""));
        ToDiagonal.setText(settings.getString("ToDiagonal", ""));
        FromMain.setText(settings.getString("FromMain", ""));
        ToMain.setText(settings.getString("ToMain", ""));
        FromFront.setText(settings.getString("FromFront", ""));
        ToFront.setText(settings.getString("ToFront", ""));
        FromYear.setText(settings.getString("FromYear", ""));
        ToYear.setText(settings.getString("ToYear", ""));
        FromPrice.setText(settings.getString("FromPrice", ""));
        ToPrice.setText(settings.getString("ToPrice", ""));


        Black.setChecked(settings.getBoolean("Black", false));
        white.setChecked(settings.getBoolean("white", false));
        silver.setChecked(settings.getBoolean("silver", false));
        red.setChecked(settings.getBoolean("red", false));
        purple.setChecked(settings.getBoolean("purple", false));
        pink.setChecked(settings.getBoolean("pink", false));
        gold.setChecked(settings.getBoolean("gold", false));
        biruz.setChecked(settings.getBoolean("biruz", false));
        yellow.setChecked(settings.getBoolean("yellow", false));
        green.setChecked(settings.getBoolean("green", false));
        blue.setChecked(settings.getBoolean("blue", false));
        korall.setChecked(settings.getBoolean("korall", false));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        init();

        File f = new File(
                "/data/data/android.malakhov.appmarket/shared_prefs/Filter.xml");
        if (f.exists()) {
            createSharedPref();
            updateUI();
        }else
            createSharedPref();

        smartphone = findViewById(R.id.smartphone);
        planshet = findViewById(R.id.planshet);
        watch = findViewById(R.id.watch);

        smartphone.setOnClickListener(this);
        planshet.setOnClickListener(this);
        watch.setOnClickListener(this);

        findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    editor.putString("FromPrice", FromPrice.getText().toString());
                    editor.putString("ToPrice", ToPrice.getText().toString());
                    editor.putString("FromOZU", FromOZU.getText().toString());
                    editor.putString("ToOZU", ToOZU.getText().toString());
                    editor.putString("FromFleash", FromFleash.getText().toString());
                    editor.putString("ToFlesh", ToFlesh.getText().toString());
                    editor.putString("FromCapacity", FromCapacity.getText().toString());
                    editor.putString("ToCapacity", ToCapacity.getText().toString());
                    editor.putString("FromDiagonal", FromDiagonal.getText().toString());
                    editor.putString("ToDiagonal", ToDiagonal.getText().toString());
                    editor.putString("FromMain", FromMain.getText().toString());
                    editor.putString("ToMain", ToMain.getText().toString());
                    editor.putString("FromFront", FromFront.getText().toString());
                    editor.putString("ToFront", ToFront.getText().toString());
                    editor.putString("FromYear", FromYear.getText().toString());
                    editor.putString("ToYear", ToYear.getText().toString());


                if (lastBtn != null)
                    editor.putString("Type", lastBtn.getText().toString());
                else
                    editor.putString("Type", "");


                    editor.putBoolean("Black",Black.isChecked());
                    editor.putBoolean("white",white.isChecked());
                    editor.putBoolean("silver",silver.isChecked());
                    editor.putBoolean("red",red.isChecked());
                    editor.putBoolean("purple",purple.isChecked());
                    editor.putBoolean("pink",pink.isChecked());
                    editor.putBoolean("gold",gold.isChecked());
                    editor.putBoolean("biruz",biruz.isChecked());
                    editor.putBoolean("yellow",yellow.isChecked());
                    editor.putBoolean("green",green.isChecked());
                    editor.putBoolean("blue",blue.isChecked());
                    editor.putBoolean("korall",korall.isChecked());


                editor.apply();
                MarketFragment.settings = FilterActivity.settings;
                finish();
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastBtn != null)
                lastBtn.setTextColor(getResources().getColor(R.color.typeTV));
                editor.clear().apply();
                updateUI();
                MarketFragment.settings = null;
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (lastBtn != null)
            lastBtn.setTextColor(getResources().getColor(R.color.typeTV));

        ((TextView)v).setTextColor(getResources().getColor(R.color.price));
        lastBtn = (TextView) v;
    }

}