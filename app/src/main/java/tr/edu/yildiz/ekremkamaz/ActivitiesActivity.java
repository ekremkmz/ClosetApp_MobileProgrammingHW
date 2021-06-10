package tr.edu.yildiz.ekremkamaz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import tr.edu.yildiz.ekremkamaz.Dialogs.AddActivitiesDialog;

public class ActivitiesActivity extends AppCompatActivity implements View.OnClickListener {

    private final int SELECT_LOCATION = 200;
    private final int SELECT_COMBINE = 100;
    private AddActivitiesDialog d;
    private FloatingActionButton addActivitiesFAB;
    private RecyclerView activitiesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        setTitle("Etkinlikler");
        defineVariables();
        defineListeners();
    }

    private void defineListeners() {
        addActivitiesFAB.setOnClickListener(this);
    }

    private void defineVariables() {
        addActivitiesFAB = findViewById(R.id.addActivitiesFAB);
        activitiesRecyclerView = findViewById(R.id.activitiesRecyclerView);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof FloatingActionButton) {
            d = new AddActivitiesDialog(ActivitiesActivity.this);
            d.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_COMBINE: {
                    d.setCombine(data.getParcelableExtra("combine"));
                }
                break;
                case SELECT_LOCATION: {
                    d.setLocation(data.getParcelableExtra("location"));
                }
                break;
                default:
                    break;
            }
        }
    }

    public void addActivites(ActivitiesActivity a) {

    }

}