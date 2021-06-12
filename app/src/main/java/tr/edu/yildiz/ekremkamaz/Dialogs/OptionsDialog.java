package tr.edu.yildiz.ekremkamaz.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import tr.edu.yildiz.ekremkamaz.Dialogs.AddClothesDialog;
import tr.edu.yildiz.ekremkamaz.DrawerActivity;
import tr.edu.yildiz.ekremkamaz.R;

public abstract class OptionsDialog extends Dialog implements View.OnClickListener {
    private Activity a;
    private TextView delete;
    private TextView update;

    public OptionsDialog(Activity a) {
        super(a);
        this.a = a;
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
    public void onBackPressed() {
        dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.optionsClothesDialogDelete: {
                deleteAction();
            }
            break;
            case R.id.optionsClothesDialogUpdate: {
                updateAction();
            }
            break;
            default:
                break;
        }
    }

    public abstract void deleteAction();

    public abstract void updateAction();
}
