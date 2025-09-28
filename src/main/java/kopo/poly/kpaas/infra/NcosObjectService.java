package kopo.poly.kpaas.infra;

import kopo.poly.kpaas.config.NcosProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Service
public class NcosObjectService {

  private final NcosProperties ncosProperties;

  /**
   * Object Storage 파일 삭제
   * @param fileUrl public URL (예: https://bucket.region.ncloudstorage.com/folder/uuid.png)
   */
  public void deleteObject(String fileUrl) {
    if (fileUrl == null || fileUrl.isBlank()) {
      log.warn("[NCOS] 삭제할 파일 URL 없음");
      return;
    }

    // public URL → object key 추출
    String base = "https://" + ncosProperties.getBucket() + ".kr.object.ncloudstorage.com/";
    if (!fileUrl.startsWith(base)) {
      log.error("[NCOS] URL이 bucket public base와 다름: {}", fileUrl);
      return;
    }
    String key = fileUrl.substring(base.length());

    log.info("[NCOS] 삭제 요청 - bucket={}, key={}", ncosProperties.getBucket(), key);

    AwsBasicCredentials creds = AwsBasicCredentials.create(
            ncosProperties.getAccessKey(),
            ncosProperties.getSecretKey()
    );

    try (S3Client s3 = S3Client.builder()
            .endpointOverride(URI.create(ncosProperties.getEndpoint()))
            .region(Region.of(ncosProperties.getRegion()))
            .credentialsProvider(StaticCredentialsProvider.create(creds))
            .build()) {

      DeleteObjectRequest deleteReq = DeleteObjectRequest.builder()
              .bucket(ncosProperties.getBucket())
              .key(key)
              .build();

      s3.deleteObject(deleteReq);

      log.info("[NCOS] 삭제 완료 - {}", key);
    } catch (Exception e) {
      log.error("[NCOS] 파일 삭제 중 오류 발생", e);
    }
  }
}
