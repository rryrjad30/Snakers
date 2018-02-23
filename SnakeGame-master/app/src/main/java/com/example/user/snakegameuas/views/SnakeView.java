package com.example.user.snakegameuas.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.user.snakegameuas.R;
import com.example.user.snakegameuas.enums.TileType;

/**
 * Created by USER on 2/19/2018.
 */

public class SnakeView extends View {
    private Paint mPaint= new Paint();
    private TileType snakeViewMap[][];
//    private Bitmap image;


    public SnakeView(Context context, AttributeSet attrs) {
        super(context,attrs);
//        image = BitmapFactory.decodeResource(getResources(), R.drawable.linux);
    }
    public void setSnakeViewMap (TileType[][] map){
        this.snakeViewMap=map;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(snakeViewMap != null) {
//            width untuk size lingkaran
//            ambil panjang array pertama snakeViewMap
            float tileSizeX = canvas.getWidth() / snakeViewMap.length;
//            first row untuk size lingkaran
//            ambil panjang array snakeViewMap[]
            float tileSizeY = canvas.getHeight() / snakeViewMap[0].length;
//            Math.min() return the smaller of two float values
            float circleSize = Math.min(tileSizeX,tileSizeY)/2;
            for (int x = 0; x < snakeViewMap.length; x++) {
                for (int y = 0; y < snakeViewMap[x].length; y++) {
                    switch (snakeViewMap[x][y]) {

                        case Nothing:
                            mPaint.setColor(Color.WHITE);
                            break;
                        case Wall:
                            mPaint.setColor(Color.GREEN);
                            break;
                        case SnakeHead:
                            mPaint.setColor(Color.RED);
                            break;
                        case SnakeTail:
                            mPaint.setColor(Color.GREEN);
                            break;
                        case Apple:
                            mPaint.setColor(Color.RED);
                            break;
                    }
                    canvas.drawCircle(x * tileSizeX + tileSizeX/2f+ circleSize/2,y * tileSizeY +tileSizeY/2f + circleSize/2, circleSize,mPaint);
                    System.out.println(tileSizeX/2f);
                }
            }
        }
//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(image,50,50,false );
//        canvas.drawBitmap(resizedBitmap, 0,0,null);
    }
}
