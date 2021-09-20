package android.malakhov.appmarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CharacteristicsActivity extends AppCompatActivity implements View.OnClickListener {

    private int i = 0;
    private Uri uploadURI;
    private StorageReference storageRef;
    private int RequestCode;
    private List<EditText> edit = new ArrayList<>();
    private ImageView add1, add2, add3;
    private Button addBtn;
    private Spinner type, color;
    private String[] types = { "Смартфон", "Планшет", "Часы"};
    private String[] colors = { "Черный", "Белый", "Серебристый","Красный","Фиолетовый","Розовый","Золотой","Бирюзовый",
            "Желтый","Зеленый","Синий","Коралловый"};


    public void init() {

        color = findViewById(R.id.color);
        type = findViewById(R.id.type);
        edit.add(findViewById(R.id.name));
        edit.add(findViewById(R.id.CPU));
        edit.add(findViewById(R.id.OZU));
        edit.add(findViewById(R.id.flesh));
        edit.add(findViewById(R.id.capacity));
        edit.add(findViewById(R.id.diagonal));
        edit.add(findViewById(R.id.mainCamera));
        edit.add(findViewById(R.id.frontCamera));
        edit.add(findViewById(R.id.year));
        edit.add(findViewById(R.id.price));
        edit.add(findViewById(R.id.amount));

        add1 = findViewById(R.id.add1);
        add2 = findViewById(R.id.add2);
        add3 = findViewById(R.id.add3);

        addBtn = findViewById(R.id.add_btn);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.characteristics_activity);
        init();
        storageRef = FirebaseStorage.getInstance().getReference("ProductImages");

        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color.setAdapter(colorAdapter);
        AdapterView.OnItemSelectedListener colorItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                color.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        color.setOnItemSelectedListener(colorItemSelectedListener);




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                type.setSelection(position);
                if (position == 2){
                    edit.get(6).setVisibility(View.GONE);
                    edit.get(7).setVisibility(View.GONE);
                    edit.get(6).setText(String.valueOf(-1));
                    edit.get(7).setText(String.valueOf(-1));
                }else{
                    edit.get(6).setVisibility(View.VISIBLE);
                    edit.get(7).setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        type.setOnItemSelectedListener(itemSelectedListener);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (EditText et : edit) {
                    if (et.getText().toString().equals("")) {
                        Toast.makeText(CharacteristicsActivity.this, "Вы пропустили поле", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                ImageView[] images = {add1, add2, add3};

                for (int i = 0; i < 3; i++) {
                    if (images[i].getDrawable().getConstantState() == getResources().getDrawable(R.drawable.add_image).getConstantState()) {
                        Toast.makeText(CharacteristicsActivity.this, "Нужно 3 изображения", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                }


                Product product = new Product();

                product.setType(type.getSelectedItem().toString());
                product.setName(edit.get(0).getText().toString());
                product.setCPU(edit.get(1).getText().toString());
                product.setOZU(Integer.parseInt(edit.get(2).getText().toString()));
                product.setFleshMemory(Integer.parseInt(edit.get(3).getText().toString()));
                product.setCapacity(Integer.parseInt(edit.get(4).getText().toString()));
                product.setDiagonal(Float.parseFloat(edit.get(5).getText().toString()));
                product.setMainCamera(Integer.parseInt(edit.get(6).getText().toString()));
                product.setFrontCamera(Integer.parseInt(edit.get(7).getText().toString()));
                product.setDateOfRelease(Integer.parseInt(edit.get(8).getText().toString()));
                product.setPrice(Integer.parseInt(edit.get(9).getText().toString()));
                product.setAmount(Integer.parseInt(edit.get(10).getText().toString()));
                product.setColor(color.getSelectedItem().toString());


                loadPhotoToDb(images, product);
                Toast.makeText(CharacteristicsActivity.this, "Товар Загружается", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        add1.setOnClickListener(this);
        add2.setOnClickListener(this);
        add3.setOnClickListener(this);
    }


    public void loadPhotoToDb (ImageView[]images, Product product){


        final StorageReference idRef = storageRef.child(product.getId());
        final StorageReference ref = idRef.child(String.valueOf(i));

        Bitmap bitmap = ((BitmapDrawable) images[i].getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        UploadTask up = ref.putBytes(byteArray);


        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {

                uploadURI = task.getResult();
                product.getPhotoURI().add(uploadURI.toString());
                if (i == 2) {
                    ProductLab.get().addProductToDB(product);
                } else {
                    i++;
                    loadPhotoToDb(images, product);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);


        if (((ImageView) v) == add1)
            RequestCode = 1;
        if (((ImageView) v) == add2)
            RequestCode = 2;
        if (((ImageView) v) == add3)
            RequestCode = 3;

        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RequestCode);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED)
            return;

        if (RequestCode == 1)
                add1.setImageURI(data.getData());
            if (RequestCode == 2)
                add2.setImageURI(data.getData());
            if (RequestCode == 3)
                add3.setImageURI(data.getData());
    }
}





