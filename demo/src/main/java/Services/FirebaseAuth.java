package Services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FirebaseAuth {

    private static final String API_KEY = "pcms-ac8bc";
    //probably need to change..

    private static final String SIGN_UP =
            "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY;

    private static final String SIGN_IN =
            "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;

    private final HttpClient client = HttpClient.newHttpClient();

    public AuthResult register(String email, String password) throws Exception {
        String json = """
        {
          "email": "%s",
          "password": "%s",
          "returnSecureToken": true
        }
        """.formatted(email, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SIGN_UP))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

        boolean success = response.contains("\"localId\"");
        String uid = extractValue(response, "localId");
        String returnedEmail = extractValue(response, "email");

        return new AuthResult(success, uid, returnedEmail, response);
    }

    public AuthResult login(String email, String password) throws Exception {
        String json = """
        {
          "email": "%s",
          "password": "%s",
          "returnSecureToken": true
        }
        """.formatted(email, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SIGN_IN))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

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