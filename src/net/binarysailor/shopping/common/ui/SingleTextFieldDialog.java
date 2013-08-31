package net.binarysailor.shopping.common.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class SingleTextFieldDialog {
	private Context context;
	private SingleTextFieldDialogOptions options;
	private AlertDialog dialog;
	private EditText textField;
	private TextNotEmptyWatcher textWatcher;

	public SingleTextFieldDialog(Context context, SingleTextFieldDialogOptions options) {
		this.context = context;
		this.options = options;
	}

	public void open() {
		createTextField();
		buildDialogWithTextField();
		initializeDialog();
	}

	private void createTextField() {
		textField = new EditText(context);
		textField.setId(android.R.id.text1);
		textField.setHint(options.getTextFieldHint());
		textWatcher = new TextNotEmptyWatcher();
		textField.addTextChangedListener(textWatcher);
	}

	private void buildDialogWithTextField() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle(options.getDialogTitle()).setView(textField).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				options.onOK(textField.getText().toString());
			}
		});
		dialog = dialogBuilder.create();
		dialog.show();
	}

	private void initializeDialog() {
		textWatcher.setTargetView(dialog.getButton(AlertDialog.BUTTON_POSITIVE));
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}
}
