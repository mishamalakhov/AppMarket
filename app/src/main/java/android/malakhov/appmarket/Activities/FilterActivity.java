package android.malakhov.appmarket.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.malakhov.appmarket.Fragments.MarketFragment;
import android.malakhov.appmarket.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String PERSISTANT_STORAGE_NAME = "Filter";
    private TextView smartphone, planshet, watch, lastBtn;
    private static List<EditText> filtersView;
    private static List<CheckBox> colorsView;
    private static String[] colors = {"Черный","Белый","Серебристый","Красный","Фиолетовый","Розовый","Золотой",
            "Бирюзовый","Желтый","Зеленый","Синий","Коралловый"};
    private static String[] filters = {"FromPrice","ToPrice","FromOZU","ToOZU","FromFlesh","ToFlesh","FromCapacity",
            "ToCapacity","FromDiagonal", "ToDiagonal","FromMain","ToMain","FromFront","ToFront","FromYear","ToYear"};
    private static SharedPreferences settings = null;
    public static SharedPreferences.Editor editor = null;
    private Button accept, cancel;
    private File f;
    private Set<String> colorList = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        init();
        f = new File("/data/data/android.malakhov.appmarket/shared_prefs/Filter.xml");
        if (f.exists()) { createSharedPref(); updateUI(); }else createSharedPref();

        smartphone.setOnClickListener(this);
        planshet.setOnClickListener(this);
        watch.setOnClickListener(this);

//Сохраняме все фильтры в SharedPref при нажатии кнопки применить
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear().apply();

                for (int i = 0; i < filtersView.size(); i++){
                    if (!filtersView.get(i).getText().toString().equals(""))
                        editor.putString(filters[i], filtersView.get(i).getText().toString());
                }
                for (int i = 0; i < colorsView.size(); i++){
                    if (colorsView.get(i).isChecked())
                        colorList.add(colors[i]);
                }
                editor.putStringSet("Color",colorList);


                if (lastBtn != null) editor.putString("Type", lastBtn.getText().toString());

                editor.apply();

                if (settings.getAll().size() == 0) {
                    settings = null;
                    filtersView = null;
                }
                finish();
            }
        });

        //Обрабатываем кнопку очистить и выходим из экрана фильтров
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings = null;
                f.delete();
                finish();
            }
        });
    }

    //Инициализация переменных
    public void init(){
        colorsView = new ArrayList<>();
        filtersView = new ArrayList<>();
        for (String s : filters) {
            filtersView.add(findViewById(getResources().getIdentifier(s, "id", this.getPackageName())));
        }
        for (String s : colors)
            colorsView.add(findViewById(getResources().getIdentifier(s, "id", this.getPackageName())));

        smartphone = findViewById(R.id.smartphone);
        planshet = findViewById(R.id.planshet);
        watch = findViewById(R.id.watch);
        accept = findViewById(R.id.accept);
        cancel = findViewById(R.id.cancel);
        smartphone = findViewById(R.id.smartphone);
        planshet = findViewById(R.id.planshet);
        watch = findViewById(R.id.watch);
    }

    //Гетер
    public static SharedPreferences getSettings(){ return settings; }

    //Создаем SharePreferences
    public void createSharedPref(){
        settings = this.getSharedPreferences(PERSISTANT_STORAGE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    //Сохраняме установленные фильтры при перезаходе в меню фильтров
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

        for (int i = 0; i < filtersView.size(); i++)
            filtersView.get(i).setText(settings.getString(filters[i], ""));

        for (String str : settings.getStringSet("Color",colorList))
            for (CheckBox color: colorsView)
                if (color.getText().equals(str))
                    color.setChecked(true);
    }

    @Override
    public void onClick(View v) {

        if (lastBtn != null)
            lastBtn.setTextColor(getResources().getColor(R.color.typeTV));

        ((TextView)v).setTextColor(getResources().getColor(R.color.price));
        lastBtn = (TextView) v;
    }

}