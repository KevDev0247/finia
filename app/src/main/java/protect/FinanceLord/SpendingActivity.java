package protect.FinanceLord;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

import protect.FinanceLord.DAOs.TransactionsDao;
import protect.FinanceLord.Database.FinanceLordDatabase;

public class SpendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);

        ImageButton returnButton = findViewById(R.id.spending_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void retrieveDataFromDatabase() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(SpendingActivity.this);
                TransactionsDao transactionsDao = database.transactionsDao();
            }
        });
    }

    private void setUpSpendingList() {

    }
}
