package protect.FinanceLord.TransactionViewingUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.R;

public class View_RevenuesFragment extends Fragment {

    List<Transactions> transactions;

    public View_RevenuesFragment(List<Transactions> transactions){
        this.transactions = transactions;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View revenuesFragmentView = inflater.inflate(R.layout.fragment_view_revenues, null);

        return revenuesFragmentView;
    }
}
