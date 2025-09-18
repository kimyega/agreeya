package kopo.poly.kpaas.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ContractUploadDTO {
    private MultipartFile file;
    private String userId;   // 필요하면 같이 받을 수도 있음
}
