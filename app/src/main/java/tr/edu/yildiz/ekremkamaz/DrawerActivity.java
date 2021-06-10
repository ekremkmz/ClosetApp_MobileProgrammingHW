package tr.edu.yildiz.ekremkamaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import tr.edu.yildiz.ekremkamaz.Dialogs.AddClothesDialog;
import tr.edu.yildiz.ekremkamaz.Dialogs.OptionsClothesDialog;
import tr.edu.yildiz.ekremkamaz.data.Clothes;
import tr.edu.yildiz.ekremkamaz.data.Drawer;
import tr.edu.yildiz.ekremkamaz.helper.DatabaseHelper;
import tr.edu.yildiz.ekremkamaz.helper.ImageLoader;

public class DrawerActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView clothesRecyclerView;
    private FloatingActionButton addClothesFAB;
    private ArrayList<Clothes> clothesList;
    private DatabaseHelper DBHelper;
    public AddClothesDialog addClothesDialog;
    private CustomAdapter customAdapter;
    static final int PICK_IMAGE = 123;
    public Drawer d;
    private boolean select = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        defineVariables();
        defineListeners();
        if (getIntent().hasExtra("select")) {
            select = true;
        }
    }

    private void defineListeners() {
        addClothesFAB.setOnClickListener(this);
    }

    private void defineVariables() {
        DBHelper = DatabaseHelper.getInstance(DrawerActivity.this);
        d = getIntent().getParcelableExtra("drawer");
        setTitle(d.getName());
        addClothesFAB = findViewById(R.id.addClothesFAB);
        clothesRecyclerView = findViewById(R.id.clothesRecyclerView);
        clothesList = DBHelper.getClothesFromDrawer(d.getId());
        customAdapter = new CustomAdapter(clothesList);
        clothesRecyclerView.setAdapter(customAdapter);
        clothesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(@org.jetbrains.annotations.NotNull View view) {
        if (view.getId() == R.id.addClothesFAB) {
            addClothesDialog = new AddClothesDialog(DrawerActivity.this);
            addClothesDialog.show();
        }
    }

    public void addClothes(Clothes c, Uri uri) {
        if (!DBHelper.addClothes(c, uri, d.getId())) return;
        clothesList.add(c);
        customAdapter.notifyItemInserted(clothesList.size() - 1);
    }

    public void updateClothes(Clothes c, int position, Uri uri) {
        if (!DBHelper.updateClothes(c, uri)) return;
        clothesList.set(position, c);
        customAdapter.notifyItemChanged(position);
    }

    public Clothes getClothes(int position) {
        return clothesList.get(position);
    }

    public void deleteClothes(int position) {
        customAdapter.deleteClothes(position);
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<Clothes> clothesList;

        public CustomAdapter(ArrayList<Clothes> clothesList) {
            this.clothesList = clothesList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View clothes = inflater.inflate(R.layout.item_drawer, parent, false);
            return new ViewHolder(clothes);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Clothes c = clothesList.get(position);
            holder.clothesTextView.setText(c.getName());
            ImageLoader imageLoader = new ImageLoader(holder.clothesImageView, Uri.parse(c.getPhoto()));
            imageLoader.execute();
        }

        private void recycle(ImageView view){
            if(view.getDrawable() instanceof BitmapDrawable){
                ((BitmapDrawable) view.getDrawable()).getBitmap().recycle();
            }
        }

        @Override
        public int getItemCount() {
            return clothesList.size();
        }

        @Override
        public void onViewRecycled(@NonNull @NotNull DrawerActivity.CustomAdapter.ViewHolder holder) {
            recycle(holder.clothesImageView);
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull @NotNull ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            recycle(holder.clothesImageView);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView clothesTextView;
            ImageView clothesImageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                clothesTextView = itemView.findViewById(R.id.drawerTextView);
                clothesImageView = itemView.findViewById(R.id.drawerImageView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (select) {
                    Intent _intent = new Intent();
                    _intent.putExtra("selected", clothesList.get(getAdapterPosition()));
                    setResult(RESULT_OK, _intent);
                    finish();
                } else {
                    //TODO: kıyafeti göster
                }
            }

            @Override
            public boolean onLongClick(View view) {
                OptionsClothesDialog options = new OptionsClothesDialog(DrawerActivity.this, getAdapterPosition());
                options.show();
                return true;
            }
        }

        public void deleteClothes(int adapterPosition) {
            DBHelper.deleteClothes(clothesList.get(adapterPosition));
            clothesList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clothesRecyclerView.setAdapter(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri uri = data.getData();
                addClothesDialog.setContent(uri);
            }
        }
    }
}