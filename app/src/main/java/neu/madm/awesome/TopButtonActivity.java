package neu.madm.awesome;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TopButtonActivity extends AppCompatActivity {
    private List<CharacterInfo> characterInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_button);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    public void onClickCharacterInfoButton(View view) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mCharacter = mDatabase.child("character1");
        mCharacter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CharacterInfo characterInfo = new CharacterInfo();
                for (DataSnapshot eachChildSnapshot : snapshot.getChildren()) {
                    if (eachChildSnapshot.getKey().equals("name")) {
                        characterInfo.setName((String) eachChildSnapshot.getValue());
                    } else if (eachChildSnapshot.getKey().equals("gender")) {
                        characterInfo.setGender((String) eachChildSnapshot.getValue());

                    } else {
                        Map<String, List<String>> skill = ((HashMap<String, List<String>>) eachChildSnapshot.getValue());
                        characterInfo.setSkills(skill);
                    }
                }
                characterInfoList.add(characterInfo);

                LayoutInflater layoutInflater = (LayoutInflater) TopButtonActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.activity_character_info_popup, null);

                Button closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                TextView charaInfoPopWindow = (TextView) customView.findViewById(R.id.infoPopWindowText);
                charaInfoPopWindow.setText(characterInfoList.get(0).toString());
                Log.d("Information", characterInfoList.get(0).toString());
                //Log.d("Information", characterInfoList.get(0).);

                //instantiate popup window
                PopupWindow popupWindow = new PopupWindow(customView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

                //display the popup window
                FrameLayout topBtnFrame = (FrameLayout) findViewById(R.id.topBtnFrame);
                popupWindow.showAtLocation(topBtnFrame, Gravity.CENTER, 0, 0);

                //close the popup window on button click
                closePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
        Log.d("information ", String.valueOf(characterInfoList.size()));
    }


    public void onClickInventoryButton(View view) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/inventory");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sampleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: sampleSnapshot " + sampleSnapshot.getKey() + " = " + sampleSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });
    }
}