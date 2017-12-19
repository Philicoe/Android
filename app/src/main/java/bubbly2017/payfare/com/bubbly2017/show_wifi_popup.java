package bubbly2017.payfare.com.bubbly2017;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;
import bubbly2017.payfare.com.bubbly2017.NsdChatActivity;
public class show_wifi_popup extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final Context context = getActivity();
        final CharSequence text = "Opening Wifi page now..";
        final int duration = Toast.LENGTH_LONG;


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.appwifiopenquestion)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();


                        //Intent intent = new Intent(context, NsdChatActivity.class);
                        Intent intent = new Intent(context, ServerSetup.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();

    }
}
