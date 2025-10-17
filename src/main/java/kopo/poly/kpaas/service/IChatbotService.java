package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.QaLogDTO;

public interface IChatbotService {
        String askQuestion(QaLogDTO pDTO) throws Exception;
}
