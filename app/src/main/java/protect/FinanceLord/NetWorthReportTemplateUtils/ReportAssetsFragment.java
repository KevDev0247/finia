package protect.FinanceLord.NetWorthReportTemplateUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import protect.FinanceLord.R;

public class ReportAssetsFragment extends Fragment {

    String title;
    private ArrayList<ReportItemsDataModel> liquidAssetsDataSource;
    private ArrayList<ReportItemsDataModel> personalAssetsDataSource;
    private ArrayList<ReportItemsDataModel> taxableAccountsDataSource;
    private ArrayList<ReportItemsDataModel> retirementAccountsDataSource;
    private ArrayList<ReportItemsDataModel> ownershipInterestsDataSource;

    ListView liquidAssetsListView;
    ListView personalAssetsListView;
    ListView taxableAccountsListView;
    ListView retirementAccountsListView;
    ListView ownershipInterestsListView;

    public ReportAssetsFragment(String title){
        this.title = title;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View assetsView = inflater.inflate(R.layout.report_assets_layout, null);

        liquidAssetsListView = assetsView.findViewById(R.id.liquid_assets_list);
        personalAssetsListView = assetsView.findViewById(R.id.personal_assets_list);
        taxableAccountsListView = assetsView.findViewById(R.id.taxable_accounts_list);
        retirementAccountsListView = assetsView.findViewById(R.id.retirement_accounts_list);
        ownershipInterestsListView = assetsView.findViewById(R.id.ownership_interests_list);

        ReportListAdapter liquidAssetsAdapter = new ReportListAdapter(getContext(), liquidAssetsDataSource);
        ReportListAdapter personalAssetsAdapter = new ReportListAdapter(getContext(), personalAssetsDataSource);
        ReportListAdapter taxableAccountsAdapter = new ReportListAdapter(getContext(), taxableAccountsDataSource);
        ReportListAdapter retirementAccountsAdapter = new ReportListAdapter(getContext(), retirementAccountsDataSource);
        ReportListAdapter ownershipInterestsAdapter = new ReportListAdapter(getContext(), ownershipInterestsDataSource);

        liquidAssetsListView.setAdapter(liquidAssetsAdapter);
        personalAssetsListView.setAdapter(personalAssetsAdapter);
        taxableAccountsListView.setAdapter(taxableAccountsAdapter);
        retirementAccountsListView.setAdapter(retirementAccountsAdapter);
        ownershipInterestsListView.setAdapter(ownershipInterestsAdapter);

        return assetsView;
    }
}
