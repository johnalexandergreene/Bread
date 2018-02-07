package org.fleen.bread.app.forsythiaCompositionGenerator.fc0001_18hexposter;

import org.fleen.bread.app.forsythiaCompositionGenerator.head_Single.Head_Single;

public class Head extends Head_Single{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  static final int 
    EXPORTIMAGEWIDTH=9042,
    EXPORTIMAGEHEIGHT=5248;
  
  Head(Gen g){
    super(g);
    setExportDir(EXPORTDIR);
    setExportImageDimensions(EXPORTIMAGEWIDTH,EXPORTIMAGEHEIGHT);}
  
  public static final void main(String[] a){
    new Head(new Gen());}

}
