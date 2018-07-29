package org.fleen.bread.app.cruncher;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Renderer{
  
  
  //reddened that pale pink
  public static final Color[] P_TOY_STORY_ADJUSTED2=new Color[]{
    new Color(168,67,39),
    new Color(251,206,89),
    new Color(88,184,121),
    new Color(154,94,154),
    new Color(234,61,65),
    new Color(248,237,23),
    new Color(249,139,90),
    new Color(0,146,232),
    new Color(254,178,213)};
  
  public static final Color[] GRAYSCALE=new Color[]{
      new Color(0,0,0),
      new Color(16,16,16),
      new Color(32,32,32),
      new Color(48,48,48),
      new Color(64,64,64),
      new Color(80,80,80),
      new Color(96,96,96),
      new Color(112,112,112),
      new Color(128,128,128),
      new Color(144,144,144),
      new Color(160,160,160),
      new Color(176,176,176),
      new Color(192,192,192),
      new Color(208,208,208),
      };
  
  public static final Color[] RAINBOW=new Color[]{
      new Color(251,77,77),
      new Color(251,77,134),
      new Color(251,77,192),
      new Color(251,77,249),
      new Color(192,77,251),
      new Color(134,77,251),
      new Color(77,77,251),
      new Color(77,134,251),
      new Color(77,192,251),
      new Color(77,249,251),
      new Color(77,251,192),
      new Color(77,251,77),
      new Color(192,251,77),
      new Color(249,251,77),
      new Color(251,192,77),
      new Color(251,134,77),
      new Color(251,77,77)
      };
  
  public static final Color[] SHARPIE=new Color[]{
      new Color(255,255,0),
      new Color(0,0,0),
      new Color(255,255,255)};
  
  public static final Color[] SHARPIE2=new Color[]{
      new Color(255,0,0),
      new Color(255,255,0),
      new Color(0,255,0),
      new Color(0,255,255),
      new Color(0,0,255)};
  
  public static final Color[] SHARPIE3=new Color[]{
      new Color(0,0,0),
      new Color(212,194,159)};
  
  Cruncher cruncher;
  static final int CELLSPAN=5;
  
  Color[] palette=SHARPIE3;
  
  
  Renderer(Cruncher cruncher){
    this.cruncher=cruncher;
  }
  
  BufferedImage getImage(){
    int 
      imagespan=Cruncher.GRIDSPAN*CELLSPAN,
      pixelval;
    Color c;
    BufferedImage image=new BufferedImage(imagespan,imagespan,BufferedImage.TYPE_INT_RGB);
    for(int x=0;x<imagespan;x++){
      for(int y=0;y<imagespan;y++){
        pixelval=cruncher.grid[x/CELLSPAN][y/CELLSPAN];
        c=palette[pixelval%palette.length];
        image.setRGB(x,y,c.getRGB());}}
    return image;}

}
