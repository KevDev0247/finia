package protect.FinanceLord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = "FinanceLord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                intent.setClass(MainActivity.this, NetWorthEditReportsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeDatabase(){
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
    }
}