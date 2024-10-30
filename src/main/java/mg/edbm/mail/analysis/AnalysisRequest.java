package mg.edbm.mail.analysis;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnalysisRequest {
    private List<String> columns = new ArrayList<>();
    private List<String> mesures = new ArrayList<>();
    private List<String> orders = new ArrayList<>();
    private Long limit = 10L;


    public MailAnalysis toMailAnalysis() {
        return new MailAnalysis(columns, mesures, orders, limit);
    }

    public MouvementAnalysis toMouvementAnalysis() {
        return new MouvementAnalysis(columns, mesures, orders, limit);
    }
}
