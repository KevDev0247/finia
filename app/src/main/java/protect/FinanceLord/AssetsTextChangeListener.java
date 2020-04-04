package protect.FinanceLord;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import java.lang.ref.WeakReference;

import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;

public class AssetsTextChangeListener implements TextWatcher {
    WeakReference<Activity> context;




    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) { }
}
