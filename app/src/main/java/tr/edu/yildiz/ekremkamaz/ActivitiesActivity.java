package tr.edu.yildiz.ekremkamaz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;

import tr.edu.yildiz.ekremkamaz.Dialogs.AddActivitiesDialog;
import tr.edu.yildiz.ekremkamaz.Dialogs.AddClothesDialog;
import tr.edu.yildiz.ekremkamaz.Dialogs.OptionsDialog;
import tr.edu.yildiz.ekremkamaz.data.Activities;
import tr.edu.yildiz.ekremkamaz.data.Combine;
import tr.edu.yildiz.ekremkamaz.helper.DatabaseHelper;
import tr.edu.yildiz.ekremkamaz.helper.ImageLoader;

public class ActivitiesActivity extends AppCompatActivity implements View.OnClickListener {

    private final int SELECT_LOCATION = 200;
    private final int SELECT_COMBINE = 100;
    private AddActivitiesDialog d;
    private FloatingActionButton addActivitiesFAB;
    private RecyclerView activitiesRecyclerView;
    private CustomAdapter customAdapter;
    private ArrayList<Activities> activitiesArrayList;
    private DatabaseHelper DBHelper;
    private LinearLayoutManager layoutManager;

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
        DBHelper = DatabaseHelper.getInstance(this);
        AsyncTask<Object, Object, ArrayList<Activities>> task = new AsyncTask<Object, Object, ArrayList<Activities>>() {
            @Override
            protected ArrayList<Activities> doInBackground(Object... objects) {
                activitiesArrayList = DBHelper.getActivities();
                return activitiesArrayList;
            }

            @Override
            protected void onPostExecute(ArrayList<Activities> activitiesArrayList) {
                super.onPostExecute(activitiesArrayList);
                customAdapter = new CustomAdapter(activitiesArrayList);
                activitiesRecyclerView.setAdapter(customAdapter);
            }
        }.execute();
        layoutManager = new LinearLayoutManager(ActivitiesActivity.this);
        activitiesRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof FloatingActionButton) {
            d = new AddActivitiesDialog(ActivitiesActivity.this);
            d.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activitiesRecyclerView.setAdapter(null);

        for (int i = 0; i < layoutManager.getChildCount(); i++) {
            recycle(layoutManager.getChildAt(i).findViewById(R.id.topOfHeadImageView));
            recycle(layoutManager.getChildAt(i).findViewById(R.id.faceImageView));
            recycle(layoutManager.getChildAt(i).findViewById(R.id.topImageView));
            recycle(layoutManager.getChildAt(i).findViewById(R.id.lowerImageView));
            recycle(layoutManager.getChildAt(i).findViewById(R.id.footImageView));
        }
    }

    private void recycle(ImageView view) {
        if (view.getDrawable() instanceof BitmapDrawable) {
            ((BitmapDrawable) view.getDrawable()).getBitmap().recycle();
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
                    d.setLocation(data.getParcelableExtra("latlng"));
                }
                break;
                default:
                    break;
            }
        }
    }

    public void addActivites(Activities a) {
        activitiesArrayList.add(a);
        customAdapter.notifyItemInserted(activitiesArrayList.size() - 1);
        Toast.makeText(this, "Etkinlik eklendi!", Toast.LENGTH_SHORT).show();
    }

    public void updateActivites(Activities activities, int position) {
        activitiesArrayList.set(position, activities);
        customAdapter.notifyItemChanged(position);
        Toast.makeText(this, "Etkinlik güncellendi!", Toast.LENGTH_SHORT).show();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<Activities> activitiesArrayList;

        public CustomAdapter(ArrayList<Activities> activitiesArrayList) {
            this.activitiesArrayList = activitiesArrayList;
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_activities, parent, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
            Activities a = activitiesArrayList.get(position);
            Combine c = a.getCombine();
            ImageLoader imageLoader1 = new ImageLoader(holder.itemActivitiesCombineLayout.findViewById(R.id.topOfHeadImageView), Uri.parse(c.getTopOfHead().getPhoto()));
            ImageLoader imageLoader2 = new ImageLoader(holder.itemActivitiesCombineLayout.findViewById(R.id.faceImageView), Uri.parse(c.getFace().getPhoto()));
            ImageLoader imageLoader3 = new ImageLoader(holder.itemActivitiesCombineLayout.findViewById(R.id.topImageView), Uri.parse(c.getTop().getPhoto()));
            ImageLoader imageLoader4 = new ImageLoader(holder.itemActivitiesCombineLayout.findViewById(R.id.lowerImageView), Uri.parse(c.getLower().getPhoto()));
            ImageLoader imageLoader5 = new ImageLoader(holder.itemActivitiesCombineLayout.findViewById(R.id.footImageView), Uri.parse(c.getFoot().getPhoto()));
            imageLoader1.execute();
            imageLoader2.execute();
            imageLoader3.execute();
            imageLoader4.execute();
            imageLoader5.execute();
            holder.itemActivitiesNameTextView.setText(a.getName());
            holder.itemActivitiesTypeTextView.setText(a.getType());
            holder.itemActivitiesDateTextView.setText(a.getDate());
            int price = c.getTopOfHead().getPrice() + c.getFace().getPrice() + c.getTop().getPrice() + c.getLower().getPrice() + c.getFoot().getPrice();
            String symbol = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
            holder.itemActivitiesPriceTextView.setText(symbol + String.valueOf((double) price / 100));
        }

        @Override
        public int getItemCount() {
            return activitiesArrayList.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            LinearLayout itemActivitiesCombineLayout;
            TextView itemActivitiesNameTextView;
            TextView itemActivitiesTypeTextView;
            TextView itemActivitiesDateTextView;
            TextView itemActivitiesPriceTextView;
            ImageView itemActivitiesMapImageView;
            CardView itemActivitiesCardView;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                itemActivitiesCombineLayout = itemView.findViewById(R.id.itemActivitiesCombineLayout);
                itemActivitiesNameTextView = itemView.findViewById(R.id.itemActivitiesNameTextView);
                itemActivitiesTypeTextView = itemView.findViewById(R.id.itemActivitiesTypeTextView);
                itemActivitiesDateTextView = itemView.findViewById(R.id.itemActivitiesDateTextView);
                itemActivitiesMapImageView = itemView.findViewById(R.id.itemActivitiesMapImageView);
                itemActivitiesPriceTextView = itemView.findViewById(R.id.itemActivitiesPriceTextView);
                itemActivitiesCardView = itemView.findViewById(R.id.itemActivitiesCardView);
                itemActivitiesMapImageView.setOnClickListener(this);
                itemActivitiesCardView.setOnLongClickListener(this);
                LayoutInflater inflater = LayoutInflater.from(itemActivitiesCombineLayout.getContext());
                View combine = inflater.inflate(R.layout.item_closet, itemActivitiesCombineLayout, false);
                System.out.println(itemActivitiesDateTextView.getWidth());
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                combine.setLayoutParams(layoutParams);
                itemActivitiesCombineLayout.addView(combine);
            }

            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.itemActivitiesMapImageView) {
                    Intent _intent = new Intent(ActivitiesActivity.this, MapsActivity.class);
                    _intent.putExtra("view", true);
                    LatLng latlng = activitiesArrayList.get(getAdapterPosition()).getLocation();
                    _intent.putExtra("latlng", latlng);
                    startActivity(_intent);
                }
            }

            @Override
            public boolean onLongClick(View view) {
                int position = getAdapterPosition();
                OptionsDialog dialog = new OptionsDialog(ActivitiesActivity.this) {
                    @Override
                    public void deleteAction() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitiesActivity.this);
                        builder.setTitle("Uyarı");
                        builder.setMessage("Etkinlik silinecek. Emin misiniz?");
                        builder.setNegativeButton("Hayır", null);
                        builder.setPositiveButton("Evet", (dialogInterface, i) -> {
                            DBHelper.deleteActivities(activitiesArrayList.get(position));
                            activitiesArrayList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(ActivitiesActivity.this, "Aktivite silindi!", Toast.LENGTH_SHORT).show();
                        });
                        builder.show();
                        dismiss();
                    }

                    @Override
                    public void updateAction() {
                        d = new AddActivitiesDialog(ActivitiesActivity.this, activitiesArrayList.get(position), position);
                        d.show();
                        dismiss();
                    }
                };

                dialog.show();

                return true;
            }
        }
    }

}