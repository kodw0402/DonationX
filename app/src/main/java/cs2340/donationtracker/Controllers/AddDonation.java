package cs2340.donationtracker.Controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cs2340.donationtracker.Model.Category;
import cs2340.donationtracker.Model.CurrentItems;
import cs2340.donationtracker.Model.CurrentUser;
import cs2340.donationtracker.Model.ItemInfo;
import cs2340.donationtracker.Model.User_type;
import cs2340.donationtracker.R;

/**
 * implementation of add donation function
 */
@SuppressWarnings({"FieldCanBeLocal", "CyclicClassDependency"})
public class AddDonation extends AppCompatActivity {

    private final List<Category> categoryList = Arrays.asList(Category.values());

    private Spinner locationSpinner;
    private Spinner categorySpinner;
    private EditText shortDes;
    private EditText fullDes;
    private EditText value;
    private EditText comments;
    private ImageView imageView;

    private ItemInfo itemInfo;
    private Uri filePath;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donation);

        initSpinners();
        buildSpinners();
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCameraCrop();
            }
        });

        Button button = findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings({"LawOfDemeter", "ChainedMethodCall"})
            @Override
            public void onClick(View v) {
                getTexts();
                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat(
                        "yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
                itemInfo.setTimeStamp(timeStamp);
                uploadFile();
                addItemIntoFirebase(timeStamp);
                CurrentItems.getInstance().getItemList().add(itemInfo);
                Toast.makeText(AddDonation.this,
                        "Donation Item was made successfully", Toast.LENGTH_SHORT).show();
                goToNextView();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                filePath = data.getParcelableExtra("albumURI");
                imageView.setImageURI(filePath);
            }
        }
    }
    @SuppressWarnings({"SpellCheckingInspection", "ChainedMethodCall"})
    private void uploadFile() {
        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyyMMHH_mmss");
            Date now = new Date();
            final String filename = formatter.format(now) + ".jpeg";
            itemInfo.setImageName(filename);

            StorageReference storageRef = storage.getReferenceFromUrl(
                    "gs://donation-tracker-56b.appspot.com").child("images/" + filename);

            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("ChainedMethodCall")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @SuppressWarnings("ChainedMethodCall")
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploading failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /
                                    taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Choose a file first.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method takes user to CameraCropActivity class where user can crop their picture.
     */
    private void goToCameraCrop() {
        Intent intent = new Intent(this, CameraCropActivity.class);
        startActivityForResult(intent, 1);
    }

    private void goToNextView() {
        Intent intent = new Intent(this, MainApplication.class);
        startActivity(intent);
    }

    /**
     * This This method takes users to MainApplication display.
     * @param v an object class of View class
     */
    @SuppressWarnings("unused")
    public void cancel(View v) {
        Intent intent = new Intent(this, MainApplication.class);
        startActivity(intent);
    }

    @SuppressWarnings({"SpellCheckingInspection", "ChainedMethodCall"})
    private void addItemIntoFirebase(String timeStamp) {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("item").child(timeStamp).setValue(itemInfo);
    }

    @SuppressWarnings("FeatureEnvy")
    private void initSpinners() {
        locationSpinner = findViewById(R.id.locationSpinner);
        categorySpinner = findViewById(R.id.categorySpinner);

        itemInfo = new ItemInfo("",0,null,"","",null,"","","");

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    itemInfo.setLocationData(Location.locationList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemInfo.setLocationData(Location.locationList.get(0));
            }
        });
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemInfo.setCategory(categoryList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemInfo.setCategory(categoryList.get(0));
            }
        });
    }
    @SuppressWarnings({"unchecked", "SpellCheckingInspection", "FeatureEnvy", "LawOfDemeter",
            "ChainedMethodCall"})
    private void buildSpinners() {
        if (CurrentUser.getInstance().getUserType() == User_type.LOCATION_EMPLOYEE) {
            List list = new LinkedList();
            list.add(CurrentUser.getInstance().getLocationData());
            ArrayAdapter locationAdapter = new ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, list);
            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(locationAdapter);
        } else {
            ArrayAdapter locationAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, Location.locationList);
            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(locationAdapter);
        }
        ArrayAdapter categroryAdapter = new ArrayAdapter<>(
                this,android.R.layout.simple_spinner_item, categoryList);
        categroryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categroryAdapter);
    }
    @SuppressWarnings({"FeatureEnvy", "ChainedMethodCall"})
    private void getTexts() {
        shortDes = findViewById(R.id.sDescriptionText_value);
        fullDes = findViewById(R.id.fDescriptionText_value);
        value = findViewById(R.id.valueText_value);
        comments = findViewById(R.id.commentsText_value);

        itemInfo.setShortDescription(shortDes.getText().toString());
        itemInfo.setFullDescription(fullDes.getText().toString());
        itemInfo.setValue(Integer.parseInt(value.getText().toString()));
        itemInfo.setComments(comments.getText().toString());
    }
}