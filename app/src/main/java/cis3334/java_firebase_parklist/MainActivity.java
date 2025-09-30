package cis3334.java_firebase_parklist;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
// Import your R file, e.g.:
// import cis3334.java_firebase_parklist.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The 'enableEdgeToEdge()' equivalent is often handled by themes
        // or can be manually set using WindowManager flags if specifically needed.
        // For simplicity, we'll focus on basic content setting.

        setContentView(R.layout.activity_main); // Make sure R is imported correctly

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigation is now handled by the NavHostFragment defined in activity_main.xml
        // and the navigation graph res/navigation/nav_graph.xml
    }
}
