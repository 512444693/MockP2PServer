package com.zm.MockP2PServer;


import com.zm.MockP2PServer.server.MockP2PServer;

public class App {
    public static void main( String[] args ) {
        MockP2PServer server = MockP2PServer.getInstance();
        server.start(args);
    }
}
