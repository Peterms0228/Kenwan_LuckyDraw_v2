package com.example.kenwan_luckydraw_v2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class LuckyDrawActivity extends AppCompatActivity {

    public static ArrayList<LuckyDrawItem> itemsList = new ArrayList<>();
    private LuckyDrawView luckWheel;
    private int currentSector = 1;

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

        preSetData();

        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int randomSector = new Random().nextInt(itemsList.size());
                //random
                LuckyDrawItem randomItem = getRandomItem(itemsList);
                TextView resultView = findViewById(R.id.tvResult);

                if(randomItem != null) {
                    int randomSector = itemsList.indexOf(randomItem);

                    spinWheel(randomSector);

                    currentSector = itemsList.get(randomSector).getId();
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
        luckWheel.spinWheelToSector(itemsList.get(selectedSector).getId(), currentSector);

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
        builder.setMessage("You won Item " + itemsList.get(id).getId() + "\n" +
                "Your prize is "+ itemsList.get(id).getName());
        builder.show();
    }

    private void preSetData(){

        // Assume as read from the database
        for(int i=1; i<=18; i++){
            itemsList.add(new LuckyDrawItem(i, "KenWan Item " + i, 1));
        }
    }

    private LuckyDrawItem getRandomItem(ArrayList<LuckyDrawItem> list){
        ArrayList<LuckyDrawItem> tempList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list.get(i).getWeight(); j++){
                tempList.add(
                        new LuckyDrawItem(
                                list.get(i).getId(),
                                list.get(i).getName(),
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