// Service-Klasse für Uploads nach AWS S3
package com.example.orderservice.service;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.InputStream;

// Service für S3-Uploads (AWS), vorbereitet für Rechnungs-Upload.
@Service
public class S3Service {
    private final S3Client s3 = S3Client.builder()
            .region(Region.EU_CENTRAL_1)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    public void uploadInvoice(String bucket, String key, InputStream file) throws Exception {
        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file, file.available()));
    }
}
