package de.emptyapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button button1;
    private Button button2, button3, button4;

    // Request code for creating a PDF document.
    private static final int CREATE_FILE = 1;
    // Request code for selecting a PDF document.
    private static final int PICK_PDF_FILE = 2;
    // Request code for selecting a txt document.
    private static final int PICK_TXT_FILE = 3;

    Context context1; // wird für read a file from uri benötigt

    // ### bessere version da startActivityForResult deprecated ist

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        this.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Button1 gedrückt", Toast.LENGTH_SHORT).show();

                // https://developer.android.com/training/data-storage/shared/documents-files
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");

                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
                boolean pickerInitialUri = false;
                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
                startActivityForResult(intent, PICK_PDF_FILE);

            }
        });

        this.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Button2 gedrückt", Toast.LENGTH_SHORT).show();

                context1 = button2.getContext(); // Context context1;
                // wird für read a file from uri benötigt
                // https://developer.android.com/training/data-storage/shared/documents-files
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
                boolean pickerInitialUri = false;
                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
                // deprecated startActivityForResult(intent, PICK_TXT_FILE);
                fileChooserActivityResultLauncher.launch(intent);
            }
        });

        this.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Button3 gedrückt", Toast.LENGTH_SHORT).show();
                context1 = button3.getContext(); // Context context1;
                // wird für read a file from uri benötigt
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
                //boolean pickerInitialUri = false;
                //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
                intent.putExtra(Intent.EXTRA_TITLE, "pwm.txt");
                // deprecated startActivityForResult(intent, PICK_TXT_FILE);
                fileSaverActivityResultLauncher.launch(intent);

            }
        });

        this.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Button4 gedrückt", Toast.LENGTH_SHORT).show();
                context1 = button4.getContext(); // Context context1;
                // wird für read a file from uri benötigt
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
                //boolean pickerInitialUri = false;
                //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
                intent.putExtra(Intent.EXTRA_TITLE, "pwm3.txt");
                // deprecated startActivityForResult(intent, PICK_TXT_FILE);
                fileBinarySaverActivityResultLauncher.launch(intent);

            }
        });

    }

    ActivityResultLauncher<Intent> fileChooserActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent resultData = result.getData();
                        // The result data contains a URI for the document or directory that
                        // the user selected.
                        Uri uri = null;
                        if (resultData != null) {
                            uri = resultData.getData();
                            // Perform operations on the document using its URI.
                            // todo remove print
                            System.out.println("URI: " + uri);
                            try {
                                String fileContent = readTextFromUri(uri);
                                System.out.println("fileContent: \n" + fileContent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> fileSaverActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent resultData = result.getData();
                        // The result data contains a URI for the document or directory that
                        // the user selected.
                        Uri uri = null;
                        if (resultData != null) {
                            uri = resultData.getData();
                            // Perform operations on the document using its URI.
                            // todo remove print
                            System.out.println("URI: " + uri);
                            try {
                                String fileContent = "the lazy dog";
                                writeTextToUri(uri, fileContent);
                                System.out.println("fileContent written: \n" + fileContent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> fileBinarySaverActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent resultData = result.getData();
                        // The result data contains a URI for the document or directory that
                        // the user selected.
                        Uri uri = null;
                        if (resultData != null) {
                            uri = resultData.getData();
                            // Perform operations on the document using its URI.
                            // todo remove print
                            System.out.println("URI: " + uri);
                            try {
                                byte[] fileContent = new byte[25];
                                SecureRandom secureRandom = new SecureRandom();
                                secureRandom.nextBytes(fileContent);
                                writeByteToUri(uri, fileContent);
                                System.out.println("fileContent written: \n" + new String(fileContent));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_TXT_FILE
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                // Perform operations on the document using its URI.
                System.out.println("URI: " + uri);
                try {
                    String fileContent = readTextFromUri(uri);
                    System.out.println("fileContent: \n" + fileContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String readTextFromUri(Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        //try (InputStream inputStream = getContentResolver().openInputStream(uri);
        // achtung: context1 muss gefüllt sein !
        try (InputStream inputStream = context1.getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        }
        return stringBuilder.toString();
    }

    private void writeTextToUri(Uri uri, String data) throws IOException {
        try {
            //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context1.getContentResolver().openOutputStream(uri));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            System.out.println("Exception File write failed: " + e.toString());
        }
    }

    private void writeByteToUri(Uri uri, byte[] data) throws IOException {
        try {
            OutputStream os = getContentResolver().openOutputStream(uri);
            if (os != null) {
                os.write(data);
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}