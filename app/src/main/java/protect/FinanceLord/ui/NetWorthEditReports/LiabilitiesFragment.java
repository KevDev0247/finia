package protect.FinanceLord.ui.NetWorthEditReports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import protect.FinanceLord.R;

public class LiabilitiesFragment extends Fragment {
    String title;
    public LiabilitiesFragment(String title) {
        this.title = title;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View liabilitiesView = inflater.inflate(R.layout.fragment_liabilities, null);

        return liabilitiesView;
    }
}
