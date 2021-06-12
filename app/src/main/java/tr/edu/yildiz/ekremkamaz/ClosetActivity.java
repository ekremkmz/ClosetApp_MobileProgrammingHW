package tr.edu.yildiz.ekremkamaz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import tr.edu.yildiz.ekremkamaz.data.Clothes;
import tr.edu.yildiz.ekremkamaz.data.Combine;
import tr.edu.yildiz.ekremkamaz.helper.DatabaseHelper;
import tr.edu.yildiz.ekremkamaz.helper.ImageLoader;

public class ClosetActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper DBHelper;
    private RelativeLayout closet;
    private RecyclerView combinesRecyclerView;
    private final int TOP_OF_HEAD = 1;
    private final int FACE = 2;
    private final int TOP = 3;
    private final int LOWER = 4;
    private final int FOOT = 5;
    private Clothes[] clothes;
    private ArrayList<Combine> combineArrayList;
    private CustomAdapter customAdapter;
    private FloatingActionButton shareFAB;
    private boolean select = false;
    private int selectedCombinePosition = -1;
    private boolean selectedExistCombine = false;
    private Combine selectedCombine;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);
        setTitle("Kabin");
        defineVariables();
        if (getIntent().hasExtra("select")) {
            select = true;
            shareFAB.setImageDrawable(getResources().getDrawable(R.drawable.check_icon));
        }
    }

    private void defineVariables() {
        DBHelper = DatabaseHelper.getInstance(ClosetActivity.this);
        combinesRecyclerView = findViewById(R.id.combinesRecyclerView);
        closet = findViewById(R.id.closet);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_closet, closet, false);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(10, 10, 10, 10);
        View child = ((ViewGroup) view).getChildAt(0);
        for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
            ((ViewGroup) child).getChildAt(i).setOnClickListener(ClosetActivity.this);
        }
        closet.addView(view, layoutParams);

        AsyncTask task = new AsyncTask<Object, Object, ArrayList<Combine>>() {
            @Override
            protected ArrayList<Combine> doInBackground(Object[] objects) {
                combineArrayList = DBHelper.getCombines();
                return combineArrayList;
            }

            @Override
            protected void onPostExecute(ArrayList<Combine> o) {
                super.onPostExecute(o);
                customAdapter = new CustomAdapter(combineArrayList);
                combinesRecyclerView.setAdapter(customAdapter);
            }
        }.execute();
        layoutManager = new LinearLayoutManager(ClosetActivity.this, LinearLayoutManager.HORIZONTAL, false);
        combinesRecyclerView.setLayoutManager(layoutManager);
        clothes = new Clothes[5];
        shareFAB = findViewById(R.id.shareFAB);
        shareFAB.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.shareFAB) {
            if (select) {
                if (selectedExistCombine) {
                    Intent _intent = new Intent();
                    _intent.putExtra("combine", selectedCombine);
                    setResult(RESULT_OK, _intent);
                    finish();
                } else {
                    if (!checkCombineComplete()) return;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ClosetActivity.this);
                    builder.setTitle("Uyarı");
                    builder.setMessage("Kayıtlı olmayan kombin önce kaydedilmelidir. Onaylıyor musunuz?");
                    builder.setNegativeButton("Hayır", null);
                    builder.setPositiveButton("Evet", (dialogInterface, i) -> {
                        Combine c = new Combine(-1, clothes[0], clothes[1], clothes[2], clothes[3], clothes[4]);
                        if (DBHelper.addCombine(c)) {
                            Intent _intent = new Intent();
                            _intent.putExtra("combine", c);
                            setResult(RESULT_OK, _intent);
                            Toast.makeText(ClosetActivity.this, "Kombin kaydedildi!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ClosetActivity.this, "Kombin kaydedilirken sorun oluştu", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            } else {
                if (!checkCombineComplete()) return;
                Combine c = new Combine(-1, clothes[0], clothes[1], clothes[2], clothes[3], clothes[4]);
                File file = null;
                try {
                    file = c.toFile(ClosetActivity.this);
                } catch (IOException e) {
                    Toast.makeText(ClosetActivity.this, "Kombin dosyaya yazılırken hata oluştu !", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent _intent = new Intent();
                _intent.setAction(Intent.ACTION_SEND);
                _intent.setType("text/plain");
                _intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                startActivity(Intent.createChooser(_intent, "Kombini paylaş"));
            }

        } else if (view instanceof ImageView) {
            int i = 0;

            switch (view.getId()) {
                case R.id.topOfHeadImageView: {
                    i = TOP_OF_HEAD;
                }
                break;
                case R.id.faceImageView: {
                    i = FACE;
                }
                break;
                case R.id.topImageView: {
                    i = TOP;
                }
                break;
                case R.id.lowerImageView: {
                    i = LOWER;
                }
                break;
                case R.id.footImageView: {
                    i = FOOT;
                }
                break;
                default:
                    break;
            }
            Intent _intent = new Intent(ClosetActivity.this, ListDrawerActivity.class);
            _intent.putExtra("select", true);
            startActivityForResult(_intent, i);
        }

    }

    private boolean checkCombineComplete() {
        for (Clothes c : clothes) {
            if (c == null) {
                Toast.makeText(ClosetActivity.this, "Kombin eksik", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Clothes c = data.getParcelableExtra("selected");
            clothes[requestCode - 1] = c;
            ViewGroup child = (ViewGroup) ((ViewGroup) closet.getChildAt(1)).getChildAt(0);
            ImageLoader imageLoader = new ImageLoader(((ImageView) child.getChildAt(requestCode - 1)), Uri.parse(c.getPhoto()));
            imageLoader.execute();
            selectedExistCombine = false;
            if (selectedCombinePosition != -1) {
                layoutManager.findViewByPosition(selectedCombinePosition).setBackgroundColor(getColor(R.color.appBackgroundColor));
                selectedCombinePosition = -1;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewGroup child = (ViewGroup) ((ViewGroup) closet.getChildAt(1)).getChildAt(0);
        for (int i = 0; i < 5; i++) {
            Drawable d = ((ImageView) child.getChildAt(i)).getDrawable();
            if (d instanceof BitmapDrawable) {
                ((BitmapDrawable) d).getBitmap().recycle();
            }
        }

        for (int i = 0; i < layoutManager.getChildCount() - 1; i++) {
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

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolders> {
        private final int ADD_BUTTON = 1;
        private final int COMBINE_TYPE = 2;

        ArrayList<Combine> combines;

        public CustomAdapter(ArrayList<Combine> combines) {
            this.combines = combines;
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolders onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            if (viewType == COMBINE_TYPE) {
                Context context = parent.getContext();
                LayoutInflater inflater = LayoutInflater.from(context);
                View combine = inflater.inflate(R.layout.item_closet, parent, false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);
                combine.setLayoutParams(layoutParams);
                ViewHolderCabinet viewHolder = new ViewHolderCabinet(combine);
                return viewHolder;
            } else {
                LinearLayout layout = new LinearLayout(parent.getContext());
                FloatingActionButton fab = new FloatingActionButton(layout.getContext());
                LinearLayout.LayoutParams fabParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                fabParams.setMargins(20, 0, 20, 0);
                fabParams.gravity = Gravity.CENTER_VERTICAL;
                fab.setLayoutParams(fabParams);
                Resources r = getResources();
                int[][] state = new int[][]{
                        new int[]{android.R.attr.state_enabled}, // enabled
                        new int[]{-android.R.attr.state_enabled}, // disabled
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_pressed}  // pressed
                };
                int color = r.getColor(R.color.cardBackgroundColor, getTheme());
                fab.setBackgroundTintList(new ColorStateList(state, new int[]{color, color, color, color}));
                fab.setImageDrawable(r.getDrawable(R.drawable.check_icon, getTheme()));
                fab.setImageTintList(new ColorStateList(state, new int[]{Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE}));
                layout.addView(fab);
                layout.setLayoutParams(layoutParams);
                ViewHolderFAB viewHolder2 = new ViewHolderFAB(layout);
                return viewHolder2;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == combines.size() ? ADD_BUTTON : COMBINE_TYPE;
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolders holder, int position) {
            if (combines.size() != position) {
                if (position == selectedCombinePosition) {
                    holder.itemView.setBackgroundColor(getColor(R.color.cardBackgroundColor));
                } else {
                    holder.itemView.setBackgroundColor(getColor(R.color.transparent));
                }
                Combine c = combines.get(position);
                ImageLoader imageLoader1 = new ImageLoader(((ViewHolderCabinet) holder).topOfHead, Uri.parse(c.getTopOfHead().getPhoto()));
                ImageLoader imageLoader2 = new ImageLoader(((ViewHolderCabinet) holder).face, Uri.parse(c.getFace().getPhoto()));
                ImageLoader imageLoader3 = new ImageLoader(((ViewHolderCabinet) holder).top, Uri.parse(c.getTop().getPhoto()));
                ImageLoader imageLoader4 = new ImageLoader(((ViewHolderCabinet) holder).lower, Uri.parse(c.getLower().getPhoto()));
                ImageLoader imageLoader5 = new ImageLoader(((ViewHolderCabinet) holder).foot, Uri.parse(c.getFoot().getPhoto()));
                imageLoader1.execute();
                imageLoader2.execute();
                imageLoader3.execute();
                imageLoader4.execute();
                imageLoader5.execute();
            }
        }

        @Override
        public int getItemCount() {
            return select ? combines.size() : combines.size() + 1;
        }

        private abstract class ViewHolders extends RecyclerView.ViewHolder {
            public ViewHolders(@NonNull @NotNull View itemView) {
                super(itemView);
            }
        }

        private class ViewHolderCabinet extends ViewHolders implements View.OnClickListener, View.OnLongClickListener {
            ImageView topOfHead;
            ImageView face;
            ImageView top;
            ImageView lower;
            ImageView foot;


            public ViewHolderCabinet(@NonNull @NotNull View itemView) {
                super(itemView);
                topOfHead = itemView.findViewById(R.id.topOfHeadImageView);
                face = itemView.findViewById(R.id.faceImageView);
                top = itemView.findViewById(R.id.topImageView);
                lower = itemView.findViewById(R.id.lowerImageView);
                foot = itemView.findViewById(R.id.footImageView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Combine c = combineArrayList.get(getAdapterPosition());
                clothes = new Clothes[]{c.getTopOfHead(), c.getFace(), c.getTop(), c.getLower(), c.getFoot()};
                ((ViewGroup) itemView).getChildAt(0).setOnClickListener(this);
                ViewGroup child = (ViewGroup) ((ViewGroup) closet.getChildAt(1)).getChildAt(0);
                for (int i = 0; i < 5; i++) {
                    ImageLoader imageLoader = new ImageLoader((ImageView) child.getChildAt(i), Uri.parse(clothes[i].getPhoto()));
                    imageLoader.execute();
                }
                selectedExistCombine = true;
                int tmp = selectedCombinePosition;
                selectedCombinePosition = getAdapterPosition();
                selectedCombine = c;
                if (tmp != -1)
                    notifyItemChanged(tmp);
                notifyItemChanged(selectedCombinePosition);
            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClosetActivity.this);
                builder.setTitle("Uyarı");
                builder.setMessage("Kombin silinecek. Emin misiniz?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", (dialogInterface, i) -> {
                    int position = getAdapterPosition();
                    Combine c = combineArrayList.get(position);
                    if (!DBHelper.deleteCombine(c)) return;
                    combineArrayList.remove(c);
                    notifyItemRemoved(position);
                });
                builder.show();
                return true;
            }
        }

        private class ViewHolderFAB extends ViewHolders implements View.OnClickListener {
            FloatingActionButton button;

            public ViewHolderFAB(@NonNull @NotNull View itemView) {
                super(itemView);
                ((ViewGroup) itemView).getChildAt(0).setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (!checkCombineComplete()) return;
                Combine c = new Combine(-1, clothes[0], clothes[1], clothes[2], clothes[3], clothes[4]);
                if (!DBHelper.addCombine(c)) return;
                combineArrayList.add(c);
                notifyItemInserted(combineArrayList.size() - 1);
            }
        }
    }
}