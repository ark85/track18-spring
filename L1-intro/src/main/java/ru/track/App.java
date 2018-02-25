package ru.track;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * TASK:
 * POST request to  https://guarded-mesa-31536.herokuapp.com/track
 * fields: name,github,email
 *
 * LIB: http://unirest.io/java.html
 *
 *
 */
public class App {

    public static final String URL = "http://guarded-mesa-31536.herokuapp.com/track";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_GITHUB = "github";
    public static final String FIELD_EMAIL = "email";

    public static void main(String[] args) throws Exception {
        HttpResponse<JsonNode> r = Unirest.post(URL)
                .field(FIELD_NAME, "Arkadii")
                .field(FIELD_GITHUB, "ark85")
                .field(FIELD_EMAIL, "ark100295@yandex.ru")
                .asJson();

        System.out.println(r.getBody().getObject().get("success"));
    }

}
