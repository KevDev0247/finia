package protect.FinanceLord.NetWorthDataTerminal;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.LiabilitiesTypeTree;

public class TypeProcessor_Liabilities {

    private List<LiabilitiesTypeTree> dataList;

    public TypeProcessor_Liabilities(List<LiabilitiesTypeTree> dataList){
        this.dataList = dataList;
    }

    public List<DataCarrier_Liabilities> getSubGroup(String parentGroupLabel, int level) {
        List<DataCarrier_Liabilities> subGroupLiabilities = new ArrayList<>();

        if (level == 0) {
            for (LiabilitiesTypeTree liabilitiesTypeTree : dataList) {
                if (liabilitiesTypeTree.liabilitiesFirstLevelName != null) {

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeTree.liabilitiesFirstLevelName, liabilitiesTypeTree.liabilitiesFirstLevelId, 0);
                    addTypeToSubGroup(dataCarrier, subGroupLiabilities);
                }
            }
        } else if (level == 1) {
            for (LiabilitiesTypeTree liabilitiesTypeTree : dataList) {
                if (liabilitiesTypeTree.liabilitiesFirstLevelName != null
                        && liabilitiesTypeTree.liabilitiesFirstLevelName.equals(parentGroupLabel)
                        && liabilitiesTypeTree.liabilitiesSecondLevelName != null) {

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeTree.liabilitiesSecondLevelName, liabilitiesTypeTree.liabilitiesSecondLevelId, 1);
                    addTypeToSubGroup(dataCarrier, subGroupLiabilities);
                }
            }
        } else if (level == 2) {
            for (LiabilitiesTypeTree liabilitiesTypeTree : dataList) {
                if (liabilitiesTypeTree.liabilitiesSecondLevelName != null
                        && liabilitiesTypeTree.liabilitiesSecondLevelName.equals(parentGroupLabel)
                        && liabilitiesTypeTree.liabilitiesThirdLevelName != null) {

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeTree.liabilitiesThirdLevelName, liabilitiesTypeTree.liabilitiesThirdLevelId, 2);
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
