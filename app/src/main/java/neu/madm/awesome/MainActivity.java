package neu.madm.awesome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private ImageButton mImageButton;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    private String currName = "default";
    private String currDescription = "default";
    private TextView mTextViewOpenBag;
    private Set<String> nameSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progress_bar);
        mTextViewOpenBag = findViewById(R.id.open_bag);
        mTextViewOpenBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBagActivity();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference("inventory");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("inventory");
    }

    public void knifeWithBloodImageOnClick(View v) {
        //clickItemSelected();
        mImageUri = Uri.parse("android.resource://neu.madm.awesome/" + R.drawable.knifewithbloodbig);
        AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
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
                    Toast.makeText(MainActivity.this, "in progress", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View view = factory.inflate(R.layout.dead_body, null);
        alertadd.setView(view);
        alertadd.setTitle("This is a dead body");
        alertadd.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        alertadd.setNegativeButton("Examine", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(MainActivity.this, "in progress", Toast.LENGTH_SHORT).show();
                } else {
                    currName = "DeadBody";
                    currDescription = "This is a dead body";
                    TryPutIntoBag();
                }
            }
        });

        alertadd.show();
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
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "No interactive component selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openBagActivity() {
        Intent intent = new Intent(this, BagActivity.class);
        startActivity(intent);
    }

    private void clickItemSelected() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
}