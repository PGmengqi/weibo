import route.Request;
import route.Route;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;


public class Server {

    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void socketSendAll(Socket socket, byte[] data) throws IOException {
        OutputStream output = socket.getOutputStream();
        // byte[] raw = data.getBytes();
        // output.write(raw);
        output.write(data);
    }

    public static String socketReadAll(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        // 指定读取的数据长度为 1024
        byte[] raw = new byte[1024];
        // read 函数会把读到的数据复制到 data 数组中去
        int size = input.read(raw);
        // chrome 有时候会发长度为 0 的空请求过来
        String data = "";
        if (size > 0) {
            data = new String(raw, 0, size);
        } else {
            log("这是一个 chrome 空请求");
        }
        return data;

    }






    private static void run(String host, int port) {
        // 监听请求
        // 获取请求数据
        // 发送响应数据
        // 我们的服务器使用 9000 端口
        // 不使用 80 的原因是 1024 以下的端口都要管理员权限才能使用
        log("服务器启动, 访问 http://%s:%s", host, port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                // accept 方法会一直停留在这里等待连接
                try (Socket socket = serverSocket.accept()) {
                    // 客户端连接上来了
                    log("client 连接成功");
                    // 读取客户端请求数据
                    String request = socketReadAll(socket);
                    // String response;
                    byte[] response;
                    if (request.length() > 0) {
                        // 输出响应的数据
                        log("请求:\n%s", request);
                        // 解析 request 得到 path
                        Request r = new Request(request);
                        log("请求 path:%s", r.route);
                        // 根据 path 来判断要返回什么数据
                        response = Route.responseForRoute(r);
                    } else {
                        // response = "";
                        response = new byte[0];
                        log("接受到了一个空请求");
                    }
                    socketSendAll(socket, response);
                }
            }
        } catch (IOException ex) {
            System.out.println("exception: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {


        run("localhost", 9000);
        // run("0.0.0.0", 80);
    }


}

