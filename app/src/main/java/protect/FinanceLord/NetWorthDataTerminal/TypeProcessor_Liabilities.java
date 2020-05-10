package protect.FinanceLord.NetWorthDataTerminal;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.LiabilitiesTypeQuery;

public class TypeProcessor_Liabilities {

    private List<LiabilitiesTypeQuery> dataList;

    public TypeProcessor_Liabilities(List<LiabilitiesTypeQuery> dataList){
        this.dataList = dataList;
    }

    public List<DataCarrier_Liabilities> getSubGroup(String parentGroupLabel, int level) {

        List<DataCarrier_Liabilities> subGroupLiabilities = new ArrayList<>();

        if (level == 0) {
            for (LiabilitiesTypeQuery liabilitiesTypeQuery: dataList) {
                if (liabilitiesTypeQuery.liabilitiesFirstLevelName != null) {

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeQuery.liabilitiesFirstLevelName, liabilitiesTypeQuery.liabilitiesFirstLevelId, 0);
                    addTypeToSubGroup(dataCarrier, subGroupLiabilities);
                }
            }
        } else if (level == 1) {
            for (LiabilitiesTypeQuery liabilitiesTypeQuery: dataList) {
                if (liabilitiesTypeQuery.liabilitiesFirstLevelName != null
                        && liabilitiesTypeQuery.liabilitiesFirstLevelName.equals(parentGroupLabel)
                        && liabilitiesTypeQuery.liabilitiesSecondLevelName != null) {

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeQuery.liabilitiesSecondLevelName, liabilitiesTypeQuery.liabilitiesSecondLevelId, 1);
                    addTypeToSubGroup(dataCarrier, subGroupLiabilities);
                }
            }
        } else if (level == 2) {
            for (LiabilitiesTypeQuery liabilitiesTypeQuery: dataList) {
                if (liabilitiesTypeQuery.liabilitiesSecondLevelName != null
                        && liabilitiesTypeQuery.liabilitiesSecondLevelName.equals(parentGroupLabel)
                        && liabilitiesTypeQuery.liabilitiesThirdLevelName != null) {

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeQuery.liabilitiesThirdLevelName, liabilitiesTypeQuery.liabilitiesThirdLevelId, 2);
                    addTypeToSubGroup(dataCarrier, subGroupLiabilities);
                }
            }
        }

        return subGroupLiabilities;
    }

    void addTypeToSubGroup(DataCarrier_Liabilities liabilitiesFragmentDataCarrier, List<DataCarrier_Liabilities> subGroupLiabilities) {

        for (DataCarrier_Liabilities dataCarrier: subGroupLiabilities) {
            if (dataCarrier.liabilitiesId == liabilitiesFragmentDataCarrier.liabilitiesId && dataCarrier.liabilitiesId != 0) {
                return;
            }
        }
        subGroupLiabilities.add(liabilitiesFragmentDataCarrier);
    }
}
