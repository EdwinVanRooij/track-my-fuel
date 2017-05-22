package io.github.edwinvanrooij.trackmyfuel;

import android.app.AlertDialog;
import android.content.Context;

import io.github.edwinvanrooij.trackmyfuel.interfaces.ReturnBoolean;

/**
 * Author: Edwin
 * Date: 11/20/2015.
 */
public class ConfirmDialog {
    //region Fields
    private Context c;
    //endregion

    //region Properties
    public ReturnBoolean delegate = null;
    //endregion

    //region Constructors
    public ConfirmDialog(Context c, ReturnBoolean bool) {
        this.c = c;
        this.delegate = bool;
    }
    //endregion

    //region Methods
    public void show(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(c.getResources().getString(R.string.positive),
                (arg0, arg1) -> delegate.processFinish(true));

        alertDialogBuilder.setNegativeButton(c.getResources().getString(R.string.negative),
                (arg0, arg1) -> delegate.processFinish(false));

        alertDialogBuilder.create().show();
    }
    //endregion
}
