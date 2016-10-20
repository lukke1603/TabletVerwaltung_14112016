package leila.tabletverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout flGeraete;
    private RelativeLayout flEinstellungen;
    private RelativeLayout flEinlesen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        flGeraete = (RelativeLayout) findViewById(R.id.flGeraete);
        flEinstellungen = (RelativeLayout) findViewById(R.id.flEinstellungen);
        flEinlesen = (RelativeLayout) findViewById(R.id.flEinlesen);

        flEinlesen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ReaderActivity.class);
                startActivity(i);
            }
        });
    }
}
