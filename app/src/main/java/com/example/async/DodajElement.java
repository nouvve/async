package com.example.async;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DodajElement extends AppCompatActivity {

    private EditText tytulText, opisText, cenaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodajelement);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tytulText = findViewById(R.id.dodajtytul);
        opisText = findViewById(R.id.dodajopis);
        cenaText = findViewById(R.id.dodajcena);
        findViewById(R.id.dodajzapis).setOnClickListener(v -> zapisz());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void zapisz() {
        try {
            JSONArray jsonArray = new JSONArray(zaladujjson());
            JSONObject newItem = new JSONObject();
            newItem.put("title", tytulText.getText().toString());
            newItem.put("description", opisText.getText().toString());
            newItem.put("price", cenaText.getText().toString());
            jsonArray.put(newItem);
            zapiszjson(jsonArray.toString());
            finish();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String zaladujjson() throws IOException {
        InputStream inputStream = openFileInput("data.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) jsonString.append(line);
        return jsonString.toString();
    }

    private void zapiszjson(String jsonString) throws IOException {
        try (FileOutputStream fos = openFileOutput("data.json", MODE_PRIVATE)) {
            fos.write(jsonString.getBytes());
        }
    }
}
