package tr.edu.yildiz.ekremkamaz.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tr.edu.yildiz.ekremkamaz.ListDrawerActivity;
import tr.edu.yildiz.ekremkamaz.R;
import tr.edu.yildiz.ekremkamaz.data.Drawer;

public class AddDrawerDialog extends Dialog implements View.OnClickListener {
    private ListDrawerActivity a;
    private EditText editText;
    private Button cancelButton;
    private Button saveButton;

    public AddDrawerDialog(Activity a) {
        super(a);
        this.a = (ListDrawerActivity) a;
        setContentView(R.layout.dialog_add_drawer);
        defineVariables();
        defineListeners();
    }

    private void defineListeners() {
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private void defineVariables() {
        editText = findViewById(R.id.addDrawerDialogEditText);
        cancelButton = findViewById(R.id.addDrawerDialogCancelButton);
        saveButton = findViewById(R.id.addDrawerDialogSaveButton);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    private boolean validateBox(){
        if(editText.getText().toString().equals("")){
            editText.setError("Çekmece ismi boş olamaz!");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addDrawerDialogCancelButton: {
                dismiss();
            }
            break;
            case R.id.addDrawerDialogSaveButton: {
                if(validateBox()){
                    a.addDrawer(new Drawer(-1, editText.getText().toString()));
                    dismiss();
                }
            }
            break;
            default:
                break;
        }
    }
}
