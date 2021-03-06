package bubbly2017.payfare.com.bubbly2017;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import android.graphics.drawable.Drawable;
import bubbly2017.payfare.com.bubbly2017.ServerSetup;

public class ChatConnection {

    private Handler mUpdateHandler;
    private ChatServer mChatServer;
    private ChatClient mChatClient;
    private String strMSGToSend;

    private static final String TAG = "ChatConnection";
    public boolean biSConnected=false;
    public boolean bIsConfRequest=false;
    public boolean bIsUploadRequest=false;
    private Socket mSocket;
    private int mPort = -1;
    public boolean bIsSending=false;


    public ChatConnection(Handler handler) {
        mUpdateHandler = handler;
        mChatServer = new ChatServer(handler);
    }

    public void tearDown() {
        mChatServer.tearDown();
        mChatClient.tearDown();
    }

    public void connectToServer(InetAddress address, int port) {
        mChatClient = new ChatClient(address, port);
    }

    public void sendMessage(String msg) {
        if (mChatClient != null) {
            mChatClient.sendMessage(msg);
            Log.d("BUBBLY",msg);
        }
    }

    public void setMessaggeToBeSend(String msgOUT){

        strMSGToSend=msgOUT;
    }

    public int getLocalPort() {
        return mPort;
    }

    public void setLocalPort(int port) {
        mPort = port;
    }


    public synchronized void updateMessages(String msg, boolean local) {
        Log.e(TAG, "Updating message: " + msg);

        //if(server_setup.bIsConnnected)
        if (local) {
            msg =  msg.trim();
        } else {
            msg =  msg.trim();
        }

        Bundle messageBundle = new Bundle();
        messageBundle.putString("msg", msg.trim());

        Message message = new Message();
        message.setData(messageBundle);
        mUpdateHandler.sendMessage(message);

    }

