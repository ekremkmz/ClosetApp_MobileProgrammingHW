package tr.edu.yildiz.ekremkamaz.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

import tr.edu.yildiz.ekremkamaz.ActivitiesActivity;
import tr.edu.yildiz.ekremkamaz.ClosetActivity;
import tr.edu.yildiz.ekremkamaz.MapsActivity;
import tr.edu.yildiz.ekremkamaz.R;
import tr.edu.yildiz.ekremkamaz.data.Activities;
import tr.edu.yildiz.ekremkamaz.data.Combine;
import tr.edu.yildiz.ekremkamaz.helper.DatabaseHelper;
import tr.edu.yildiz.ekremkamaz.helper.ImageLoader;

public class AddActivitiesDialog extends Dialog implements View.OnClickListener {

    private final int SELECT_COMBINE = 100;
    private final int SELECT_LOCATION = 200;
    ActivitiesActivity a;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText nameEditText;
    private EditText typeEditText;
    private TextView dateTextView;
    private TextView combineTextView;
    private TextView locationTextView;
    private LinearLayout selectedCombineLayout;
    private Button cancelButton;
    private Button saveButton;
    private Combine selectedCombine;
    private DatabaseHelper DBHelper;
    private LatLng location;

    public AddActivitiesDialog(@NonNull Context context) {
        super(context);
        this.a = (ActivitiesActivity) context;
        setContentView(R.layout.dialog_add_activites);
        defineVariables();
        defineListeners();
    }

    private void defineListeners() {
        dateTextView.setOnClickListener(this);
        combineTextView.setOnClickListener(this);
        locationTextView.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private void defineVariables() {
        nameEditText = findViewById(R.id.addActivitiesDialogNameEditText);
        typeEditText = findViewById(R.id.addActivitiesDialogTypeEditText);
        dateTextView = findViewById(R.id.addActivitiesDialogDateTextView);
        combineTextView = findViewById(R.id.addActivitiesDialogCombineTextView);
        locationTextView = findViewById(R.id.addActivitiesDialogLocationTextView);
        selectedCombineLayout = findViewById(R.id.addActivitiesDialogSelectedCombine);
        cancelButton = findViewById(R.id.addActivitiesDialogCancelButton);
        saveButton = findViewById(R.id.addActivitiesDialogSaveButton);
        mDateSetListener = (datePicker, year, month, day) -> {
            String date = day + "/" + (month + 1) + "/" + year;
            dateTextView.setText(date);
        };
        DBHelper = DatabaseHelper.getInstance(a);
    }

    public void setCombine(Combine c) {
        combineTextView.setText("Kombin seçildi !");
        combineTextView.setError(null);
        selectedCombine = c;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_closet, selectedCombineLayout, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        selectedCombineLayout.setLayoutParams(layoutParams);
        selectedCombineLayout.addView(view, selectedCombineLayout.getWidth(), selectedCombineLayout.getWidth());

        ImageLoader imageLoader1 = new ImageLoader(view.findViewById(R.id.topOfHeadImageView), Uri.parse(selectedCombine.getTopOfHead().getPhoto()));
        ImageLoader imageLoader2 = new ImageLoader(view.findViewById(R.id.faceImageView), Uri.parse(selectedCombine.getTopOfHead().getPhoto()));
        ImageLoader imageLoader3 = new ImageLoader(view.findViewById(R.id.topImageView), Uri.parse(selectedCombine.getTopOfHead().getPhoto()));
        ImageLoader imageLoader4 = new ImageLoader(view.findViewById(R.id.lowerImageView), Uri.parse(selectedCombine.getTopOfHead().getPhoto()));
        ImageLoader imageLoader5 = new ImageLoader(view.findViewById(R.id.footImageView), Uri.parse(selectedCombine.getTopOfHead().getPhoto()));
        imageLoader1.execute();
        imageLoader2.execute();
        imageLoader3.execute();
        imageLoader4.execute();
        imageLoader5.execute();
    }


    private boolean validateBoxes() {
        if (nameEditText.getText().toString().equals("")) {
            nameEditText.setError("İsim boş olamaz!");
            return false;
        }
        if (typeEditText.getText().toString().equals("")) {
            typeEditText.setError("Tür boş olamaz!");
            return false;
        }
        if (combineTextView.getText().toString().equals("Kombin seçiniz...")) {
            combineTextView.setError("Kombin eksik!");
            return false;
        }
        if (locationTextView.getText().toString().equals("Konum seçiniz...")) {
            locationTextView.setError("Konum eksik");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addActivitiesDialogDateTextView: {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int mounth = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mDateSetListener, year, mounth, day);
                datePickerDialog.show();
            }
            break;
            case R.id.addActivitiesDialogCombineTextView: {
                Intent _intent = new Intent(a, ClosetActivity.class);
                _intent.putExtra("select", true);
                a.startActivityForResult(_intent, SELECT_COMBINE);
            }
            break;
            case R.id.addActivitiesDialogLocationTextView:
                dAC:
                {
                    Intent _intent = new Intent(a, MapsActivity.class);
                    if (location != null)
                        _intent.putExtra("location", location);
                    a.startActivityForResult(_intent, SELECT_LOCATION);
                }
                break;
            case R.id.addActivitiesDialogSaveButton: {
                if (validateBoxes()) {
                    Activities activities = new Activities(-1, nameEditText.getText().toString(), typeEditText.getText().toString(), dateTextView.getText().toString(), location, selectedCombine);
                    if (!DBHelper.addActivities(activities))
                        return;
                    a.addActivites(a);
                }
            }
            break;
            case R.id.addActivitiesDialogCancelButton: {
                dismiss();
            }
            break;
            default:
                break;
        }
    }

    public void setLocation(LatLng location) {
        this.location = location;
        locationTextView.setText("Latitude: " + location.latitude + ", Longitude: " + location.longitude);
        locationTextView.setTextSize(16);
        locationTextView.setError(null);
    }
}
