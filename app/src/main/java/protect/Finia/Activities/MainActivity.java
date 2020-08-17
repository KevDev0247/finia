package protect.Finia.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.concurrent.Executors;

import protect.Finia.Database.DatabaseInitialization;
import protect.Finia.R;

/**
 * The starter activity of the whole application.
 * The Main Activity will direct the user to the four sections :
 * Transaction, Budget, Spending, and Net Worth.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/02/29
 */
public class MainActivity extends AppCompatActivity {
    private final static String TAG = "FinanceLord";

    /**
     * Create and initialize the activity.
     * This method was called when the activity was created.
     * The method first set the view of the content by finding the corresponding layout file through id.
     * Next, the function to initialize the database is called
     * Then the method define the buttons for the four sections. Note that the buttons' layout was defined
     * as LinearLayout because the resources in this circumstance were more difficult to load to a Button class.
     * View.onclickListener was added to the button to enable user go to the corresponding section through a click.
     *
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeDatabase();

        LinearLayout TransactionButton = findViewById(R.id.TransactionOption);
        LinearLayout BudgetButton = findViewById(R.id.BudgetOption);
        LinearLayout SpendingButton = findViewById(R.id.SpendingOption);
        LinearLayout NetWorthButton = findViewById(R.id.NetWorthOption);

        TransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TransactionActivity.class);
                startActivity(intent);
            }
        });

        BudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, BudgetActivity.class);
                startActivity(intent);
            }
        });

        SpendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SpendingActivity.class);
                startActivity(intent);
            }
        });

        NetWorthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NetWorthActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Initialize the database by calling each initialize method in an object of DatabaseInitialization class.
     * The initialization methods are called on separate threads to avoid locking the UI thread for a long period of time.
     * These methods will insert the default categories of each section into
     * the corresponding entity in the database.
     */
    private void initializeDatabase() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                new DatabaseInitialization(MainActivity.this).initAssetTypeDb();
            }
        });

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                new DatabaseInitialization(MainActivity.this).initAssetValueDb();
            }
        });

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                new DatabaseInitialization(MainActivity.this).initLiabilityTypeDb();
            }
        });

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                new DatabaseInitialization(MainActivity.this).initLiabilityValueDb();
            }
        });

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                new DatabaseInitialization(MainActivity.this).initBudgetTypeDb();
            }
        });

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                new DatabaseInitialization(MainActivity.this).initBudgetValueDb();
            }
        });

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                new DatabaseInitialization(MainActivity.this).initTransactionDb();
            }
        });
    }
}