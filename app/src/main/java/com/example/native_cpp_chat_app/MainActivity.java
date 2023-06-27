package com.example.native_cpp_chat_app;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.StringPrepParseException;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.lang.Thread;

import com.example.native_cpp_chat_app.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native_cpp_chat_app' library on application startup.
    static {
        System.loadLibrary("native_cpp_chat_app");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Thread server_thread = new Thread(() -> {
            start();
        });
        Thread client_thread = new Thread(() -> {
            Socket socket = null;
            try {
                socket = new Socket("localhost", 8080);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                String message = "hello";
                OutputStream outputStream = null;
                try {
                    outputStream = socket.getOutputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                try {
                    dataOutputStream.writeUTF(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                InputStream inputStream = null;
                try {
                    inputStream = socket.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                String response = null;
                try {
                    response = dataInputStream.readUTF();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    alertString("Received from server: " + response);
                } catch (StringPrepParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    public String alertString(String alertStr) throws StringPrepParseException {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Notice")
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

    public native String start();
//    public native int createSocket(int x, String y);
//    public native String stringFromJNI();
//    public native int addNumbers(int x, int y);

    /**
     * A native method that is implemented by the 'native_cpp_chat_app' native library,
     * which is packaged with this application.
     */
}
