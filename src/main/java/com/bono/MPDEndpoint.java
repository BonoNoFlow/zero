package com.bono;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Created by hendriknieuwenhuis on 28/09/15.
 */


public class MPDEndpoint {

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private String host = null;
    private int port = 0;

    private String version = null;

    private Socket socket;

    public MPDEndpoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String command(Command command) throws IOException {

        String reply = "";
        //boolean end = false;
        int read = 0;
        DataOutputStream out;
        DataInputStream in;

        connect();

        try {
            if (version.startsWith("OK")) {
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                out.write(command.getCommandBytes());
                out.flush();
                //buffer.flip();

                while ((read = in.read(buffer.array())) != -1) {
                    reply += new String(buffer.array(), 0, read);
                    if ((reply.startsWith("ACK")) || (reply.endsWith("OK\n"))) {
                        //System.out.println(reply + " " + this.getClass().getName());
                        break;
                    }
                }

            } else {
                return null;
            }
        } finally {
            buffer.clear();
            socket.close();
            //System.out.println(reply + " closed " + this.getClass().getName());
        }
        return reply;
    }

    // connect to server
    private void connect() throws IOException {
        byte[] versionBuffer = new byte[18];
        if (host != null && port != 0) {
            socket = new Socket(host, port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            int read = in.read(versionBuffer);
            version = new String(versionBuffer, 0, read);
        } else {
            throw new UnknownHostException();
        }
    }

    public void closeEndpoint() throws IOException {
        socket.close();
    }

    public String getVersion() throws IOException {
        connect();
        return version;
    }
}
