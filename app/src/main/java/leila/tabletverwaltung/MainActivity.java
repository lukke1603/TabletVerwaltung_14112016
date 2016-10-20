package leila.tabletverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private FrameLayout flGeraete;
    private FrameLayout flEinstellungen;
    private FrameLayout flEinlesen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        flGeraete = (FrameLayout)findViewById(R.id.flGeraete);
        flEinstellungen = (FrameLayout)findViewById(R.id.flEinstellungen);
        flEinlesen = (FrameLayout)findViewById(R.id.flEinlesen);

        flEinlesen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ReaderActivity.class);
                startActivity(i);
            }
        });
    }
}
