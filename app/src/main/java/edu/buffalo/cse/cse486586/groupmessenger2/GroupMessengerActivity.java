package edu.buffalo.cse.cse486586.groupmessenger2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * GroupMessengerActivity is the main Activity for the assignment.
 * 
 * @author stevko
 *
 */
public class GroupMessengerActivity extends Activity {


    static final int SERVER_PORT = 10000;
    static final String TAG = GroupMessengerActivity.class.getSimpleName();

    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";
    private static int seq = 0;
    private String remotePort[] = {"11108","11112","11116","11120","11124"};
    private final Uri mUri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger2.provider");
    Double latestProposedPriority = new Double("0.00");
    Double latestAgreedPriorityReceived = new Double("0.00");
    Double latestAgreedPriorityPropsed = new Double("0.00");
    private String myPort = "";
    boolean flag = true;


//     void printQueue(PriorityQueue<CompMessage> pq){
//        Boolean deliverable = true;
//        String msg = null;
//        Double prior = 0.00;
//        CompMessage message = new CompMessage(msg, prior, deliverable);
//        Iterator iterator = pq.iterator();
//        while(iterator.hasNext())
//        {
//            String mess = message.getMsg(), message.priority, message.isDeliverable();
//            Log.i(TAG, "priority queue: ", + mess);
//        }
//    }
   // private java.util.PriorityQueue<Message> queue = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);

        /*
         * TODO: Use the TextView to display your messages. Though there is no grading component
         * on how you display the messages, if you implement it, it'll make your debugging easier.
         */
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());
        
        /*
         * Registers OnPTestClickListener for "button1" in the layout, which is the "PTest" button.
         * OnPTestClickListener demonstrates how to access a ContentProvider.
         */
        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));
        
        /*
         * TODO: You need to register and implement an OnClickListener for the "Send" button.
         * In your implementation you need to get the message from the input box (EditText)
         * and send it to other AVDs.
         */