    private synchronized void setSocket(Socket socket) {
        Log.d(TAG, "setSocket being called.");
        if (socket == null) {
            Log.d(TAG, "Setting a null socket.");
        }
        if (mSocket != null) {
            if (mSocket.isConnected()) {
                try {
                    mSocket.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
        mSocket = socket;
    }

    private Socket getSocket() {
        return mSocket;
    }

    private class ChatServer {
        ServerSocket mServerSocket = null;
        Thread mThread = null;

        public ChatServer(Handler handler) {
            mThread = new Thread(new ServerThread());
            mThread.start();
        }

        public void tearDown() {
            mThread.interrupt();
            try {
                mServerSocket.close();
            } catch (IOException ioe) {
                Log.e(TAG, "Error when closing server socket.");
            }
        }

        class ServerThread implements Runnable {

            @Override
            public void run() {

                try {
                      mServerSocket = new ServerSocket(0);
                    setLocalPort(mServerSocket.getLocalPort());

                    while (!Thread.currentThread().isInterrupted()) {
                        Log.d(TAG, "ServerSocket Created, awaiting connection");
                        setSocket(mServerSocket.accept());
                        Log.d(TAG, "Connected.");
                        if (mChatClient == null) {
                            int port = mSocket.getPort();
                            InetAddress address = mSocket.getInetAddress();
                            connectToServer(address, port);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error creating ServerSocket: ", e);
                    e.printStackTrace();
                }
            }
        }
    }

    private class ChatClient {

        private InetAddress mAddress;
        private int PORT;

        private final String CLIENT_TAG = "Payfare_SVR_Client";

        private Thread mSendThread;
        private Thread mRecThread;

        public ChatClient(InetAddress address, int port) {

            Log.d(CLIENT_TAG, "Creating serverClient");
            this.mAddress = address;
            this.PORT = port;

            mSendThread = new Thread(new SendingThread());
            mSendThread.start();
        }

        class  SendingThread implements Runnable {

            BlockingQueue<String> mMessageQueue;
            private int QUEUE_CAPACITY = 10;

            public SendingThread() {
                mMessageQueue = new ArrayBlockingQueue<String>(QUEUE_CAPACITY);
            }

            @Override
            public void run() {
                try {
                    if (getSocket() == null) {
                        setSocket(new Socket(mAddress, PORT));
                        biSConnected=true;
                        updateConnectedButton();
                        Log.d(CLIENT_TAG, "Client-side socket initialized.");


                    } else {
                        biSConnected=true;
                        updateConnectedButton();
                        Log.d(CLIENT_TAG, "Socket already initialized. skipping!");
                    }

                    mRecThread = new Thread(new ReceivingThread());
                    mRecThread.start();

                } catch (UnknownHostException e) {
                    biSConnected=false;
                    updateConnectedButton();
                    Log.d(CLIENT_TAG, "Initializing socket failed, UHE", e);
                    updateMessages("Initializing socket failed, UHE" + e, false);
                } catch (IOException e) {
                    biSConnected=false;
                    updateConnectedButton();
                    Log.d(CLIENT_TAG, "Initializing socket failed, IOE.", e);
                    updateMessages("Initializing socket failed, IOE." + e, false);
                }

                while (true) {
                    try {
                        biSConnected=true;
                        updateConnectedButton();
                        String msg = mMessageQueue.take();

                        sendMessage(msg);
                    } catch (InterruptedException ie) {
                        biSConnected=false;
                        updateConnectedButton();
                        Log.d(CLIENT_TAG, "Message sending loop interrupted, exiting");
                    }
                }
            }
        }

        class ReceivingThread implements Runnable {

            @Override
            public void run() {

                BufferedReader input;
                try {
                    input = new BufferedReader(new InputStreamReader(
                            mSocket.getInputStream()));
                    while (!Thread.currentThread().isInterrupted()) {
                        biSConnected=true;
                        updateConnectedButton();
                        String messageStr = null;
                        messageStr = input.readLine();
                        if (messageStr != null) {
                            Log.d("BUBBLY", "RECEIV" + messageStr);
                            updateMessages(messageStr, false);
                        } else {

                            Log.d(CLIENT_TAG, "The nulls! The nulls!");
                            break;
                        }
                    }
                    input.close();
                    biSConnected=false;
                    updateConnectedButton();
                } catch (IOException e) {
                    biSConnected=false;
                    updateConnectedButton();
                    Log.e(CLIENT_TAG, "Server loop error: ", e);
                    updateMessages("Server loop error: " + e, false);
                }
            }
        }

        public void updateConnectedButton(){
        /*
            if(biSConnected){
                server_setup.radButtonSvrConnected.setButtonDrawable(R.drawable.serverbtn_ok);
            }else{

                server_setup.radButtonSvrConnected.setButtonDrawable(R.drawable.serverbtn_ok);
            }
*/

        }
        public void tearDown() {
            try {
                getSocket().close();
            } catch (IOException ioe) {
                Log.e(CLIENT_TAG, "Error when closing server socket.");
            }
        }

        public void sendMessage(String msg) {
            try {

                Socket socket = getSocket();
                if (socket == null) {
                    Log.d(CLIENT_TAG, "Socket is null, wtf?");
                } else if (socket.getOutputStream() == null) {
                    Log.d(CLIENT_TAG, "Socket output stream is null, wtf?");
                }


               /* PrintWriter out = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(getSocket().getOutputStream(), StandardCharsets.UTF_8)), true);*/


                PrintWriter out = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(getSocket().getOutputStream())), true);

                out.println(msg);
                out.flush();


                updateMessages(msg, true);
            } catch (UnknownHostException e) {
                Log.d(CLIENT_TAG, "Unknown Host", e);
            } catch (IOException e) {
                Log.d(CLIENT_TAG, "I/O Exception", e);
            } catch (Exception e) {
                Log.d(CLIENT_TAG, "Error3", e);
            }
            Log.d(CLIENT_TAG, "Client sent message: " + msg);
           // updateMessages("Sent", true);
        }
    }
}
