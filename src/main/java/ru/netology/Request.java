package ru.netology;

import java.util.Collections;
import java.util.Map;

public class Request {
    private final String method;  // GET, POST и т.д.
    private final String path;    // /, /messages и т.д.
    private final String body;

    private final Map<String, String> headers;

    public Request(String method, String path, String body, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.body = body;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

}