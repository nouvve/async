package com.example.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lista extends AppCompatActivity {

    private ListView listView;
    private Handler handler = new Handler();
    private List<Map<String, String>> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.lista);
        startPeriodicRefresh();
    }

    private void startPeriodicRefresh() {
        handler.postDelayed(() -> {
            loadJsonData();
            handler.postDelayed(this::startPeriodicRefresh, 1000);
        }, 1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadJsonData() {
        new LoadJsonTask().execute();
    }

    private class LoadJsonTask extends AsyncTask<Void, Void, List<Map<String, String>>> {
        @Override
        protected List<Map<String, String>> doInBackground(Void... voids) {
            List<Map<String, String>> newItemList = new ArrayList<>();
            try (InputStream inputStream = openFileInput("data.json");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                StringBuilder jsonString = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) jsonString.append(line);

                JSONArray jsonArray = new JSONArray(jsonString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Map<String, String> item = new HashMap<>();
                    item.put("title", jsonObject.getString("title"));
                    item.put("description", jsonObject.getString("description"));
                    item.put("price", jsonObject.getString("price"));
                    newItemList.add(item);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return newItemList;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> newItemList) {
            itemList = newItemList;
            updateListView();
        }
    }

    private void updateListView() {
        SimpleAdapter adapter = new SimpleAdapter(this, itemList, R.layout.element,
                new String[]{"title", "description", "price"},
                new int[]{R.id.title, R.id.description, R.id.price});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Map<String, String> selectedItem = itemList.get(position);
            Intent intent = new Intent(Lista.this, Edycja.class);
            intent.putExtra("title", selectedItem.get("title"));
            intent.putExtra("description", selectedItem.get("description"));
            intent.putExtra("price", selectedItem.get("price"));
            intent.putExtra("position", position);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                itemList.get(position).put("title", data.getStringExtra("updatedTitle"));
                itemList.get(position).put("description", data.getStringExtra("updatedDescription"));
                itemList.get(position).put("price", data.getStringExtra("updatedPrice"));
                updateListView();
                Toast.makeText(this, "Element zaktualizowany!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
