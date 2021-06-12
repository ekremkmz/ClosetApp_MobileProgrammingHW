package tr.edu.yildiz.ekremkamaz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import tr.edu.yildiz.ekremkamaz.Dialogs.AddDrawerDialog;
import tr.edu.yildiz.ekremkamaz.data.Clothes;
import tr.edu.yildiz.ekremkamaz.data.Drawer;
import tr.edu.yildiz.ekremkamaz.helper.DatabaseHelper;

public class ListDrawerActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView drawerRecyclerView;
    private FloatingActionButton addDrawerFAB;
    private ArrayList<Drawer> drawerList;
    private DatabaseHelper DBHelper;
    private CustomAdapter customAdapter;
    private boolean select = false;
    private final int SELECT_CLOTHES = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_drawer);
        setTitle("Çekmecem");
        defineVariables();
        defineListeners();
        if (getIntent().hasExtra("select")) {
            select = true;
        }
    }

    private void defineListeners() {
        addDrawerFAB.setOnClickListener(this);
    }

    private void defineVariables() {
        DBHelper = DatabaseHelper.getInstance(getApplicationContext());
        addDrawerFAB = findViewById(R.id.addDrawerFAB);
        drawerRecyclerView = findViewById(R.id.drawersRecyclerView);
        drawerList = DBHelper.getDrawers();
        customAdapter = new CustomAdapter(drawerList);
        drawerRecyclerView.setAdapter(customAdapter);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addDrawerFAB) {
            AddDrawerDialog addDrawerDialog = new AddDrawerDialog(ListDrawerActivity.this);
            addDrawerDialog.show();
        }
    }

    public void addDrawer(Drawer d) {
        if (!DBHelper.addDrawer(d)) return;
        drawerList.add(d);
        customAdapter.notifyItemInserted(drawerList.size() - 1);
        Toast.makeText(this, "Çekmece eklendi!", Toast.LENGTH_SHORT).show();
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        ArrayList<Drawer> drawerList;

        public CustomAdapter(ArrayList<Drawer> drawerList) {
            this.drawerList = drawerList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View drawer = inflater.inflate(R.layout.item_drawer, parent, false);
            ViewHolder viewHolder = new ViewHolder(drawer);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.drawerTextView.setText(drawerList.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return drawerList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView drawerTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                drawerTextView = itemView.findViewById(R.id.drawerTextView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (select) {
                    Intent _intent = new Intent(ListDrawerActivity.this, DrawerActivity.class);
                    _intent.putExtra("drawer", drawerList.get(getAdapterPosition()));
                    _intent.putExtra("select", true);
                    startActivityForResult(_intent, SELECT_CLOTHES);
                } else {
                    Intent _intent = new Intent(ListDrawerActivity.this, DrawerActivity.class);
                    _intent.putExtra("drawer", drawerList.get(getAdapterPosition()));
                    startActivity(_intent);
                }
            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListDrawerActivity.this);
                builder.setTitle("Uyarı");
                builder.setMessage("Çekmece silinecek. Emin misiniz?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", (dialogInterface, i) -> {
                    deleteDrawer(getAdapterPosition());
                    Toast.makeText(ListDrawerActivity.this, "Çekmece silindi!", Toast.LENGTH_SHORT).show();
                });
                builder.show();
                return true;
            }
        }

        private void deleteDrawer(int adapterPosition) {
            DBHelper.deleteDrawer(drawerList.get(adapterPosition));
            drawerList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_CLOTHES) {
            Clothes c = data.getParcelableExtra("selected");
            Intent _intent = new Intent();
            _intent.putExtra("selected", c);
            setResult(RESULT_OK, _intent);
            finish();
        }
    }
}