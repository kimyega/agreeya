package kopo.poly.kpaas.service;

import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.AnalysisSummaryDto;
import kopo.poly.kpaas.dto.ClauseDto;
import kopo.poly.kpaas.mapper.ContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractMapper mapper;

    public AnalysisSummaryDto summary(int userId, int contractId){
        return mapper.getSummary(userId, contractId);
    }
    public List<ClauseDto> clauses(int contractId){
        return mapper.getClauses(contractId);
    }
}
