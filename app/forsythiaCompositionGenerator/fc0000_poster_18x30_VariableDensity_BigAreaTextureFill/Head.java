package org.fleen.bread.app.forsythiaCompositionGenerator.fc0000_poster_18x30_VariableDensity_BigAreaTextureFill;

import org.fleen.bread.app.forsythiaCompositionGenerator.head_Single.Head_Single;

public class Head extends Head_Single{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  static final int 
    EXPORTIMAGEWIDTH=9095,
    EXPORTIMAGEHEIGHT=5274,
    BORDERTHICKNESS=32;
  
  Head(Gen g){
    super(g);
    setExportDir(EXPORTDIR);
    setExportImageDimensions(EXPORTIMAGEWIDTH,EXPORTIMAGEHEIGHT);
    setExportBorderThickness(BORDERTHICKNESS);}
  
  public static final void main(String[] a){
    new Head(new Gen());}

}
