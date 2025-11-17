package projeto_amor_e_acao.TCC.security.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount =
                        new FileInputStream("src/main/resources/firebase/serviceAccountKey.json");

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket("projeto-amor-e-acao-tcc.firebasestorage.app")
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("🔥 Firebase inicializado com sucesso!");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar Firebase: " + e.getMessage(), e);
        }
    }
}

