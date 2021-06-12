package tr.edu.yildiz.ekremkamaz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

import tr.edu.yildiz.ekremkamaz.data.Clothes;
import tr.edu.yildiz.ekremkamaz.helper.ImageLoader;

public class ViewClothesActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameTextView;
    private TextView typeTextView;
    private TextView colorTextView;
    private TextView patternTextView;
    private TextView dateTextView;
    private TextView priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineVariables();
    }

    private void defineVariables() {
        setContentView(R.layout.activity_view_clothes);
        imageView = findViewById(R.id.viewClothesImageView);
        nameTextView = findViewById(R.id.viewClothesNameTextView);
        typeTextView = findViewById(R.id.viewClothesTypeTextView);
        colorTextView = findViewById(R.id.viewClothesColorTextView);
        patternTextView = findViewById(R.id.viewClothesPatternTextView);
        dateTextView = findViewById(R.id.viewClothesDateTextView);
        priceTextView = findViewById(R.id.viewClothesPriceTextView);
        Clothes c = getIntent().getParcelableExtra("clothes");
        ImageLoader imageLoader = new ImageLoader(imageView, Uri.parse(c.getPhoto()));
        imageLoader.execute();
        nameTextView.setText("İsim: " + c.getName());
        typeTextView.setText("Tür: " + c.getType());
        colorTextView.setText("Renk: " + c.getColor());
        patternTextView.setText("Desen: " + c.getPattern());
        dateTextView.setText("Tarih: " + c.getDate());
        String symbol = NumberFormat.getCurrencyInstance().getCurrency().getSymbol();
        priceTextView.setText("Fiyat: " + symbol + (double) c.getPrice() / 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageView.getDrawable() instanceof BitmapDrawable) {
            ((BitmapDrawable) imageView.getDrawable()).getBitmap().recycle();
        }
    }
}