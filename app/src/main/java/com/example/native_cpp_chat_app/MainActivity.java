package com.example.native_cpp_chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.StringPrepParseException;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.lang.Thread;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;

import com.example.native_cpp_chat_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native_cpp_chat_app' library on application startup.
    static {
        System.loadLibrary("native_cpp_chat_app");
    }

    private ActivityMainBinding binding;
    private ImageButton btn_msg_send;
    private EditText box_msg_input;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();

        Thread server_thread = new Thread(() -> {
            try {
                echoServerStart();
            } finally {
                try {
                    alertString("Server was interrupted");
                } catch (StringPrepParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        server_thread.start();

        Thread client_thread = new Thread(() -> {
            try {
                clientSocket = new Socket("127.0.0.1", 8080);
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                out.println("xxx");
//                alertString(in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }  finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        client_thread.start();

//        Thread reading_thread = new Thread(() -> {
//            try {
//                String line;
//                while((line = in.readLine()) != null) {
//                    alertString(line);
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (StringPrepParseException e) {
//                throw new RuntimeException(e);
//            }finally {
//                // Close the input stream when done
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    showErrorDialog("An error occurred while closing input: " + e.getMessage());
//                }
//            }
//        });
//
//        reading_thread.start();
    }

    private int setupUI() {

        btn_msg_send = findViewById(R.id.btn_msg_send);
        box_msg_input = findViewById(R.id.box_msg_input);

        btn_msg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = box_msg_input.getText().toString();
//                try {
//                    sendMessage(text);
//                    emptyMsgSendBox();
//                } catch (StringPrepParseException e) {
//                    throw new RuntimeException(e);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
                try {
                    alertString(text);
                } catch (StringPrepParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return 1;
    }

    //initializes text input box
    public int emptyMsgSendBox() {
        box_msg_input.setText("");
        return 1;
    }

    //sends message to server socket
    private String sendMessage(String msg) throws StringPrepParseException, IOException {

//        out.println(msg);

        out.println(msg);
        msg = in.readLine();
        return msg;

    }

//    public String receiveMessage() {
//        String line;
//        try {
//            while((line = in.readLine()) != null) {
//                alertString(line);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (StringPrepParseException e) {
//            throw new RuntimeException(e);
//        }
//        return line;
//    }

    //alerts string
    public String alertString(String alertStr) throws StringPrepParseException {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Response from server socket")
                .setMessage(alertStr)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Code to handle press on OK button
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        return alertStr;
    }

    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Error")
                .setMessage(errorMessage)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    //imports here
    public native String echoServerStart();
    /**
     * A native method that is implemented by the 'native_cpp_chat_app' native library,
     * which is packaged with this application.
     */
}
