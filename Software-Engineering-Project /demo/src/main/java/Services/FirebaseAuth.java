package Services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FirebaseAuth {

    private static final String API_KEY = "AIzaSyD-jeZT0KaJoQo0y3OLAOadO_1O8jbpqKA";

    private static final String SIGN_UP =
            "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY;

    private static final String SIGN_IN =
            "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;

    private final HttpClient client = HttpClient.newHttpClient();

    public AuthResult register(String email, String password) throws Exception {
        String response = sendAuthRequest(SIGN_UP, email, password);
        return parseResponse(response);
    }

    public AuthResult login(String email, String password) throws Exception {
        if (API_KEY.contains("AIzaSyD-jeZT0KaJoQo0y3OLAOadO_1O8jbpqKA")) {
            return new AuthResult(false, null, null, "Firebase API key has not been set yet.");
        }
        String response = sendAuthRequest(SIGN_IN, email, password);
        return parseResponse(response);
    }

    private String sendAuthRequest(String url, String email, String password) throws Exception {
        String json = """
        {
          "email": "%s",
          "password": "%s",
          "returnSecureToken": true
        }
        """.formatted(email, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private AuthResult parseResponse(String response) {
        boolean success = response.contains("\"localId\"");
        String uid = extractValue(response, "localId");
        String returnedEmail = extractValue(response, "email");
        return new AuthResult(success, uid, returnedEmail, response);
    }

    private String extractValue(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = json.indexOf(pattern);
        if (start == -1) return null;
        start += pattern.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return null;
        return json.substring(start, end);
    }
}
