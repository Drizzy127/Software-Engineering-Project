package Services;

public class AuthResult {
    private final boolean success;
    private final String uid;
    private final String email;
    private final String rawResponse;

    public AuthResult(boolean success, String uid, String email, String rawResponse) {
        this.success = success;
        this.uid = uid;
        this.email = email;
        this.rawResponse = rawResponse;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getRawResponse() {
        return rawResponse;
    }
}