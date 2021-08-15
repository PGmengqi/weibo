package route;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;

public class Request {
    public String route;
    public String method;
    public HashMap<String, String> query;
    public HashMap<String, String> form;
    public HashMap<String, String> headers;
    public HashMap<String, String> cookie;
    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public Request(String request) {
        String[] requestParts = request.split("\r\n\r\n", 2);
        String headers = requestParts[0];
        String body = requestParts[1];
        String[] lines = headers.split("\r\n");
        String requestLine = lines[0];
        String[] requestLineParts = requestLine.split(" ");
        this.method = requestLineParts[0];
        String path = requestLineParts[1];
        this.parsePath(path);
        this.parseBody(body);
        this.parseHeaders(lines);
    }

    private void parsePath(String path) {
        // this.path
        int index = path.indexOf("?");

        if (index == -1) {
            this.route = path;
            this.query = new HashMap<>();
        } else {
            this.route = path.substring(0, index);
            String queryString = path.substring(index + 1);
            log("queryString %s", queryString);
            try {
                queryString = URLDecoder.decode(queryString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            log("queryString url decode %s", queryString);
            String[] queries = queryString.split("&");
            this.query = new HashMap<>();
            for(int i = 0; i < queries.length; i ++) {
                String query = queries[i];
                log("query %s", query);
                String[] kv = query.split("=", 2);
                String k = kv[0];
                String v = kv[1];
                this.query.put(k, v);
            }
            log("this.query %s", this.query);
        }
    }
    private void parseBody(String body) {
        if (body.length() > 0) {
            // "".split("&") 会返回一个长度为一内容是一个空字符串的数组
            // 所以要排除长度为零的 body
            log("当前body是：" + body);
            String[] pairs = body.split("&");
            log("parseBody pairs <%s> <%s>", Arrays.toString(pairs), pairs.length);
            this.form = new HashMap<>();
            for (int i = 0; i < pairs.length; i++) {
                String pair = pairs[i];
                log("form <%s>", pair);
                String[] kv = pair.split("=", 2);
                // System.out.println("当前的kv是");
                // System.out.println(kv[0] + ":" +kv[1]);
                // String k = kv[0];
                //
                // String v = kv[1];
                // this.form.put(k, v);
            }
            log("this.form %s", this.form);
        }
    }
    private void parseHeaders(String[] lines) {
        this.headers = new HashMap<>();
        // i=1 才是请求头开始的行
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            String[] pairs = line.split(":");
            String key = pairs[0];
            // 除掉字符串开头和结尾的空白字符
            String value = pairs[1].strip();
            this.headers.put(key, value);
        }

        this.cookie = new HashMap<>();
        if (this.headers.containsKey("Cookie")) {
            String cookies = this.headers.get("Cookie");
            String args[] = cookies.split(";");
            for (int j = 0; j < args.length; j++) {
                String arg = args[j];
                String[] kv = arg.split("=", 2);
                this.cookie.put(kv[0].strip(), kv[1].strip());
            }
        }
    }
}
