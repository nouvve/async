package com.example.async;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button pokazwszystkie = findViewById(R.id.pokazwszystkie);
        pokazwszystkie.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Lista.class);
            startActivity(intent);
        });

        Button dodajNowyElement = findViewById(R.id.dodajnowyelement);
        dodajNowyElement.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DodajElement.class);
            startActivity(intent);
        });

        File file = new File(getFilesDir(), "data.json");
        if (!file.exists()) {
            String jsonData = "[]";
            try (FileOutputStream fos = openFileOutput("data.json", MODE_PRIVATE)) {
                fos.write(jsonData.getBytes());
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}