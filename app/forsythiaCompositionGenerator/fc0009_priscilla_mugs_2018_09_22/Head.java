package org.fleen.bread.app.forsythiaCompositionGenerator.fc0009_priscilla_mugs_2018_09_22;

import org.fleen.bread.app.forsythiaCompositionGenerator.head_Single.Head_Single;

public class Head extends Head_Single{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  static final int 
  EXPORTIMAGEWIDTH=1050,
  EXPORTIMAGEHEIGHT=2403,
  BORDERTHICKNESS=8;
  
  Head(Gen g){
    super(g);
    setExportDir(EXPORTDIR);
    setExportImageDimensions(EXPORTIMAGEWIDTH,EXPORTIMAGEHEIGHT);
    setExportBorderThickness(BORDERTHICKNESS);}
  
  public static final void main(String[] a){
    new Head(new Gen());}

}
