package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractUploadDTO {
    private MultipartFile file; // 프론트에서 파일 업로드할 때 사용
    private String fileUrl;     // 프론트에서 Presigned URL 업로드 후 전달할 때 사용
    private String userId;      // 필요하면 같이 받을 수도 있음

}
