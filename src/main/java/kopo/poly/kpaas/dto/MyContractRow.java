package kopo.poly.kpaas.dto;
import lombok.Data;

@Data
public class MyContractRow {
    private Integer contractId;
    private String analyzedDate; // YYYY-MM-DD
    private Integer riskCount;
}
