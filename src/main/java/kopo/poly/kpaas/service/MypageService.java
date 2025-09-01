package kopo.poly.kpaas.service;

import kopo.poly.kpaas.mapper.MypageMapper;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class MypageService {

    private final MypageMapper mypageMapper;

    public MypageService(MypageMapper mypageMapper) {
        this.mypageMapper = mypageMapper;
    }

    public Map<String, Object> getProfile(String userId) {
        return mypageMapper.selectUserProfile(userId);
    }
}
