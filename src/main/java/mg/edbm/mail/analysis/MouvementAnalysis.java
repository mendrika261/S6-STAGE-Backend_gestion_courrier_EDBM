package mg.edbm.mail.analysis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MouvementAnalysis extends Analysis {
    private List<Input> possibleColumns;
    private List<Input> possibleMeasures;
    private List<Input> possibleOrders;

    public MouvementAnalysis(List<String> columns, List<String> mesures, List<String> orders, Long limit) {
        super(columns, mesures, orders, limit);
    }

    public List<Input> getPossibleColumns() {
        return List.of(
                new Input("Date de début", "m.start_date"),
                new Input("Date de début - Date seulement", "DATE(m.start_date)"),
                new Input("Date de début - Heure seulement", "EXTRACT(hour FROM m.start_date)"),
                new Input("Date de début - Jour", "extract('day' from m.start_date)"),
                new Input("Date de début - Mois", "TO_CHAR(m.start_date&comma; 'TMMonth')"),
                new Input("Date de début - Année", "extract('year' from m.start_date)"),
                new Input("Date de début - Jour de la semaine", "TO_CHAR(m.start_date&comma; 'TMDay')"),
                new Input("Date de fin", "m.end_date"),
                new Input("Date de fin - Date seulement", "DATE(m.end_date)"),
                new Input("Date de fin - Heure seulement", "EXTRACT(hour FROM m.end_date)"),
                new Input("Date de fin - Jour", "extract('day' from m.end_date)"),
                new Input("Date de fin - Mois", "TO_CHAR(m.end_date&comma; 'TMMonth')"),
                new Input("Date de fin - Année", "extract('year' from m.end_date)"),
                new Input("Date de fin - Jour de la semaine", "TO_CHAR(m.end_date&comma; 'TMDay')"),
                new Input("Expéditeur", "m.sender"),
                new Input("Localisation de l'expéditeur", "ls.name&comma; ls.latitude&comma; ls.longitude"),
                new Input("Destinataire", "m.receiver"),
                new Input("Localisation du destinataire", "lr.name&comma; lr.latitude&comma; lr.longitude"),
                new Input("Coursier", "u.first_name || ' ' || u.last_name")
        );
    }

    public List<Input> getPossibleMeasures() {
        return List.of(
                new Input("Nombre de mouvements", "count(m.id)"),
                new Input("Moyenne de durée (m)", "ROUND(EXTRACT(EPOCH FROM avg(m.end_date - m.start_date)) / 60, 0)"),
                new Input("Durée médiane (m)", "ROUND(EXTRACT(EPOCH FROM percentile_cont(0.5) within group (order by m.end_date - m.start_date)) / 60, 0)"),
                new Input("Durée minimale (m)", "ROUND(EXTRACT(EPOCH FROM min(m.end_date - m.start_date)) / 60, 0)"),
                new Input("Durée maximale (m)", "ROUND(EXTRACT(EPOCH FROM max(m.end_date - m.start_date)) / 60, 0)"),
                new Input("Durée totale (m)", "ROUND(EXTRACT(EPOCH FROM sum(m.end_date - m.start_date)) / 60, 0)")
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
