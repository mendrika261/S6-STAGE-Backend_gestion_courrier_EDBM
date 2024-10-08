package mg.edbm.mail.analysis;

import lombok.Data;

import java.util.List;

@Data
public class AnalysisResult {
    private final List<String> columns;
    private final List<List<String>> data;
    private final String chartType;
    private final List<String> chartCategories;
    private final List<ChartData> chartData;

}
