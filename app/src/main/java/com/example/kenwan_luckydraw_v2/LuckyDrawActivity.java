package com.example.kenwan_luckydraw_v2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class LuckyDrawActivity extends AppCompatActivity {

    public static ArrayList<LuckyDrawItem> itemList = new ArrayList<>();
    public static ArrayList<LuckyDrawItem> itemList2 = new ArrayList<>();
    private LuckyDrawView luckWheel;
    private int currentSector = 1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw);

        /*
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF2E2E"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("KenWan's Lucky Draw");\

         */

        luckWheel = findViewById(R.id.customWheel);
        Button spinButton = findViewById(R.id.btnSpin);

        setDefaultData();
        readDataFromDatabase();

        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int randomSector = new Random().nextInt(itemsList.size());
                //random
                LuckyDrawItem randomItem = getRandomItem(itemList);
                TextView resultView = findViewById(R.id.tvResult);

                if(randomItem != null) {
                    int randomSector = itemList.indexOf(randomItem);

                    spinWheel(randomSector);

                    currentSector = itemList.get(randomSector).getId();
                    //binding.resultTextView.setText("You won " + currentSector);
                    resultView.setText("You won " + currentSector);

                }else{
                    //binding.resultTextView.setText("Error");
                    resultView.setText("Error");
                }
            }
        });
    }

    private void spinWheel(final int selectedSector) {
        luckWheel.spinWheelToSector(itemList.get(selectedSector).getId(), currentSector);

        // Display the result after a delay (simulating the spinning animation)
        luckWheel.postDelayed(new Runnable() {
            @Override
            public void run() {
                showResult(selectedSector);
            }
        }, 2000); // 2 seconds delay, adjust as needed
    }

    private void showResult(int id) {
        // Display or handle the result as needed
        AlertDialog.Builder builder = new AlertDialog.Builder(LuckyDrawActivity.this);
        builder.setTitle("Congratulation");
        builder.setMessage("You won " + itemList.get(id).getDetails());
        builder.show();
    }

    private void setDefaultData(){

        // Assume as read from the database
        for(int i=1; i<=18; i++){
            itemList.add(new LuckyDrawItem(i, "0%", "KenWan Item " + i, 1));
        }
        /*
        DatabaseReference myRef = database.getReference("Items");
        myRef.child("Item 18").setValue(new LuckyDrawItem(18, "10%", "Extra 10% discount on all mobile accessories (except phone)", 8));
        */
    }


    private void readDataFromDatabase(){
        DatabaseReference myRef = database.getReference("Items");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                itemList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    // Get the Product object and do something with it
                    LuckyDrawItem item = productSnapshot.getValue(LuckyDrawItem.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }
                //Toast toast = Toast.makeText(getApplicationContext(), itemList2.size(), Toast.LENGTH_SHORT);
                //toast.show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast toast = Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT);
                toast.show();
            }
        });


        /*
        DatabaseReference itemRef = database.getReference("Items").child("");
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ArrayList<LuckyDrawItem> itemList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LuckyDrawItem item = snapshot.getValue(LuckyDrawItem.class);
                    if (item != null) {
                        itemList.add(item);
                        Toast toast = Toast.makeText(getApplicationContext(), item.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

         */
    }

    private LuckyDrawItem getRandomItem(ArrayList<LuckyDrawItem> list){
        ArrayList<LuckyDrawItem> tempList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list.get(i).getWeight(); j++){
                tempList.add(
                        new LuckyDrawItem(
                                list.get(i).getId(),
                                list.get(i).getName(),
                                list.get(i).getDetails(),
                                list.get(i).getWeight()
                        )
                );
            }
        }
        int random = new Random().nextInt(tempList.size());
        LuckyDrawItem finalItem = tempList.get(random);
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getId() == finalItem.getId() &&
                    list.get(i).getName().equals(finalItem.getName())){
                return list.get(i);
            }
        }
        return null;
    }
}