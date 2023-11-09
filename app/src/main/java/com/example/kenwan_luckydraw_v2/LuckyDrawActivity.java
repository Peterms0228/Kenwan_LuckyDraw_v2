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
    LuckyDrawItemService luckyDrawItemService = new LuckyDrawItemService();
    public static ArrayList<LuckyDrawItem> itemList = new ArrayList<>();
    private LuckyDrawView luckWheel;
    private int currentSector = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw);

        luckWheel = findViewById(R.id.customWheel);
        Button spinButton = findViewById(R.id.btnSpin);

        //get data
        itemList = luckyDrawItemService.readDataFromDatabase();

        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LuckyDrawItem randomItem = getRandomItem(itemList);
                TextView resultView = findViewById(R.id.tvResult);

                if(randomItem != null) {
                    int randomSector = itemList.indexOf(randomItem);

                    spinWheel(randomSector);

                    currentSector = itemList.get(randomSector).getId();
                    resultView.setText("You won " + currentSector);

                }else{
                    resultView.setText("Error");
                }
            }
        });
    }

    private void spinWheel(final int selectedSector) {
        luckWheel.spinWheelToSector(itemList.get(selectedSector).getId(), currentSector);

        // Display the result after 2 seconds
        luckWheel.postDelayed(new Runnable() {
            @Override
            public void run() {
                showResult(selectedSector);
            }
        }, 2000);
    }

    private void showResult(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LuckyDrawActivity.this);
        builder.setTitle("Congratulation");
        builder.setMessage("You won " + itemList.get(id).getDetails());
        builder.show();
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