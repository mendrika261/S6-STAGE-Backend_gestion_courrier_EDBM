package mg.edbm.mail.analysis;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MailAnalysis extends Analysis {
    private List<Input> possibleColumns;
    private List<Input> possibleMeasures;
    private List<Input> possibleOrders;

    public MailAnalysis(List<String> columns, List<String> mesures, List<String> orders, Long limit) {
        super(columns, mesures, orders, limit);
    }

    public List<Input> getPossibleColumns() {
        return List.of(
                new Input("Date de création", "m.created_at"),
                new Input("Date de création - Date seulement", "DATE(m.created_at)"),
                new Input("Date de création - Heure seulement", "m.created_at::time"),
                new Input("Date de création - Jour", "extract('day' from m.created_at)"),
                new Input("Date de création - Mois", "TO_CHAR(m.created_at&comma; 'TMMonth')"),
                new Input("Date de création - Année", "extract('year' from m.created_at)"),
                new Input("Date de création - Jour de la semaine", "TO_CHAR(m.created_at&comma; 'TMDay')"),
                new Input("Référence", "m.reference"),
                new Input("Confidentialité", "m.confidentiality"),
                new Input("Priorité", "m.priority"),
                new Input("Expéditeur", "m.sender"),
                new Input("Localisation de l'expéditeur", "ls.name&comma; ls.latitude&comma; ls.longitude"),
                new Input("Destinataire", "m.receiver"),
                new Input("Localisation du destinataire", "lr.name&comma; lr.latitude&comma; lr.longitude"),
                new Input("Date de fin", "mv.end_date"),
                new Input("Date de fin - Date seulement", "DATE(mv.end_date)"),
                new Input("Date de fin - Heure seulement", "mv.end_date::time"),
                new Input("Date de fin - Jour", "extract('day' from mv.end_date)"),
                new Input("Date de fin - Mois", "TO_CHAR(mv.end_date&comma; 'TMMonth')"),
                new Input("Date de fin - Année", "extract('year' from mv.end_date)"),
                new Input("Date de fin - Jour de la semaine", "TO_CHAR(mv.end_date&comma; 'TMDay')"),
                new Input("Durée", "mv.end_date - m.created_at"),
                new Input("Durée - Jours", "extract('day' from mv.end_date - m.created_at)"),
                new Input("Durée - Heures", "extract('hour' from mv.end_date - m.created_at)"),
                new Input("Durée - Minutes", "extract('minute' from mv.end_date - m.created_at)")
        );
    }

    public List<Input> getPossibleMeasures() {
        return List.of(
                new Input("Nombre de mails", "count(distinct m.id)"),
                new Input("Moyenne de durée (m)", "EXTRACT(EPOCH FROM avg(mv.end_date - m.created_at)) / 60"),
                new Input("Durée médiane (m)", "EXTRACT(EPOCH FROM percentile_cont(0.5) within group (order by mv.end_date - m.created_at)) / 60"),
                new Input("Durée minimale (m)", "EXTRACT(EPOCH FROM min(mv.end_date - m.created_at)) / 60"),
                new Input("Durée maximale (m)", "EXTRACT(EPOCH FROM max(mv.end_date - m.created_at)) / 60"),
                new Input("Durée totale (m)", "EXTRACT(EPOCH FROM sum(mv.end_date - m.created_at)) / 60")
        );
    }

    public List<Input> getPossibleOrders() {
        final List<Input> possibleOrders = new ArrayList<>();
        final List<Input> possibleColumns = new ArrayList<>(getPossibleMeasures());
        possibleColumns.addAll(getPossibleColumns());
        for (Input input : possibleColumns) {
            possibleOrders.add(new Input(input.getName() + " - Croissant", input.getValue() + " asc"));
            possibleOrders.add(new Input(input.getName() + " - Décroissant", input.getValue() + " desc"));
        }
        return possibleOrders;
    }
}
