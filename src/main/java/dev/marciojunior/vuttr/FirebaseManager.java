package dev.marciojunior.vuttr;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class FirebaseManager implements ServletContextListener {

    private static DatabaseReference databaseRef;

    public static DatabaseReference getDatabaseRef() {

        if (databaseRef == null) {
            throw new IllegalStateException("Contexto ainda não foi inicializado.");
        }

        return databaseRef;

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {

            // Inicializa o Firebase
            InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://vuttr-app.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
            
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            databaseRef = firebaseDatabase.getReference();

        } catch (IOException e) {
            System.err.println("Erro na leitura da chave.");
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
