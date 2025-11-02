package projeto_amor_e_acao.TCC.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount =
                    getClass().getClassLoader().getResourceAsStream("firebase/serviceAccountKey.json");

            if (serviceAccount == null) {
                throw new IllegalStateException("Arquivo serviceAccountKey.json não encontrado em /resources/firebase/");
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("projeto-amor-e-acao-tcc.firebasestorage.app")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("🔥 Firebase inicializado com sucesso!");
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar Firebase: " + e.getMessage());
        }
    }
}
