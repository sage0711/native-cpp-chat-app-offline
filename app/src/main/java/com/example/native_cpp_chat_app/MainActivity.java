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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;

import com.example.native_cpp_chat_app.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native_cpp_chat_app' library on application startup.
    static {
        System.loadLibrary("native_cpp_chat_app");
    }

    private ActivityMainBinding binding;
    private ImageButton btn_msg_send;
    private EditText box_msg_input;

    private String tempString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Thread server_start = new Thread() {
            @Override
            public void run() {

                    echoServerStart();

            }
        };

        server_start.start();

        setupUI();
    }

    private int setupUI() {

        btn_msg_send = findViewById(R.id.btn_msg_send);
        box_msg_input = findViewById(R.id.box_msg_input);

        btn_msg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = box_msg_input.getText().toString();
                try {
//                    alertString(text);
                    alertStringOnUIThread(text); // Call the modified method
                } catch (StringPrepParseException e) {
                    throw new RuntimeException(e);
                }
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket("127.0.0.1", 8080);
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            DataInputStream dis = new DataInputStream(socket.getInputStream());
                            dos.writeUTF(text);
                            dos.flush();
                            tempString = dis.readUTF().toString();
                            //alertString(tempString);
                            alertStringOnUIThread(tempString); // Call the modified method
                            dos.close();
                            dis.close();
                            socket.close();
                        } catch (IOException e) {
//                            throw new RuntimeException(e);
                            e.printStackTrace();
                        } catch (StringPrepParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                thread.start();
                emptyMsgSendBox();
            }
        });
        return 1;
    }

    public String alertStringOnUIThread(String alertStr) throws StringPrepParseException {
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Response from server socket")
                        .setMessage(alertStr)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return alertStr;
    }

    //initializes text input box
    public int emptyMsgSendBox() {
        box_msg_input.setText("");
        return 1;
    }


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

    //imports here
    public native String echoServerStart();
    /**
     * A native method that is implemented by the 'native_cpp_chat_app' native library,
     * which is packaged with this application.
     */
}
