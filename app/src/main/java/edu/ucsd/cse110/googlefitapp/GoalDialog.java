package edu.ucsd.cse110.googlefitapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.widget.EditText;

public class GoalDialog implements Dialog {

    String title;
    String message;
    String hint;
    String positiveButton;
    String negativeButton;
    int result = -1;
    Context context;
    AlertDialog alertDialog = null;

    public GoalDialog(Context context, int title, int message, int hint, int positiveButton, int negativeButton) {
        Resources r = context.getResources();
        this.title = r.getString(title);
        this.message = r.getString(message);
        this.hint = r.getString(hint);
        this.positiveButton = r.getString(positiveButton);
        this.negativeButton = r.getString(negativeButton);
        this.context = context;
    }

    @Override
    public void build() {
        final EditText setGoal = new EditText(context);
        setGoal.setHint(hint);
        alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setView(setGoal)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            result = Integer.parseInt(setGoal.getText().toString());
                            System.out.println("Mariya Takeuchi result: " + result);
                        } catch (NumberFormatException e) {
                            result = 0;
                            System.out.println("The Pope error");
                        }
                    }
                })
                .setNegativeButton(negativeButton, null)
                .create();
    }

    @Override
    public void show() {
        alertDialog.show();
    }

    @Override
    public int getIntResult() {
        return result;
    }
}
