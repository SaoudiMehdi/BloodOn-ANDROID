package stock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodon.R;

public class MainActivityStock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stock);
        Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivityStock.this, List.class);
        startActivity(intent);
    }

}