/////////////////////
//        ContentValues keyValueToInsert = new ContentValues();
//        keyValueToInsert.put(KEY_FIELD, 1);
//        Log.i(TAG, "inserting seq - son create");
//        keyValueToInsert.put(VALUE_FIELD, "rashhhhhhhhhhhhhhhhhhh");
//        Log.i(TAG, "inserting msg - server" + "rashhhhhhhhhhhhhhhhhhh");
//        getContentResolver().insert(mUri, keyValueToInsert);



        ///////////////////////
        TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        myPort = String.valueOf((Integer.parseInt(portStr) * 2));

        try {

            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
            Log.d(TAG, "socket created");
        } catch (IOException e) {
            Log.e(TAG, "Can't create a ServerSocket");
            return;
        }

        final Button button = (Button) findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.editText1);
                String msg = editText.getText().toString();// + "\n";
                editText.setText(""); // This is one way to reset the input box.
                TextView localTextView = (TextView) findViewById(R.id.textView1);
                localTextView.append("\t" + msg); // This is one way to display a string.
                localTextView.append("\n");

                Log.i(TAG,"Message sent - onClickListener");

                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myPort);

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction0() {
        Thing object = new Thing.Builder()
                .setName("GroupMessenger Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction0());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/Ap
        AppIndex.AppIndexApi.end(client, getIndexApiAction0());
        client.disconnect();
    }

 public class CompMessage implements Comparable<CompMessage> {

     private Double priority;
     private String msg = "";
     private boolean deliverable;
     public String sourcePort;


     public CompMessage(String msg, Double priority, boolean deliverable, String sourcePort) {
         this.msg = msg;
         this.priority = priority;
         this.deliverable = deliverable;
         this.sourcePort = sourcePort;
     }

     public String getMsg() {
         return msg;
     }

     public void setMsg(String msg) {
         this.msg = msg;
     }

     @Override
     public int compareTo(CompMessage msg) {
         return priority.compareTo(msg.priority);
     }

     public boolean isDeliverable() {
         return deliverable;
     }

     public void setDeliverable(boolean deliverable) {
         this.deliverable = deliverable;
     }
 }


    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

    @Override
    protected Void doInBackground(ServerSocket... sockets) {
        ServerSocket serverSocket = sockets[0];
        int g = 0;

        Double priority = new Double("0.00");

        PriorityQueue<CompMessage> priorityQueue = new PriorityQueue<CompMessage>();

        BufferedReader bufferedReader;
        Log.i(TAG, "Server Started");

            while (g<10) {

                try {
                    serverSocket.setSoTimeout(5000);
                    Socket socket = serverSocket.accept();
                    Log.i(TAG, "entered the server");
                    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
//                pw.println("Acknowledged");
//                pw.flush();
//                String str = "#Rashmil";
//                List<String> List = Arrays.asList(remotePort);
//                List.add(str);

                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String input = null; //bufferedReader.readLine();

                    //bufferedReader.close();

                    input = bufferedReader.readLine();
                    if (input != null) {
                        Log.i(TAG, "msg: " + input);
                        String[] arrayOfStrings = input.split(",");
                        //String result = "";
                        //List<String> list = Arrays.asList(arrayOfStrings);

//                        try {

//                        Log.i(TAG, "length - server" + input + " arrayOfStrings = " + arrayOfStrings[0] + " arrayOfStrings.length = " + arrayOfStrings.length);
                            if (arrayOfStrings.length < 3) {

                                if (arrayOfStrings[0].equals("gossip")) {
                                    String portDown = remotePort[Integer.parseInt(arrayOfStrings[1])];
//                                    CompMessage head = priorityQueue.peek();

                                    Iterator iterator = priorityQueue.iterator();

                                    while (iterator.hasNext()) {

                                        CompMessage temp = (CompMessage) iterator.next();

                                        if (temp.sourcePort.equals(portDown)) {
                                            priorityQueue.remove(temp);
                                        }

//                                        head = priorityQueue.peek();
                                        //socket.close();
                                    }


                                } else {
                                    Log.i(TAG, "entered if - server");
                                    Log.i(TAG, "entered if - server" + latestAgreedPriorityReceived);
                                    Log.i(TAG, "entered if - server" + latestProposedPriority.toString());
                                    priority = Math.max(latestAgreedPriorityReceived, latestProposedPriority) + 1.0;
                                    Log.i(TAG, "priority: " + latestProposedPriority);
                                    Double prior = priority;
                                    if (flag) {
                                        prior = prior + (Double.parseDouble(myPort) / 100000);
                                    }
                                    Log.i(TAG, "priority - server:" + prior);
                                    latestProposedPriority = prior;
                                    pw.println(latestProposedPriority);
                                    pw.flush();
                                    CompMessage message = new CompMessage(arrayOfStrings[0], latestProposedPriority, false, arrayOfStrings[1]);
                                    priorityQueue.add(message);
                                }
                                // printQueue(priorityQueue);
                                //CompMessage head = priorityQueue.peek();
                                //socket.close();
//                         try {
//                             Thread.sleep(200);
//                         } catch (InterruptedException e) {
//                             Log.e(TAG, "sleep");
//                             e.printStackTrace();
//                         }

                            } else {


                                Log.d(TAG, "Else ");
                                Iterator iterator = priorityQueue.iterator();
                                Log.i(TAG, "in while - server: " + input);
                                while (iterator.hasNext()) {

                                    CompMessage newMsg;
                                    newMsg = (CompMessage) iterator.next();
                                    boolean r = newMsg.msg.equals(arrayOfStrings[0]);
                                    if (r) {
                                        priorityQueue.remove(newMsg);
                                        latestAgreedPriorityReceived = Double.parseDouble(arrayOfStrings[1]);
                                        Log.i(TAG, "where are you now");
                                        CompMessage oldMsg = new CompMessage(arrayOfStrings[0], latestAgreedPriorityReceived, true, arrayOfStrings[2]);
                                        priorityQueue.add(oldMsg);
                                        CompMessage head = priorityQueue.peek();
                                        while (priorityQueue.size() != 0 && head.isDeliverable()) {
                                            CompMessage poll = priorityQueue.poll();
                                            head = priorityQueue.peek();

                                            ContentValues keyValueToInsert = new ContentValues();
                                            keyValueToInsert.put(KEY_FIELD, String.valueOf(seq));
                                            Log.i(TAG, "inserting seq - server " + seq);
                                            keyValueToInsert.put(VALUE_FIELD, poll.getMsg());
                                            Log.i(TAG, "inserting msg - server" + poll.getMsg());
                                            getContentResolver().insert(mUri, keyValueToInsert);
                                            //Cursor resultCursor = getContentResolver().query(mUri, null, String.valueOf(keyValueToInsert), null, null);
                                            seq++;

                                            publishProgress(poll.getMsg());
                                            //socket.close();
                                        }
                                    }

                                }
                            }


//                        } catch (ArrayIndexOutOfBoundsException e) {
//                            Log.e(TAG, "failed - server");
//                        } catch (NumberFormatException e) {
//                            Log.e(TAG, "This is not a number");
//                            e.printStackTrace();
//                        }

//                CompMessage poll = priorityQueue.poll();
//                String resultString = poll.msg;

                        //String resultString = arrayOfStrings[0];

                        bufferedReader.close();

                        // Make server side both read and write messages.

                        // The priority queue will store the messages with the agreed priorities. No need to store the messages with proposed priorities.

                        // Add a count variable for the priority.

                        // Server side will receive client's message and send a proposed priority to its client and the other clients.
                        // The priority that the server sends to the client, has to be greater than the latest proposed priority proposed by the client to itself,
                        // and the latest agreed priority that the client has received so far. Calculate that and increment it.
                        // After sending the proposed priority, it will wait for the client to send agreed priority.
                        // Close the connections.

//                ContentValues keyValueToInsert = new ContentValues();
//                keyValueToInsert.put(KEY_FIELD, seq++);
//                Log.e(TAG, "inserting seq");
//                keyValueToInsert.put(VALUE_FIELD, resultString);
//                Log.e(TAG, "inserting msg");

//                Uri newUri = getContentResolver().insert(mUri, keyValueToInsert);

                        Log.i(TAG, "store message - server");

                    }
                    //socket.setSoTimeout(500);

                    //Cursor resultCursor = getContentResolver().query(mUri, null, String.valueOf(keyValueToInsert), null, null);
                    //socket.close();
                    //publishProgress(resultString);
                } catch (SocketTimeoutException e)
                {
                    Log.e(TAG,"Timeout " + priorityQueue.size());
                    while (priorityQueue.size() != 0) {
                        CompMessage poll = priorityQueue.poll();
//                        head = priorityQueue.peek();

                        ContentValues keyValueToInsert = new ContentValues();
                        keyValueToInsert.put(KEY_FIELD, String.valueOf(seq));
                        Log.i(TAG, "inserting seq - server " + seq);
                        keyValueToInsert.put(VALUE_FIELD, poll.getMsg());
                        Log.i(TAG, "inserting msg - server" + poll.getMsg());
                        getContentResolver().insert(mUri, keyValueToInsert);
                        //Cursor resultCursor = getContentResolver().query(mUri, null, String.valueOf(keyValueToInsert), null, null);
                        seq++;


//                        publishProgress(poll.getMsg());
                        //socket.close();
                    }

                }catch (IOException e)
                {
                    Log.e(TAG,"exception in server");
                }
            }
//
//            try {
//                serverSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        return null;
    }


        protected void onProgressUpdate(String... strings) {

        String strReceived = strings[0].trim();
        TextView remoteTextView = (TextView) findViewById(R.id.textView1);
        remoteTextView.append(strReceived + "\t\n");
        // TextView localTextView = (TextView) findViewById(R.id.local_text_display);
        //localTextView.append("\n");

//            String filename = "SimpleMessengerOutput";
//            String string = strReceived + "\n";
//            FileOutputStream outputStream;

//            try {
//                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
//                outputStream.write(string.getBytes());
//                outputStream.close();
//            } catch (Exception e) {
//                Log.e(TAG, "File write failed");
//            }

        return;
    }

}

    private class ClientTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... msgs) {

            String msgToSend = msgs[0];
            Double max = new Double("-1.00");
            for (int i = 0; i <5; i++) {
                try {
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(remotePort[i]));
                Log.i(TAG, "client process created- " +remotePort[i]);


                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                    pw.println(msgToSend+","+myPort);
                    pw.flush();
                    Log.i(TAG, "Sent message - client: " + msgToSend);

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //Log.i(TAG, "1 -client");
//                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    //Log.i(TAG, "2 -client");

                    String rcv = bufferedReader.readLine();
                   Log.i(TAG, "3 -client: "+ rcv);


                        Double str = Double.parseDouble(rcv);
                        //Log.i(TAG, "float kar re baba -client: " + str);
                        if (str > max){
                         max = str;
                        }
                       // Log.i(TAG, "max compute kar re baba -client: " + max);


                    Log.i(TAG, "3 -client max: "+ max);


//                    ArrayList<Float> array = new ArrayList<Float>();
//                    array.add(latestProposedPriority);
//                    float max = Collections.max(array);
//                    priority = max;

                    bufferedReader.close();
                    socket.close();

                    // Client will send a message to its server and the other servers.
                    // After sending the message, it will wait for the servers to send the proposed priorities to it.
                    // After receiving the priorities, the client will select the highest priority as the agreed priority and re-broadcast the message to all the servers.
                    // Close the connections.

                    // Client side should also send and receive messages.

                } catch (Exception e) {
                    Log.e(TAG, "an exception caught in client");
                    int l =0;
                    while(!myPort.equals(remotePort[l]))
                        l++;
                    String gossipRecipient = remotePort[(l+1)%5];

                    if((l+1)%5!=i)
                    {
                        Socket gossipSocket = null;
                        try {
                            gossipSocket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(gossipRecipient));
                            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(gossipSocket.getOutputStream())));
                            printWriter.println("gossip,"+i);
                            printWriter.flush();
                            gossipSocket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    Log.e(TAG, "sleep");
//                    e.printStackTrace();
//                }
//                Log.i(TAG, "yeh Babu Rao ka istyle hai -client");
            }
            for (int j = 0; j <5 ; j++)
            {
                try{
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(remotePort[j]));
                Log.i(TAG, "process created -client part deux: " + remotePort[j]);


                    PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                    //Log.i(TAG, "writer mein ghuso -client");

                    String array = msgToSend+","+String.valueOf(max)+","+myPort;
                    //Log.i(TAG, "concatenate kar nai toh... -client");

                    printWriter.println(array);
                   // Log.i(TAG, "print msg: " +array);
                    printWriter.flush();
                    socket.close();

                }catch (Exception e) {
                    Log.e(TAG, "an exception caught in client");
                    int l =0;
                    while(!myPort.equals(remotePort[l]))
                        l++;
                    String gossipRecipient = remotePort[(l+1)%5];

                    if((l+1)%5!=j)
                    {

                        Socket gossipSocket = null;
                        try {
                            gossipSocket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(gossipRecipient));
                            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(gossipSocket.getOutputStream())));
                            printWriter.println("gossip," + j);
                            printWriter.flush();
                            gossipSocket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                }

            }

        return null;
        }

    }
}