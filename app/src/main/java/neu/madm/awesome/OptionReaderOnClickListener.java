package neu.madm.awesome;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class OptionReaderOnClickListener implements View.OnClickListener {

    private final String path;
    private final DatabaseReference mDatabase;

    public OptionReaderOnClickListener(DatabaseReference mDatabase, String path) {
        this.path = path;
        this.mDatabase = mDatabase;
    }

    @Override
    public void onClick(View view) {
        DatabaseReference option = this.mDatabase.child(this.path);
        option.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Option:" , snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

}
