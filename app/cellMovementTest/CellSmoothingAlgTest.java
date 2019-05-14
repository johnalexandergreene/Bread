package org.fleen.bread.app.cellMovementTest;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class CellSmoothingAlgTest{
  
  static int WIDTH=900,HEIGHT=900;
  static double SCALE=700;
  
  public static final Color[] P_FOO=new Color[]{
      new Color(0,0,0),
      new Color(255,0,0),
      new Color(255,255,0),
      new Color(0,255,0),
      new Color(0,255,255),
      new Color(0,0,255),
      new Color(255,0,255),
      new Color(255,255,255)
    };
  
  BufferedImage getImage(){
    BufferedImage a=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
    int b;
    Color c;
    int[][] z=getCells();
    for(int x=0;x<WIDTH;x++){
      for(int y=0;y<HEIGHT;y++){
        b=z[x][y];
        c=P_FOO[b%P_FOO.length];
        a.setRGB(x,y,c.getRGB());}}
    return a;}
  
  int[][] cells=null;
  
  int[][] getCells(){
    if(cells==null){
      initCells();
      processCells();}
    return cells;}
  
  void initCells(){
    cells=new int[WIDTH][HEIGHT];
    for(int x=0;x<WIDTH;x++){
      for(int y=0;y<HEIGHT;y++){
        cells[x][y]=2;}}}
  
  static final int CELLBACKGROUND=0,CELLBOX=1;
  
  void processCells(){
    makeBlock();
    for(int i=0;i<3;i++)
      smooth();
    }
  
  void smooth(){
    int[][] newcells=new int[WIDTH][HEIGHT];
    int bcc;
    for(int x=0;x<WIDTH;x++){
      for(int y=0;y<HEIGHT;y++){
        bcc=getBoxCellCount(x,y);
        System.out.println("bcc="+bcc);
        if(bcc>RANGE3CELLS.length/2)
          newcells[x][y]=CELLBOX;
        else
          newcells[x][y]=CELLBACKGROUND;}}
    cells=newcells;}
  
  
  static final int RANGE=5;
  static final int HALFSUMINRANGE=10;
  static final int[][] RANGE3CELLS={
      {0,0},
                 {-1,3},{0,3},{1,3},
          {-2,2},{-1,2},{0,2},{1,2},{2,2},
   {-3,1},{-2,1},{-1,1},{0,1},{1,1},{2,1},{3,1},
   {-3,0},{-2,0},{-1,0},{0,0},{1,0},{2,0},{3,0},
   {-3,-1},{-2,-1},{-1,-1},{0,-1},{1,-1},{2,-1},{3,-1},
          {-2,-2},{-1,-2},{0,-2},{1,-2},{2,-2},
                  {-1,-3},{0,-3},{1,-3},
  };
  
  int getBoxCellCount(int x,int y){
    int bcc=0,x0,y0;
    for(int[] a:RANGE3CELLS){
      x0=x+a[0];
      y0=y+a[1];
      if(x0>-1&&x0<WIDTH&&y0>-1&&y0<HEIGHT)
        if(cells[x0][y0]==CELLBOX)
          bcc++;}
    return bcc;}
  
  void makeBlock(){
    for(int x=0;x<WIDTH;x++){
      for(int y=0;y<HEIGHT;y++){
        if(x>100&&x<300&&y>200&&y<400)
          cells[x][y]=CELLBOX;
        else
          cells[x][y]=CELLBACKGROUND;}}}
  
  

}
