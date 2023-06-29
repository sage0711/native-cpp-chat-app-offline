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
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();

        echoServerStart();

    }

    private int setupUI() {

        btn_msg_send = findViewById(R.id.btn_msg_send);
        box_msg_input = findViewById(R.id.box_msg_input);

        btn_msg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = box_msg_input.getText().toString();
                try {
                    sendMessage(text);
                    emptyMsgSendBox();
                } catch (StringPrepParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return 1;
    }

    private String sendMessage(String msg) throws StringPrepParseException {
        alertString(msg);
        return msg;
    }

    public int emptyMsgSendBox() {
        box_msg_input.setText("");
        return 1;
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

    //imports here
    public native String echoServerStart();
    /**
     * A native method that is implemented by the 'native_cpp_chat_app' native library,
     * which is packaged with this application.
     */
}
