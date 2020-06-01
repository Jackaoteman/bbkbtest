package com.liyue.liyuetest.IO;

import org.springframework.web.bind.annotation.RequestHeader;
import springfox.documentation.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerThreadPool {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            ServerSocket serverSocket=new ServerSocket(8080);
            while (true){

                Socket accept = serverSocket.accept();
                System.out.println("Connection from "+accept.getRemoteSocketAddress());
              //  executor.submit(new ClientHandler(accept,new RequestHandler()));

            }




        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
