package com.example.kenwan_luckydraw_v2;

import static com.example.kenwan_luckydraw_v2.LuckyDrawActivity.itemList;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LuckyDrawView extends View{
    private Paint sectorPaint;
    private Paint textPaint;
    private float rotationAngle = 0;
    private Map<String, Integer> valueMap = new HashMap<>();
    private int colorMapIndex = -1;

    public LuckyDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        sectorPaint = new Paint();
        sectorPaint.setStyle(Paint.Style.FILL);
        sectorPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(0xFFFFFFFF); // White color for text
        textPaint.setTextSize(30); // Adjust text size as needed
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 2;

        float sectorAngle = 360;
        if(itemList.size() != 0) {
            Collections.sort(itemList, new LuckyDrawItemComparator());
            sectorAngle = 360 / itemList.size();
            float rotateWheel = 90 + sectorAngle / 2;

            for (int i = 0; i < itemList.size(); i++) {
                float startAngle = i * sectorAngle - (rotateWheel);

                // Set color of sector
                sectorPaint.setColor(getSectorColor(itemList.get(i).getName()));

                // Draw sector
                canvas.drawArc(0, 0, width, height, startAngle + rotationAngle, sectorAngle, true, sectorPaint);

                // Draw Text on Circle
                drawTextOnArc(canvas, String.valueOf(itemList.get(i).getName()), radius * 0.8, startAngle + rotationAngle + sectorAngle / 2);
            }
        }else{
            sectorPaint.setColor(getSectorColor(0));
            canvas.drawArc(0, 0, width, height, 0, sectorAngle, true, sectorPaint);
        }
    }

    private void drawTextOnArc(Canvas canvas, String text, double radius, float angle) {
        int xPos = getWidth() / 2;
        int yPos = getHeight() / 2;

        float textHeight = textPaint.descent() - textPaint.ascent();

        float x = xPos + (float) Math.cos(Math.toRadians(angle)) * (float) (radius);
        float y = yPos + (float) Math.sin(Math.toRadians(angle)) * (float) (radius);

        canvas.drawText(text, x, y - textHeight / 2, textPaint);
    }


    private int getSectorColor(int index) { //set color by index

        int[] colors = {Color.rgb(255,0,0),
                Color.rgb(255,69,0),
                Color.rgb(255,99,71),
                Color.rgb(250,128,114)};
        return colors[index % colors.length];
    }

    private int getSectorColor(String value) { //set color by item name

        int[] colors = {Color.rgb(255,0,0),
                Color.rgb(255,69,0),
                Color.rgb(255,99,71),
                Color.rgb(250,128,114)};

        if (valueMap.containsKey(value)) {
            return colors[valueMap.get(value)];
        }else{
            colorMapIndex++;
            if(colorMapIndex >= colors.length){
                colorMapIndex = 0;
            }
            valueMap.put(value, colorMapIndex);
            return colors[colorMapIndex];
        }
    }

    public void spinWheelToSector(int targetSector, int currentSector) {
        int numSectors = itemList.size();
        int distanceSector = ( numSectors - targetSector + currentSector ) % numSectors;
        float targetRotation = distanceSector * (360f / numSectors);

        int duration = (int) (360 * 3);

        float startRotation = rotationAngle;
        float finalRotation = targetRotation - startRotation;

        ValueAnimator animator = ValueAnimator.ofFloat(startRotation, startRotation + duration, finalRotation);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotationAngle = ((float) animation.getAnimatedValue() + (startRotation * 2));
                invalidate();
            }
        });
        animator.start();
    }
}
