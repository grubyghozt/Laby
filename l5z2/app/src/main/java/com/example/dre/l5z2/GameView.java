package com.example.dre.l5z2;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import java.util.Random;

public class GameView extends View {
    private int score=-1;
    private Paint textPaint = new Paint();
    private Paint p = new Paint();
    private Paint border = new Paint();
    private Random generator = new Random();
    private int height=25;
    private int offset=4;
    private int width=11+2*offset;
    private int numberOfTypes=7;
    private int startingXPosition= width/2;
    private int startingYPosition=3;
    private int startingXQueuePosition=width-1-offset/2;
    private boolean lost=false;
    private int[] colors = getResources().getIntArray(R.array.blockColors);
    private boolean [][] occupiedTiles=new boolean[width][height];
    private int [][] colorTiles=new int[width][height];
    private block activeBlock;
    private block inQueue1;
    private block inQueue2;
    private block inQueue3;
    public GameView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        textPaint.setTextAlign(Paint.Align.CENTER);
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(4);
        textPaint.setColor(Color.BLUE);
        for(int i = 0; i <height;i++){
            occupiedTiles[offset][i]=true;
            occupiedTiles[width-1-offset][i]=true;
        }
        for(int i = offset; i < width-offset;i++){
            occupiedTiles[i][height-1]=true;
        }
    }
    public boolean checkMove(block temp){
        for (Vector2 block:temp.blocks) {
            if(block.y>=height || occupiedTiles[block.x][block.y]){
                return false;
            }
        }
        return true;
    }
    public boolean moveBlock(int x, int y){
        block temp = activeBlock.move(x,y);
        if(checkMove(temp)){
            activeBlock=temp;
            return true;
        }
        return false;
    }
    public void rotateBlock(){
        block temp = activeBlock.rotate();
        if(checkMove(temp)) {
            activeBlock = temp;
        }
    }
    public void spawnNewBlock(){
        if(activeBlock!=null){
            for (Vector2 block:activeBlock.blocks) {
                occupiedTiles[block.x][block.y]=true;
                colorTiles[block.x][block.y]=activeBlock.color;
            }
        }
        if(inQueue3==null){
            //System.out.println("yoyo");
            activeBlock=new block(startingXQueuePosition,startingYPosition,generator.nextInt(numberOfTypes),generator.nextInt(colors.length-1)+1);
            int rotations = generator.nextInt(4);
            for(int i = 0;i<rotations;i++){
                rotateBlock();
            }
            inQueue1=new block(activeBlock);
            activeBlock=new block(startingXQueuePosition,startingYPosition+height/3,generator.nextInt(numberOfTypes),generator.nextInt(colors.length-1)+1);
            rotations = generator.nextInt(4);
            for(int i = 0;i<rotations;i++){
                rotateBlock();
            }
            inQueue2=new block(activeBlock);
            activeBlock=new block(startingXQueuePosition,startingYPosition+height*2/3,generator.nextInt(numberOfTypes),generator.nextInt(colors.length-1)+1);
            rotations = generator.nextInt(4);
            for(int i = 0;i<rotations;i++){
                rotateBlock();
            }
            inQueue3=new block(activeBlock);
            activeBlock=new block(startingXPosition,startingYPosition,generator.nextInt(numberOfTypes),generator.nextInt(colors.length-1)+1);
            rotations = generator.nextInt(4);
            for(int i = 0;i<rotations;i++){
                rotateBlock();
            }
        }
        else {
            System.out.println("yo");
            block temp=inQueue1.move(startingXPosition-startingXQueuePosition,0);
            inQueue1=inQueue2.move(0,-height/3);
            inQueue2=inQueue3.move(0,-height/3);
            activeBlock=new block(startingXQueuePosition,startingYPosition+height*2/3,generator.nextInt(numberOfTypes),generator.nextInt(colors.length-1)+1);
            int rotations = generator.nextInt(4);
            for(int i = 0;i<rotations;i++){
                rotateBlock();
            }
            inQueue3=activeBlock;
            activeBlock = temp;
        }
        /*activeBlock=new block(startingXPosition,startingYPosition,generator.nextInt(numberOfTypes),generator.nextInt(colors.length-1)+1);
        int rotations = generator.nextInt(4);
        for(int i = 0;i<rotations;i++){
            rotateBlock();
        }*/
    }
    public void checkForLose(){
        for(int i = offset+1 ; i<width-1-offset;i++){
            if(occupiedTiles[i][startingYPosition]){
                lost=true;
                return;
            }
        }
    }
    public void checkForCompleteRow(){
        int completedRows=0;
        boolean completedRow;
        for(int i = height-1;i>=startingYPosition;i--){
            completedRow=true;
            for(int j = offset+1; j < width-1-offset;j++){
                if(!occupiedTiles[j][i]){
                    completedRow=false;
                }
                if(completedRows>0){
                    occupiedTiles[j][i+completedRows]=occupiedTiles[j][i];
                    occupiedTiles[j][i]=false;
                    colorTiles[j][i+completedRows]=colorTiles[j][i];
                    colorTiles[j][i]=0;
                }
            }
            if(completedRow){
                completedRows++;
                score++;
            }
        }
    }
    public boolean isLost(){
        return lost;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasHeight = canvas.getHeight();
        int squareSize=canvasHeight/height;
        border.setColor(Color.BLACK);
        for(int i = 0;i<width;i++){
            for(int j=0;j<height;j++){
                p.setColor(colors[colorTiles[i][j]]);
                canvas.drawRect(new RectF(squareSize*i,squareSize*j,squareSize*(i+1),squareSize*(j+1)),p);
                canvas.drawRect(new RectF(squareSize*i,squareSize*j,squareSize*(i+1),squareSize*(j+1)),border);
            }
        }
        if(activeBlock!=null){
            p.setColor(colors[activeBlock.color]);
            for (Vector2 block:activeBlock.blocks) {
                canvas.drawRect(new RectF(squareSize*block.x,squareSize*block.y,squareSize*(block.x+1),squareSize*(block.y+1)),p);
                canvas.drawRect(new RectF(squareSize*block.x,squareSize*block.y,squareSize*(block.x+1),squareSize*(block.y+1)),border);
            }

        }
        if(inQueue1!=null){
            p.setColor(colors[inQueue1.color]);
            for (Vector2 block:inQueue1.blocks) {
                canvas.drawRect(new RectF(squareSize*block.x,squareSize*block.y,squareSize*(block.x+1),squareSize*(block.y+1)),p);
                canvas.drawRect(new RectF(squareSize*block.x,squareSize*block.y,squareSize*(block.x+1),squareSize*(block.y+1)),border);
            }

        }
        if(inQueue2!=null){
            p.setColor(colors[inQueue2.color]);
            for (Vector2 block:inQueue2.blocks) {
                canvas.drawRect(new RectF(squareSize*block.x,squareSize*block.y,squareSize*(block.x+1),squareSize*(block.y+1)),p);
                canvas.drawRect(new RectF(squareSize*block.x,squareSize*block.y,squareSize*(block.x+1),squareSize*(block.y+1)),border);
            }

        }
        if(inQueue3!=null){
            p.setColor(colors[inQueue3.color]);
            for (Vector2 block:inQueue3.blocks) {
                canvas.drawRect(new RectF(squareSize*block.x,squareSize*block.y,squareSize*(block.x+1),squareSize*(block.y+1)),p);
                canvas.drawRect(new RectF(squareSize*block.x,squareSize*block.y,squareSize*(block.x+1),squareSize*(block.y+1)),border);
            }

        }
        border.setColor(Color.BLUE);
        canvas.drawRect(new RectF(squareSize*(1+offset),squareSize*startingYPosition,squareSize*(width-1-offset),squareSize*height),border);
        textPaint.setTextSize(3*squareSize/2);
        canvas.drawText(getResources().getString(R.string.score)+" "+score,canvas.getWidth()/2,3*squareSize/2,textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightDisplay = getContext().getResources().getDisplayMetrics().heightPixels*8/10;
        int squareSize=(heightDisplay)/height;
        setMeasuredDimension(width*squareSize,heightDisplay);
    }
    public void saveState(Bundle outState){
        outState.putSerializable("occupiedTiles",occupiedTiles);
        outState.putSerializable("colorTiles",colorTiles);
        outState.putSerializable("activeBlock",activeBlock);
        outState.putSerializable("inQueue1",inQueue1);
        outState.putSerializable("inQueue2",inQueue2);
        outState.putSerializable("inQueue3",inQueue3);
        outState.putInt("score",score);
    }
    public void loadState(Bundle inState){
        occupiedTiles=(boolean[][])inState.getSerializable("occupiedTiles");
        colorTiles=(int[][])inState.getSerializable("colorTiles");
        activeBlock=(block)inState.getSerializable("activeBlock");
        inQueue1=(block)inState.getSerializable("inQueue1");
        inQueue2=(block)inState.getSerializable("inQueue2");
        inQueue3=(block)inState.getSerializable("inQueue3");
        score=(int)inState.getInt("score");
    }
}
