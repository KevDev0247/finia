package protect.FinanceLord;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;

public class NetWorthCalculator {
    Context context;

    public NetWorthCalculator(Context context){
        this.context = context;
    }

    FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
    AssetsValueDao assetsValueDao = database.assetsValueDao();

    public void calculateTotal(){
    }

    public float calculateTotalOwnershipInterests(){

        return 0;
    }

    public float calculateTotalRetirementAccountsAssets(){
        return 0;
    }

    public float calculateTotalTaxableAccountsAssets(){
        return 0;
    }

    public float calculateTotalInvestedAssets(){
        return 0;
    }

    public float calculateTotalLiquidAssets(){
        return 0;
    }

    public float calculateTotalPersonalAssets(){
        return 0;
    }

    public float calculateTotalAssets(){
        return 0;
    }
}
