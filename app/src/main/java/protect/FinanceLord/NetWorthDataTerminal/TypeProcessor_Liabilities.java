package protect.FinanceLord.NetWorthDataTerminal;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.LiabilitiesTypeTreeLeaf;

public class TypeProcessor_Liabilities {

    private List<LiabilitiesTypeTreeLeaf> dataList;

    public TypeProcessor_Liabilities(List<LiabilitiesTypeTreeLeaf> dataList){
        this.dataList = dataList;
    }

    public List<DataCarrier_Liabilities> getSubGroup(String parentGroupLabel, int level) {
        List<DataCarrier_Liabilities> subGroupLiabilities = new ArrayList<>();

        if (level == 0) {
            for (LiabilitiesTypeTreeLeaf liabilitiesTypeTreeLeaf : dataList) {
                if (liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName != null) {

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName, liabilitiesTypeTreeLeaf.liabilitiesFirstLevelId, 0);
                    addTypeToSubGroup(dataCarrier, subGroupLiabilities);
                }
            }
        } else if (level == 1) {
            for (LiabilitiesTypeTreeLeaf liabilitiesTypeTreeLeaf : dataList) {
                if (liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName != null
                        && liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName.equals(parentGroupLabel)
                        && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null) {

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName, liabilitiesTypeTreeLeaf.liabilitiesSecondLevelId, 1);
                    addTypeToSubGroup(dataCarrier, subGroupLiabilities);
                }
            }
        } else if (level == 2) {
            for (LiabilitiesTypeTreeLeaf liabilitiesTypeTreeLeaf : dataList) {
                if (liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null
                        && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName.equals(parentGroupLabel)
                        && liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName != null) {

                    DataCarrier_Liabilities dataCarrier = new DataCarrier_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName, liabilitiesTypeTreeLeaf.liabilitiesThirdLevelId, 2);
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
