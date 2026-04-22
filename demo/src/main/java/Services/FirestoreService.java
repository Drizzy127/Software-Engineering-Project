package Services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class FirestoreService {

    private final Firestore db;

    public FirestoreService() throws Exception {

        //path to service account json
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new FileInputStream("src/main/resources/Firebase/login.json")
        );

        db = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId("YOUR_PROJECT_ID")
                .build()
                .getService();
    }

    public void saveUserDepartment(String uid, String email, String departmentId, String departmentName) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("departmentId", departmentId);
        data.put("departmentName", departmentName);

        db.collection("users")
                .document(uid)
                .set(data)
                .get();
    }

    public String getDepartmentName(String uid) throws Exception {
        var snapshot = db.collection("users")
                .document(uid)
                .get()
                .get();

        if (snapshot.exists()) {
            return snapshot.getString("departmentName");
        }
        return null;
    }
}