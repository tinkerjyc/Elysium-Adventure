package neu.madm.awesome;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
    private List<Upload> inventoryList = new ArrayList<>();
    private List<ChatLog> chatLogList = new ArrayList<>();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private LayoutInflater layoutInflater;
    private View customView;
    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_button);
        layoutInflater = (LayoutInflater) TopButtonActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Button settingBtn = (Button) findViewById(R.id.settingbtn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TopButtonActivity.this,SettingsActivity.class));
            }
        });
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

                customView = layoutInflater.inflate(R.layout.activity_character_info_popup, null);

                Button closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                TextView charaInfoPopWindow = (TextView) customView.findViewById(R.id.infoPopWindowText);
                charaInfoPopWindow.setText(characterInfoList.get(0).toString());
                Log.d("Information", characterInfoList.get(0).toString());

                //instantiate popup window
                PopupWindow popupWindow = new PopupWindow(customView, 900, 1400);

                //display the popup window
                FrameLayout topBtnFrame = (FrameLayout) findViewById(R.id.topBtnFrame);
                popupWindow.showAtLocation(topBtnFrame, Gravity.CENTER, 0, 0);

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
        DatabaseReference mInventory = mDatabase.child("inventory");
        mInventory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inventoryList.clear();
                for (DataSnapshot eachChildSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = eachChildSnapshot.getValue(Upload.class);
                    inventoryList.add(upload);
                }

                customView = layoutInflater.inflate(R.layout.activity_inventory_popup, null);

                Button closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtnInventory);
                closePopupBtn.setBackgroundColor(Color.RED);

                progressBar = customView.findViewById(R.id.progress_circle);

                mRecyclerView = customView.findViewById(R.id.recycler_view);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(TopButtonActivity.this));
                mAdapter = new ItemAdapter(TopButtonActivity.this, inventoryList);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.INVISIBLE);

                PopupWindow popupWindow = new PopupWindow(customView, 900, 1400);
                //display the popup window
                FrameLayout topBtnFrame = (FrameLayout) findViewById(R.id.topBtnFrame);
                popupWindow.showAtLocation(topBtnFrame, Gravity.CENTER, 0, 0);

                closePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void onclickChatLogbutton(View view){
        DatabaseReference mChatlog = mDatabase.child("chatlog1");
        mChatlog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatLog chatlog = new ChatLog();
                for (DataSnapshot eachChildSnapshot : snapshot.getChildren()){
                    chatlog.setContent((String) eachChildSnapshot.getValue());
                    chatLogList.add(chatlog);
                }
                customView = layoutInflater.inflate(R.layout.activity_chatlog_popup, null);
                Button closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                TextView chatlogInfoPopWindow = (TextView) customView.findViewById(R.id.infoPopWindowText);
                chatlogInfoPopWindow.setText(chatLogList.get(0).toString());
                //instantiate popup window
                PopupWindow popupWindow = new PopupWindow(customView, 900, 1400);

                //display the popup window
                FrameLayout topBtnFrame = (FrameLayout) findViewById(R.id.topBtnFrame);
                popupWindow.showAtLocation(topBtnFrame, Gravity.CENTER, 0, 0);
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
    }

}