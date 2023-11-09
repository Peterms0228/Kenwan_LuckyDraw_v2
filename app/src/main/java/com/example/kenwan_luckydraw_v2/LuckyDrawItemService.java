package com.example.kenwan_luckydraw_v2;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class LuckyDrawItemService {
    public static ArrayList<LuckyDrawItem> databaseItemList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public ArrayList<LuckyDrawItem> readDataFromDatabase(){
        DatabaseReference myRef = database.getReference("Items");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                databaseItemList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    LuckyDrawItem item = productSnapshot.getValue(LuckyDrawItem.class);
                    if (item != null) {
                        databaseItemList.add(item);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Error
            }

        });
        return databaseItemList;
    }
}
