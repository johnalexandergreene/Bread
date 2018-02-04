package org.fleen.bread.app.forsythiaCompositionGenerator.fc0000_18x30poster;

import org.fleen.bread.app.forsythiaCompositionGenerator.Head0;

public class Head extends Head0{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  
  Head(Gen g){
    super(g);
    setExportDir(EXPORTDIR);}
  
  public static final void main(String[] a){
    new Head(new Gen());}

}
