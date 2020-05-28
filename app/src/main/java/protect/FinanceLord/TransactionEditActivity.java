package protect.FinanceLord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class TransactionEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);

        ImageButton returnButton = findViewById(R.id.transaction_edit_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String fragmentTag = getIntent().getExtras().getString(getString(R.string.transaction_fragment_key));
        if (fragmentTag.equals(getString(R.string.view_revenues_fragment_key))) {
            View editSectionView = LayoutInflater.from(this).inflate(R.layout.fragment_edit_expenses, null, false);
            LinearLayout sheet = findViewById(R.id.transaction_section_view);
            sheet.addView(editSectionView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        } else if (fragmentTag.equals(getString(R.string.view_expenses_fragment_key))) {
            View editSectionView = LayoutInflater.from(this).inflate(R.layout.fragment_edit_revenues, null, false);
            LinearLayout sheet = findViewById(R.id.transaction_section_view);
            sheet.addView(editSectionView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
    }
}
