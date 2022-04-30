package neu.madm.awesome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import android.net.Uri;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Set;

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

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private ImageButton mImageButton;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    private String currName = "default";
    private String currDescription = "default";
    private Set<String> nameSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_button);
        layoutInflater = (LayoutInflater) TopButtonActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Button exitbtn = (Button) findViewById(R.id.exitbtn);
        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(TopButtonActivity.this);
                builder.setTitle("Exit");
                builder.setMessage("Do you want to exit ??");
                builder.setPositiveButton("Yes. Exit now!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference("inventory");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("inventory");
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
    private void showPopWindowThreeChoice(int activity) {
        customView = layoutInflater.inflate(activity, null);
        PopupWindow popupWindow = new PopupWindow(customView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setClippingEnabled(false);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(TopButtonActivity.this.findViewById(R.id.imageView2), Gravity.BOTTOM, 0, 0);

        if (activity == R.layout.activity_three_choice) {
            Button option1 = (Button) customView.findViewById(R.id.option1);
            option1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Do something in response to button click
                    choiceOneOnClick(v);
                }
            });

            Button option2 = (Button) customView.findViewById(R.id.option2);
            Button option3 = (Button) customView.findViewById(R.id.option3);

            //option1.setOnClickListener(new OptionReaderOnClickListener(mDatabase,"choice1/content"));
            //option2.setOnClickListener(new OptionReaderOnClickListener(mDatabase,"choice2/content"));
            //option3.setOnClickListener(new OptionReaderOnClickListener(mDatabase,"choice3/content"));
        } else {
            Button option4 = (Button) customView.findViewById(R.id.option4);
            Button option5 = (Button) customView.findViewById(R.id.option5);

            option4.setOnClickListener(new OptionReaderOnClickListener(mDatabase,"choice4/content"));
            option5.setOnClickListener(new OptionReaderOnClickListener(mDatabase,"choice5/content"));
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("popWindow disappear");
            }
        });
    }


    public void knifeWithBloodImageOnClick(View v) {
        //clickItemSelected();
        mImageUri = Uri.parse("android.resource://neu.madm.awesome/" + R.drawable.knifewithbloodbig);
        AlertDialog.Builder alertadd = new AlertDialog.Builder(TopButtonActivity.this);
        LayoutInflater factory = LayoutInflater.from(TopButtonActivity.this);
        final View view = factory.inflate(R.layout.knife_with_blood, null);
        alertadd.setView(view);
        alertadd.setTitle("This is a knife with blood");
        alertadd.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        alertadd.setNegativeButton("Put into bag", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(TopButtonActivity.this, "in progress", Toast.LENGTH_SHORT).show();
                } else {
                    currName = "knifewithblood";
                    currDescription = "This is a knife with blood";
                    TryPutIntoBag();
                }
            }
        });

        alertadd.show();
    }

    public void deadBodyImageOnClick(View v) {

        //clickItemSelected();
        mImageUri = Uri.parse("android.resource://neu.madm.awesome/" + R.drawable.deadbodybig);
        AlertDialog.Builder alertadd = new AlertDialog.Builder(TopButtonActivity.this);
        LayoutInflater factory = LayoutInflater.from(TopButtonActivity.this);
        final View view = factory.inflate(R.layout.dead_body, null);
        alertadd.setView(view);
        alertadd.setTitle("Start inference");
        alertadd.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        alertadd.setNegativeButton("Examine", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(TopButtonActivity.this, "in progress", Toast.LENGTH_SHORT).show();
                } else {
                    currName = "Conclusion";
                    currDescription = "Victim was murdered before the fire. Clean month and unburnt cloth means stopped breathing and fell down before fire.";
                    TryPutIntoBag();
                }
            }
        });

        alertadd.show();
    }

    public void choiceOneOnClick(View v) {
        //clickItemSelected();
        mImageUri = Uri.parse("android.resource://neu.madm.awesome/" + R.drawable.file);
        AlertDialog.Builder alertadd = new AlertDialog.Builder(TopButtonActivity.this);
        LayoutInflater factory = LayoutInflater.from(TopButtonActivity.this);
        final View view = factory.inflate(R.layout.document, null);
        alertadd.setView(view);
        alertadd.setTitle("The corpses mouth is clean, no ash or dust.");
        alertadd.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        alertadd.setNegativeButton("Put into bag", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(TopButtonActivity.this, "in progress", Toast.LENGTH_SHORT).show();
                } else {
                    currName = "file";
                    currDescription = "the corpse's mouth is clean, no ash or dust.";
                    TryPutIntoBag();
                }
            }
        });

        alertadd.show();
    }

    public void choiceTwoOnClick(View v) {
        //clickItemSelected();
        mImageUri = Uri.parse("android.resource://neu.madm.awesome/" + R.drawable.clothes);
        AlertDialog.Builder alertadd = new AlertDialog.Builder(TopButtonActivity.this);
        LayoutInflater factory = LayoutInflater.from(TopButtonActivity.this);
        final View view = factory.inflate(R.layout.cloth, null);
        alertadd.setView(view);
        alertadd.setTitle("A small unburned piece of cloth from behind the torso.");
        alertadd.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        alertadd.setNegativeButton("Put into bag", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(TopButtonActivity.this, "in progress", Toast.LENGTH_SHORT).show();
                } else {
                    currName = "cloth";
                    currDescription = "A small unburned piece of cloth from behind the torso.";
                    TryPutIntoBag();
                }
            }
        });

        alertadd.show();
    }

    public void choiceThreeOnClick(View v) {
        //clickItemSelected();
        mImageUri = Uri.parse("android.resource://neu.madm.awesome/" + R.drawable.gold_bracelet);
        AlertDialog.Builder alertadd = new AlertDialog.Builder(TopButtonActivity.this);
        LayoutInflater factory = LayoutInflater.from(TopButtonActivity.this);
        final View view = factory.inflate(R.layout.gold_bracelet, null);
        alertadd.setView(view);
        alertadd.setTitle("A precious gold bracelet");
        alertadd.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        alertadd.setNegativeButton("Put into bag", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(TopButtonActivity.this, "in progress", Toast.LENGTH_SHORT).show();
                } else {
                    currName = "gold bracelet";
                    currDescription = "A precious gold bracelet";
                    TryPutIntoBag();
                }
            }
        });

        alertadd.show();
    }

    public void OfficerOnClick(View v) {
        ImageView officerView = findViewById(R.id.imageView2);
        officerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindowThreeChoice(R.layout.activity_three_choice);
            }
        });
