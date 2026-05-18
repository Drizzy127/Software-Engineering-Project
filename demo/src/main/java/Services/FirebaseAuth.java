package Services;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

public class FirebaseAuth {
    private final FirestoreService firestoreService;

    public FirebaseAuth() throws Exception {
        firestoreService = new FirestoreService();
    }

    public AuthResult register(String email, String password, String departmentName) throws Exception {
        email = email.trim().toLowerCase();

        UserRecord user;
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);
            user = com.google.firebase.auth.FirebaseAuth.getInstance().createUser(request);
        } catch (FirebaseAuthException e) {
            try {
                user = com.google.firebase.auth.FirebaseAuth.getInstance().getUserByEmail(email);
            } catch (FirebaseAuthException notExisting) {
                throw e;
            }
        }

        firestoreService.saveUserLogin(user.getUid(), email, password, departmentName);
        return new AuthResult(true, user.getUid(), email, "Registered with Firebase Auth and saved profile in Firestore.");
    }

    public AuthResult login(String email, String password) throws Exception {
        email = email.trim().toLowerCase();
        DocumentSnapshot userDoc = firestoreService.findUserByEmail(email);

        if (userDoc == null || !userDoc.exists()) {
            return new AuthResult(false, null, email, "No Firestore user record found.");
        }

        String savedPassword = userDoc.getString("password");
        if (savedPassword == null || !savedPassword.equals(password)) {
            return new AuthResult(false, null, email, "Invalid password.");
        }

        String uid = userDoc.getString("uid");
        if (uid == null || uid.isBlank()) uid = userDoc.getId();

        return new AuthResult(true, uid, email, "Login successful.");
    }
}
