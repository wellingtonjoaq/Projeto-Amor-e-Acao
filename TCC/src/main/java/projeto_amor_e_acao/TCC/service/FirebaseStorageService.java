package projeto_amor_e_acao.TCC.service;

import com.google.cloud.storage.Blob;
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
                .bucket(bucketName)
                .create(fileName, file.getInputStream(), file.getContentType());

        return "https://firebasestorage.googleapis.com/v0/b/" +
                bucketName + "/o/" +
                fileName.replace("/", "%2F") + "?alt=media";
    }

    public boolean deleteFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return false;
        }

        if (fileName.contains("https://")) {
            int index = fileName.indexOf("/o/") + 3;
            int end = fileName.indexOf("?alt=");
            if (index >= 3 && end > index) {
                fileName = fileName.substring(index, end).replace("%2F", "/");
            } else {
                return false;
            }
        }

        Blob blob = StorageClient.getInstance().bucket(bucketName).get(fileName);
        return blob != null && blob.delete();
    }

}

