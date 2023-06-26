package com.example.native_cpp_chat_app;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.StringPrepParseException;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;

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

        try {
            this.alertString("Hi, Yuudai Ishihara !!!");
        } catch (StringPrepParseException e) {
            throw new RuntimeException(e);
        }
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

    /**
     * A native method that is implemented by the 'native_cpp_chat_app' native library,
     * which is packaged with this application.
     */
}
