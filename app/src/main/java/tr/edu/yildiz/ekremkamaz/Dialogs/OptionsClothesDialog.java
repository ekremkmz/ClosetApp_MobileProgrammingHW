package tr.edu.yildiz.ekremkamaz.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import tr.edu.yildiz.ekremkamaz.Dialogs.AddClothesDialog;
import tr.edu.yildiz.ekremkamaz.DrawerActivity;
import tr.edu.yildiz.ekremkamaz.R;

public class OptionsClothesDialog extends Dialog implements View.OnClickListener {
    private int position;
    private DrawerActivity a;
    private TextView delete;
    private TextView update;

    public OptionsClothesDialog(Activity a, int position) {
        super(a);
        this.a = (DrawerActivity) a;
        this.position = position;
        setContentView(R.layout.dialog_options_clothes);
        defineVariables();
        defineListeners();
    }

    private void defineListeners() {
        delete.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    private void defineVariables() {
        delete = findViewById(R.id.optionsClothesDialogDelete);
        update = findViewById(R.id.optionsClothesDialogUpdate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.optionsClothesDialogDelete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(a);
                builder.setTitle("Uyarı");
                builder.setMessage("Kıyafet silinecek. Emin misiniz?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", (dialogInterface, i) -> a.deleteClothes(position));
                builder.show();
                dismiss();
            }
            break;
            case R.id.optionsClothesDialogUpdate: {
                a.addClothesDialog = new AddClothesDialog(a, a.getClothes(position), position);
                a.addClothesDialog.show();
                dismiss();
            }
            break;
            default:
                break;
        }
    }
}
