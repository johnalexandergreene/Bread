package org.fleen.bread.app.forsythiaCompositionGenerator.fc0011_ChunkPortraitsFor9x12Frame;

import org.fleen.bread.app.forsythiaCompositionGenerator.head_Single.Head_Single;

/*
 * Make a big poster
 * Find nice chunks
 * Cut them out
 * Give them nice backgrounds cropped to 9x12 + 3" of slack on all sides so 15x18
 * We use a double size 18x30 poster export image
 */
public class Head extends Head_Single{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  static final int 
  EXPORTIMAGEWIDTH=18190,
  EXPORTIMAGEHEIGHT=10548,
  BORDERTHICKNESS=32;
  
  Head(Gen g){
    super(g);
    setExportDir(EXPORTDIR);
    setExportImageDimensions(EXPORTIMAGEWIDTH,EXPORTIMAGEHEIGHT);
    setExportBorderThickness(BORDERTHICKNESS);}
  
  public static final void main(String[] a){
    new Head(new Gen());}

}
