package govan;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Rafael on 20/07/2015.
 */
public class UserError extends DialogFragment {

    String msg;

    public void setMsg(String msg){
        this.msg = msg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });

        Dialog dialog = builder.create();

        // Create the AlertDialog object and return it
        return dialog;
    }
}
