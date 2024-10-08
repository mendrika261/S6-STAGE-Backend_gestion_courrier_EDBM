package mg.edbm.mail.analysis;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public abstract class Analysis {
    private List<Input> columns;
    private List<Input> measures;
    private List<Input> orders;
    private Long limit;

    public Analysis(List<String> columns, List<String> measures, List<String> orders, Long limit) {
        setColumns(columns);
        setMeasures(measures);
        setOrders(orders);
        setLimit(limit);
    }

    public abstract List<Input> getPossibleColumns();
    public abstract List<Input> getPossibleMeasures();
    public abstract List<Input> getPossibleOrders();


    public void setColumns(List<String> columns) {
        this.columns = new ArrayList<>();
        for (String column : columns) {
            for (Input input : getPossibleColumns()) {
                if (input.getValue().contains(column)) {
                    input.setValue(input.getValue().replaceAll("&comma;", ","));
                    this.columns.add(input);
                    break;
                }
            }
        }
    }

    public void setMeasures(List<String> measures) {
        this.measures = new ArrayList<>();
        for (String mesure : measures) {
            for (Input input : getPossibleMeasures()) {
                if (input.getValue().equalsIgnoreCase(mesure)) {
                    this.measures.add(input);
                    break;
                }
            }
        }
    }

    public void setOrders(List<String> orders) {
        this.orders = new ArrayList<>();
        for (String order : orders) {
            for (Input input : getPossibleOrders()) {
                if (input.getValue().equalsIgnoreCase(order)) {
                    this.orders.add(input);
                    break;
                }
            }
        }
    }

}
