package kopo.poly.kpaas.service.impl;

import kopo.poly.kpaas.dto.SpamDTO;
import kopo.poly.kpaas.service.ISpamService;
import kopo.poly.kpaas.service.ITestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestService implements ITestService {

    private final ISpamService spamService;

    @Override
    public SpamDTO test(SpamDTO pDTO) {
        return spamService.predict(pDTO);
    }
}
