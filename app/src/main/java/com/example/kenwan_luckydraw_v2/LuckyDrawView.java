package com.example.kenwan_luckydraw_v2;

import static com.example.kenwan_luckydraw_v2.LuckyDrawActivity.itemsList;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LuckyDrawView extends View{
    private Paint sectorPaint;
    private Paint textPaint;
    private float rotationAngle = 0;

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

        float sectorAngle = 360 / itemsList.size();
        float rotateWheel = 90 + sectorAngle / 2;

        for (int i = 0; i < itemsList.size(); i++) {
            float startAngle = i * sectorAngle - (rotateWheel);

            // Set a different color for each sector
            sectorPaint.setColor(getSectorColor(i));

            // Draw the sector
            canvas.drawArc(0, 0, width, height, startAngle + rotationAngle, sectorAngle, true, sectorPaint);

            // Draw the sector label closer to the center of the sector
            drawTextOnArc(canvas, String.valueOf(itemsList.get(i).getId()), radius * 0.8, startAngle + rotationAngle + sectorAngle / 2);
        }
    }

    private void drawTextOnArc(Canvas canvas, String text, double radius, float angle) {
        int xPos = getWidth() / 2;
        int yPos = getHeight() / 2;

        float textHeight = textPaint.descent() - textPaint.ascent();

        // Calculate the position closer to the center
        float x = xPos + (float) Math.cos(Math.toRadians(angle)) * (float) (radius);
        float y = yPos + (float) Math.sin(Math.toRadians(angle)) * (float) (radius);

        canvas.drawText(text, x, y - textHeight / 2, textPaint);
    }


    private int getSectorColor(int index) {
        int[] colors = {Color.rgb(255,127,127), Color.rgb(255,201,102)};
        return colors[index % colors.length];
    }

    public void spinWheelToSector(int targetSector, int currentSector) {
        int numSectors = itemsList.size();
        int distanceSector = ( numSectors - targetSector + currentSector ) % numSectors;
        float targetRotation = distanceSector * (360f / numSectors);

        // Calculate the duration based on the total number of cycles
        int duration = (int) (360 * 3);

        float startRotation = rotationAngle;
        float finalRotation = targetRotation - startRotation;

        ValueAnimator animator = ValueAnimator.ofFloat(startRotation, startRotation + duration, finalRotation);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //rotationAngle = (startDrawLocation + targetRotation) % 360;
                rotationAngle = ((float) animation.getAnimatedValue() + (startRotation * 2));
                invalidate(); // Trigger a redraw
            }
        });
        animator.start();
    }
}
