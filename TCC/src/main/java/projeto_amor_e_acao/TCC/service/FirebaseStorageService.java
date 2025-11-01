package projeto_amor_e_acao.TCC.service;

import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    private final String bucketName = "projeto-amor-e-acao-tcc.firebasestorage.app";

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        StorageClient.getInstance()
                .bucket()
                .create(fileName, file.getInputStream(), file.getContentType());

        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucketName, fileName.replace("/", "%2F")
        );
    }
}
