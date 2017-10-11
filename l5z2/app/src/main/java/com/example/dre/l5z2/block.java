package com.example.dre.l5z2;


import java.io.Serializable;

public class block implements Serializable{
    public int color;
    public Vector2[] blocks;
    public block(block copy){
        this.color=copy.color;
        blocks=new Vector2[4];
        blocks[0]=new Vector2(copy.blocks[0].x,copy.blocks[0].y);
        blocks[1]=new Vector2(copy.blocks[1].x,copy.blocks[1].y);
        blocks[2]=new Vector2(copy.blocks[2].x,copy.blocks[2].y);
        blocks[3]=new Vector2(copy.blocks[3].x,copy.blocks[3].y);
    }
    public block(int x, int y, int type,int color){
        this.color=color;
        blocks=new Vector2[4];
        blocks[0]= new Vector2(x,y);
        switch(type){
            case 0:
                blocks[1]=new Vector2(x-1,y);
                blocks[2]=new Vector2(x+1,y);
                blocks[3]=new Vector2(x+2,y);
                break;
            case 1:
                blocks[1]=new Vector2(x,y+1);
                blocks[2]=new Vector2(x-1,y);
                blocks[3]=new Vector2(x-2,y);
                break;
            case 2:
                blocks[1]=new Vector2(x,y+1);
                blocks[2]=new Vector2(x+1,y);
                blocks[3]=new Vector2(x+2,y);
                break;
            case 3:
                blocks[1]=new Vector2(x+1,y);
                blocks[2]=new Vector2(x,y+1);
                blocks[3]=new Vector2(x+1,y+1);
                break;
            case 4:
                blocks[1]=new Vector2(x+1,y);
                blocks[2]=new Vector2(x,y+1);
                blocks[3]=new Vector2(x-1,y+1);
                break;
            case 5:
                blocks[1]=new Vector2(x-1,y);
                blocks[2]=new Vector2(x+1,y);
                blocks[3]=new Vector2(x,y+1);
                break;
            case 6:
                blocks[1]=new Vector2(x-1,y);
                blocks[2]=new Vector2(x,y+1);
                blocks[3]=new Vector2(x+1,y+1);
        }
    }
    public block move(int x, int y){
        block temp = new block(this);
        for (Vector2 block:temp.blocks) {
            block.x+=x;
            block.y+=y;
        }
        return temp;
    }
    public block rotate(){
        block temp = new block(this);
        for(int i = 1; i<4;i++){
            //int offsetX=blocks[i].x-blocks[0].x;
            //int offsetY=blocks[i].y-blocks[0].y;
            temp.blocks[i].y=blocks[0].y+blocks[i].x-blocks[0].x;
            temp.blocks[i].x=blocks[0].x-(blocks[i].y-blocks[0].y);
        }
        return temp;
    }
}
