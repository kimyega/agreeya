package kopo.poly.kpaas.infra;

import kopo.poly.kpaas.config.NcosProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class NcosPresignService {

    private final NcosProperties ncosProperties;
    private final String publicBase = "https://contest-70-bucket.kr.object.ncloudstorage.com";

    public PresignedUpload createUploadUrl(String folder, String contentType) {
        String key = folder + "/" + UUID.randomUUID() + guessExt(contentType);
        log.info("[NCOS] Generating presigned URL - folder: {}, contentType: {}, key: {}", folder, contentType, key);

        AwsBasicCredentials creds = AwsBasicCredentials.create(
                ncosProperties.getAccessKey(),
                ncosProperties.getSecretKey()
        );

        try (S3Presigner presigner = S3Presigner.builder()
                .endpointOverride(URI.create(ncosProperties.getEndpoint()))
                .region(Region.of(ncosProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build()) {

            log.debug("[NCOS] Presigner created with endpoint {} and region {}", ncosProperties.getEndpoint(), ncosProperties.getRegion());

            PutObjectRequest put = PutObjectRequest.builder()
                    .bucket(ncosProperties.getBucket())
                    .key(key)
                    .contentType(contentType)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            log.debug("[NCOS] PutObjectRequest built for bucket {}", ncosProperties.getBucket());

            PutObjectPresignRequest presign = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(put)
                    .build();

            PresignedPutObjectRequest pre = presigner.presignPutObject(presign);
            URL url = pre.url();

            String base = publicBase.endsWith("/") ? publicBase.substring(0, publicBase.length() - 1) : publicBase;
            String publicUrl = base + "/" + key;

            log.info("[NCOS] Presigned upload URL generated: {}, public URL: {}", url, publicUrl);

            return new PresignedUpload(url.toString(), publicUrl, key, contentType);
        } catch (Exception e) {
            log.error("[NCOS] Error generating presigned URL", e);
            throw e;
        }
    }

    private String guessExt(String ct) {
        if (ct == null) return "";
        switch (ct.toLowerCase()) {
            case "image/png": return ".png";
            case "image/jpeg": return ".jpg";
            case "image/webp": return ".webp";
            default: return "";
        }
    }

    public record PresignedUpload(String uploadUrl, String publicUrl, String key, String contentType) {}
}
