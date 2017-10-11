package com.example.dre.l5z1;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class game extends View {
    Paint playerColor=new Paint();
    int playerTop=80;
    int playerBottom =30;
    int playerSize=200;
    int playerPosition =0;
    int roundCorners =20;
    int playerMovingDirection=0;
    boolean lost=false;

    Paint ballColor=new Paint();
    int ballYPosition=0;
    int ballXPosition=0;
    int ballXDirection=0;
    int ballYDirection=-1;
    int radius=25;

    Paint brick1color = new Paint();
    Paint brick2color = new Paint();
    Paint brick3color = new Paint();
    Paint brick4color = new Paint();
    Paint[]brickColors;
    Paint brickBorder = new Paint();
    int brickHeight=80;
    int numOfBricks=8;
    boolean[][] destroyedBricks = new boolean[4][numOfBricks];


    public game(Context context, AttributeSet attrs){
        super(context, attrs);
        playerColor.setARGB(255,192,192,192);

        ballColor.setARGB(255,150,30,15);

        brick1color.setARGB(255,200,180,15);
        brick2color.setARGB(255,0,255,60);
        brick3color.setARGB(255,0,255,255);
        brick4color.setARGB(255,255,0,180);
        brickColors= new Paint[4];
        brickColors[0]=brick1color;
        brickColors[1]=brick2color;
        brickColors[2]=brick3color;
        brickColors[3]=brick4color;
        brickBorder.setStyle(Paint.Style.STROKE);
        brickBorder.setARGB(255,0,0,0);
    }
    public void setPlayerMovement(int direction){
        this.playerMovingDirection=direction;
    }
    public boolean isLost(){
        return lost;
    }
    public void reset(){
        for(int i=0;i<4;i++) {
            for (int j = 0; j < numOfBricks; j++) {
                destroyedBricks[i][j]=false;
            }
        }
        playerPosition=0;
        playerMovingDirection=0;
        lost=false;
        ballYPosition=0;
        ballXPosition=0;
        ballXDirection=0;
        ballYDirection=-1;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        playerPosition+=playerMovingDirection*10;
        if(playerPosition<=-width/2+playerSize){
            playerPosition=-width/2+playerSize;
        }
        else if(playerPosition>=width/2-playerSize){
            playerPosition=width/2-playerSize;
        }
        int playerLeft=width/2-playerSize+ playerPosition;
        int playerRight=width/2+playerSize+ playerPosition;
        canvas.drawRoundRect(new RectF(playerLeft,height- playerTop,playerRight,height- playerBottom), roundCorners, roundCorners, playerColor);

        ballYPosition+=ballYDirection*10;
        ballXPosition+=ballXDirection*10;
        int ballX=width/2+ ballXPosition;
        int ballY=height-playerTop-radius+ ballYPosition;
        if(ballX<radius || ballX>width-radius){
            ballXDirection=-ballXDirection;
        }
        if(ballY<radius){
            ballYDirection=1;
        }
        else if(ballY>height-radius){
            lost=true;
        }
        else if(ballY<height-(playerBottom+playerTop)/2 && ballY>=height-playerTop && ballX>=playerLeft && ballX<=playerRight){
            ballYDirection=-1;
            ballXDirection+=playerMovingDirection;
        }
        if(ballY-radius<4*brickHeight){
            int top = (ballY-radius)/brickHeight;
            int bot = (ballY+radius)/brickHeight;
            int left = (ballX-radius)/(width/numOfBricks);
            int right = (ballX+radius)/(width/numOfBricks);
            if(bot>=4){
                bot=3;
            }
            if(right>=numOfBricks){
                right=numOfBricks-1;
            }
            if((ballY-radius)/(float)brickHeight-top>0.85 || (ballY+radius)/(float)brickHeight-bot<0.15)
            for(int i = left; i <= right; i++){
                if(!destroyedBricks[top][i]){
                    destroyedBricks[top][i]=true;
                    ballYDirection=1;
                }
                if(!destroyedBricks[bot][i]){
                    destroyedBricks[bot][i]=true;
                    ballYDirection=-1;
                }
            }
            for(int i = bot; i <= top; i++){
                if(!destroyedBricks[i][left]){
                    destroyedBricks[i][left]=true;
                    ballXDirection=-ballXDirection;
                }
                if(!destroyedBricks[i][right]){
                    destroyedBricks[i][right]=true;
                    ballXDirection=-ballXDirection;
                }
            }
        }

        canvas.drawCircle(ballX,ballY,radius,ballColor);
        for(int i=0;i<4;i++){
            for(int j=0;j<numOfBricks;j++){
                if(!destroyedBricks[i][j]) {
                    canvas.drawRect(new RectF(j * width / numOfBricks, i * brickHeight, (j + 1) * width / numOfBricks, (i + 1) * brickHeight), brickColors[i]);
                    canvas.drawRect(new RectF(j * width / numOfBricks, i * brickHeight, (j + 1) * width / numOfBricks, (i + 1) * brickHeight), brickBorder);
                }
            }
        }
        for(int i=0;i<4;i++) {
            for (int j = 0; j < numOfBricks; j++) {
                if(!destroyedBricks[i][j]){
                    return;
                }
            }
        }
        lost=true;
    }
}
