package tr.edu.yildiz.ekremkamaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView drawerButton;
    private CardView closetButton;
    private CardView activitiesButton;
    private final int STORAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        defineVariables();
        defineListeners();
    }

    private void defineListeners() {
        drawerButton.setOnClickListener(this);
        closetButton.setOnClickListener(this);
        activitiesButton.setOnClickListener(this);
    }

    private void defineVariables() {
        drawerButton = findViewById(R.id.drawerButton);
        closetButton = findViewById(R.id.closetButton);
        activitiesButton = findViewById(R.id.activitiesButton);
    }

    public boolean isStoragePermissionGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
    }

    @Override
    public void onClick(View view) {
        if (!isStoragePermissionGranted()) {
            requestStoragePermission();
            Toast.makeText(this, "Depolama izni gerekiyor!", Toast.LENGTH_LONG);
            return;
        }
        switch (view.getId()) {
            case R.id.drawerButton: {
                Intent _intent = new Intent(MainActivity.this, ListDrawerActivity.class);
                startActivity(_intent);
            }
            break;
            case R.id.closetButton: {
                Intent _intent = new Intent(MainActivity.this, ClosetActivity.class);
                startActivity(_intent);
            }
            break;
            case R.id.activitiesButton: {
                Intent _intent = new Intent(MainActivity.this, ActivitiesActivity.class);
                startActivity(_intent);
            }
            break;
            default:
                break;
        }
    }
}