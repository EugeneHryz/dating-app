package com.example.datingapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.datingapp.R;

public class AlertDialogFragment extends DialogFragment {

    public static final String TAG = AlertDialogFragment.class.getName();

    public static final String DIALOG_MESSAGE_KEY = "dialog_message_key";
    public static final String DIALOG_TITLE_KEY = "dialog_title_key";
    public static final String DIALOG_HAS_NEGATIVE_BUTTON = "dialog_has_negative_button";

    private final DialogResultListener listener;

    public AlertDialogFragment(DialogResultListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String dialogMessage = getString(R.string.dialog_generic_error_msg);
        String dialogTitle = getString(R.string.dialog_title_alert);
        boolean hasNegativeButton = false;
        Bundle args = getArguments();
        if (args != null) {
            dialogMessage = args.getString(DIALOG_MESSAGE_KEY);
            dialogTitle = args.getString(DIALOG_TITLE_KEY);
            hasNegativeButton = args.getBoolean(DIALOG_HAS_NEGATIVE_BUTTON);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext())
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setPositiveButton(R.string.dialog_ok_button, (dialog, which) -> {
                    listener.onOk();
                });
        if (hasNegativeButton) {
            dialogBuilder.setNegativeButton(R.string.dialog_no_button, (dialog, which) -> {
                listener.onCancel();
            });
        }

        return dialogBuilder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        listener.onDismiss();
        super.onDismiss(dialog);
    }
}
