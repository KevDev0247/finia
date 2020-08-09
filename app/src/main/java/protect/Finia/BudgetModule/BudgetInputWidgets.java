package protect.Finia.BudgetModule;

import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * The class of all the input widgets used when adding or editing budget.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class BudgetInputWidgets {
    public TextInputLayout nameInputField;
    public TextInputLayout valueInputField;
    public TextInputLayout startDateInputField;
    public TextInputLayout endDateInputField;

    public AutoCompleteTextView nameInput;
    public TextInputEditText startDateInput;
    public TextInputEditText endDateInput;
    public TextInputEditText valueInput;

    public RelativeLayout deleteButton;
}
