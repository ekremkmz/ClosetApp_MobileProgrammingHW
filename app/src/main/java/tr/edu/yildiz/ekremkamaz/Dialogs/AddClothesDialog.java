package tr.edu.yildiz.ekremkamaz.Dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;

import tr.edu.yildiz.ekremkamaz.DrawerActivity;
import tr.edu.yildiz.ekremkamaz.R;
import tr.edu.yildiz.ekremkamaz.data.Clothes;
import tr.edu.yildiz.ekremkamaz.helper.ImageLoader;

public class AddClothesDialog extends Dialog implements View.OnClickListener {
    private DrawerActivity a;
    private EditText nameEditText;
    private EditText typeEditText;
    private EditText colorEditText;
    private EditText patternEditText;
    private EditText priceEditText;
    private TextView dateTextView;
    private TextView imageTextView;
    private ImageView imageView;
    private Button cancelButton;
    private Button saveButton;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Clothes c;
    private boolean edit = false;
    private int position;
    private final int PICK_IMAGE = 123;
    private Uri uri;
    private boolean selected=false;

    public void setContent(Uri uri) {
        this.uri = uri;
        ImageLoader imageLoader = new ImageLoader(imageView, uri);
        imageLoader.execute();
        selected=true;
    }

    public AddClothesDialog(@NonNull Activity a) {
        super(a);
        this.a = (DrawerActivity) a;
        setContentView(R.layout.dialog_add_clothes);
        defineVariables();
        defineListeners();
    }

    public AddClothesDialog(@NonNull Activity a, Clothes c, int position) {
        super(a);
        this.a = (DrawerActivity) a;
        this.c = c;
        edit = true;
        this.position = position;
        setContentView(R.layout.dialog_add_clothes);
        defineVariables();
        defineListeners();
        nameEditText.setText(c.getName());
        typeEditText.setText(c.getType());
        colorEditText.setText(c.getColor());
        patternEditText.setText(c.getPattern());
        priceEditText.setText(String.valueOf(c.getPrice()));
        dateTextView.setText(c.getDate());
        setContent(Uri.parse(c.getPhoto()));
    }

    private void defineListeners() {
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        dateTextView.setOnClickListener(this);
        imageTextView.setOnClickListener(this);
        mDateSetListener = (datePicker, year, month, day) -> {
            String date = day + "/" + (month + 1) + "/" + year;
            dateTextView.setText(date);
        };
    }

    private void defineVariables() {
        nameEditText = findViewById(R.id.addClothesDialogNameEditText);
        typeEditText = findViewById(R.id.addClothesDialogTypeEditText);
        colorEditText = findViewById(R.id.addClothesDialogColorEditText);
        patternEditText = findViewById(R.id.addClothesDialogPatternEditText);
        priceEditText = findViewById(R.id.addClothesDialogPriceEditText);
        dateTextView = findViewById(R.id.addClothesDialogDateTextView);
        imageTextView = findViewById(R.id.addClothesDialogImageTextView);
        imageView = findViewById(R.id.addClothesDialogImage);
        cancelButton = findViewById(R.id.addClothesDialogCancelButton);
        saveButton = findViewById(R.id.addClothesDialogSaveButton);
        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (s.isEmpty()) return;
                priceEditText.removeTextChangedListener(this);
                String symbol = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
                String cleanString = s.replaceAll("[" + symbol + ",.]", "");
                BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
                String formatted = NumberFormat.getCurrencyInstance().format(parsed);
                priceEditText.setText(formatted);
                priceEditText.setSelection(formatted.length());
                priceEditText.addTextChangedListener(this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addClothesDialogCancelButton: {
                dismiss();
            }
            break;
            case R.id.addClothesDialogSaveButton: {
                if (validateBox()) {
                    String name = nameEditText.getText().toString();
                    String type = typeEditText.getText().toString();
                    String color = colorEditText.getText().toString();
                    String pattern = patternEditText.getText().toString();
                    String priceText = priceEditText.getText().toString();
                    String symbol = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
                    String cleanString = priceText.replaceAll("[" + symbol + ",.]", "");
                    int price = Integer.parseInt(cleanString);
                    String date = dateTextView.getText().toString();
                    Clothes clothes = new Clothes(edit ? c.getId() : -1, name, type, color, pattern, date, price, edit ? c.getPhoto() : "", a.d.getId());
                    if (edit) {
                        a.updateClothes(clothes, position, uri);
                    } else {
                        a.addClothes(clothes, uri);
                    }
                    dismiss();
                }
            }
            break;
            case R.id.addClothesDialogDateTextView: {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int mounth = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mDateSetListener, year, mounth, day);
                datePickerDialog.show();
            }
            break;
            case R.id.addClothesDialogImageTextView: {
                Intent _intent = new Intent(Intent.ACTION_PICK);
                _intent.setType("image/*");
                a.startActivityForResult(_intent, PICK_IMAGE);
            }
            break;
            default:
                break;
        }
    }

    private boolean validateBox() {
        if (nameEditText.getText().toString().equals("")) {
            nameEditText.setError("İsim boş olamaz!");
            return false;
        }
        if (typeEditText.getText().toString().equals("")) {
            typeEditText.setError("Tür boş olamaz!");
            return false;
        }
        if (colorEditText.getText().toString().equals("")) {
            colorEditText.setError("Renk boş olamaz!");
            return false;
        }
        if (patternEditText.getText().toString().equals("")) {
            patternEditText.setError("Desen boş olamaz!");
            return false;
        }
        if (priceEditText.getText().toString().equals("")) {
            priceEditText.setError("Fiyat boş olamaz!");
            return false;
        }
        if (imageTextView.getText().toString().equals("")) {
            Toast.makeText(a, "Bir fotoğraf ekleyin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(selected)((BitmapDrawable) imageView.getDrawable()).getBitmap().recycle();
    }
}
