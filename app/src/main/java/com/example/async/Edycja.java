package com.example.async;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class Edycja extends AppCompatActivity {

    private EditText tytulText, opisText, cenaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edycja);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tytulText = findViewById(R.id.edycjatytul);
        opisText = findViewById(R.id.edycjaopis);
        cenaText = findViewById(R.id.edycjacena);

        // Set text fields with Intent data
        tytulText.setText(getIntent().getStringExtra("title"));
        opisText.setText(getIntent().getStringExtra("description"));
        cenaText.setText(getIntent().getStringExtra("price"));

        int index = getIntent().getIntExtra("position", -1);

        // Set button click listeners
        findViewById(R.id.edycjazapisz).setOnClickListener(v -> zapisz(index));
        findViewById(R.id.edycjausun).setOnClickListener(v -> usun(index));
    }

    private void zapisz(int index) {
        try {
            JSONArray jsonArray = new JSONArray(zaladujjson());
            if (index >= 0 && index < jsonArray.length()) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                jsonObject.put("title", tytulText.getText().toString())
                        .put("description", opisText.getText().toString())
                        .put("price", cenaText.getText().toString());
            }
            zapiszjson(jsonArray.toString());
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void usun(int index) {
        try {
            JSONArray jsonArray = new JSONArray(zaladujjson());
            if (index >= 0 && index < jsonArray.length()) {
                jsonArray.remove(index);
            }
            zapiszjson(jsonArray.toString());
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String zaladujjson() throws IOException {
        StringBuilder jsonString = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("data.json")))) {
            String line;
            while ((line = reader.readLine()) != null) jsonString.append(line);
        }
        return jsonString.toString();
    }

    private void zapiszjson(String jsonString) throws IOException {
        try (FileOutputStream fos = openFileOutput("data.json", MODE_PRIVATE)) {
            fos.write(jsonString.getBytes());
        }
    }
}
