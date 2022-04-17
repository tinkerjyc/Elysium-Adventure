package neu.madm.awesome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import edu.neu.madcourse.awesome.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void knifeWithBloodImageOnClick(View v) {
        AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View view = factory.inflate(R.layout.knife_with_blood, null);
        alertadd.setView(view);
        alertadd.setPositiveButton("Cancel!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        alertadd.setNegativeButton("Put into bag!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                Toast toast=Toast.makeText(getApplicationContext(),
                        "Current evidence is put inside the bag",Toast. LENGTH_SHORT);
                toast.show();
            }
        });

        alertadd.show();
    }

    public void deadBodyImageOnClick(View v) {
        AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View view = factory.inflate(R.layout.dead_body, null);
        alertadd.setView(view);
        alertadd.setPositiveButton("Cancel!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        alertadd.setNegativeButton("Examine!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                Toast toast=Toast.makeText(getApplicationContext(),
                        "Dead body is examined",Toast. LENGTH_SHORT);
                toast.show();
            }
        });

        alertadd.show();
    }

}