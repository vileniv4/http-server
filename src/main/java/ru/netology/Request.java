package ru.netology;

import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.StandardCharsets;

public class Request {
    private final String method;  // GET, POST и т.д.
    private final String path;    // /, /messages и т.д.
    private final String body;

    private final Map<String, String> headers;

    private final Map<String, List<String>> queryParams;

    private Map<String, List<String>> parseQuery(String queryString) {
        Map<String, List<String>> params = new HashMap<>();

        List<NameValuePair> parsed = URLEncodedUtils.parse(queryString, StandardCharsets.UTF_8);

        for (NameValuePair param : parsed) {
            String name = param.getName();
            String value = param.getValue();

            // если ключа ещё нет — создаём список
            if (!params.containsKey(name)) {
                params.put(name, new ArrayList<>());
            }
            // добавляем значение в список для этого ключа
            params.get(name).add(value);
        }

        return params;
    }

    public Request(String method, String fullPath, String body, Map<String, String> headers) {
        this.method = method;
        this.body = body;
        this.headers = headers;

        // Парсим путь и параметры
        if (fullPath.contains("?")) {
            String[] parts = fullPath.split("\\?", 2);
            this.path = parts[0];  // "/search"
            String queryString = parts[1];  // "word=cat&limit=10"
            this.queryParams = parseQuery(queryString);
        } else {
            this.path = fullPath;
            this.queryParams = new HashMap<>();
        }
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

    public Map<String, List<String>> getQueryParams() {
        return Collections.unmodifiableMap(queryParams);
    }

    public String getQueryParam(String name) {
        List<String> values = queryParams.get(name);
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }

}