/*        mImageUri = Uri.parse("android.resource://neu.madm.awesome/" + R.drawable.officer);
        AlertDialog.Builder alertadd = new AlertDialog.Builder(TopButtonActivity.this);
        LayoutInflater factory = LayoutInflater.from(TopButtonActivity.this);
        final View view = factory.inflate(R.layout.officer, null);
        alertadd.setView(view);
        alertadd.setTitle("This is a police officer");
        alertadd.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        alertadd.setNegativeButton("Talk", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(TopButtonActivity.this, "in progress", Toast.LENGTH_SHORT).show();
                } else {
                    currName = "Officer";
                    currDescription = "This is a police officer";
                    TryPutIntoBag();
                }
            }
        });

        alertadd.show();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageButton);
        }
    }

    // Get different file extension conditions(jpg, png).
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void TryPutIntoBag()
    {
        if (mImageUri != null)
        {
            StorageReference fileReference = mStorageRef.child(currName);

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
/*                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);*/

                            Toast.makeText(TopButtonActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
/*                            Upload upload = new Upload(currName,
                                    fileReference.getDownloadUrl().toString(), currDescription);
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(currName).setValue(upload);*/

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            Upload upload = new Upload(currName, downloadUrl.toString(), currDescription);
                            //String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(currName).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TopButtonActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "No interactive component selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void clickItemSelected() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
}