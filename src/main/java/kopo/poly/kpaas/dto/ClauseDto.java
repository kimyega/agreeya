package kopo.poly.kpaas.dto;
import lombok.Data;

@Data
public class ClauseDto {
    private Integer clauseId;
    private String clauseText;
    private Integer riskScore;
    private String aiComment;
    private String riskType;
}
