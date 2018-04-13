package org.fleen.bread.app.forsythiaCompositionGenerator.fc0009_priscilla_2018_04_13;

import org.fleen.bread.app.forsythiaCompositionGenerator.head_Single.Head_Single;

public class Head extends Head_Single{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  static final int 
  EXPORTIMAGEWIDTH=3000,
  EXPORTIMAGEHEIGHT=2250,
  BORDERTHICKNESS=32;
  
  Head(Gen g){
    super(g);
    setExportDir(EXPORTDIR);
    setExportImageDimensions(EXPORTIMAGEWIDTH,EXPORTIMAGEHEIGHT);
    setExportBorderThickness(BORDERTHICKNESS);}
  
  public static final void main(String[] a){
    new Head(new Gen());}

}
