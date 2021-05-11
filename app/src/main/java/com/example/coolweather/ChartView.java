package com.example.coolweather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ChartView extends View {
    private int[] temperatures;

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTemperatures(int[] temperatures) {
        this.temperatures = temperatures;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        if(temperatures != null) {
            for(int x : temperatures) {
                if(x < min) {
                    min = x;
                }
                if(x > max) {
                    max = x;
                }
            }

            int step = canvas.getHeight() / (max - min);
            int lineWidth = canvas.getWidth() / (temperatures.length - 2);

            for(int i = 0; i < temperatures.length - 1; i++) {
                int x1 = (i) * lineWidth;
                int y1 = canvas.getHeight() - (temperatures[i] - min) * step;

                int x2 = (i + 1) * lineWidth;
                int y2 = canvas.getHeight() - (temperatures[i+1] - min) * step;

                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setStrokeWidth(2);

                canvas.drawLine(x1, y1, x2, y2, paint);
            }
        }
    }
}
