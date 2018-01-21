package org.fleen.bread.colorMap;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.util.tree.TreeNode;

/*
 * use egg tag to distinguish nesting levels
 * at each nesting level use half of the palette, randomly
 * dupe colors by sig, for symmetry
 * this looks good with strokes
 */
@SuppressWarnings("serial")
public class CM_SymmetricChaos_EggLevelTriplePaletteSplit extends HashMap<FPolygon,Color> implements ColorMap{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public CM_SymmetricChaos_EggLevelTriplePaletteSplit(ForsythiaComposition composition,Color[] palette){
    initPalettes(palette);
    doPolygonColors(composition);}
  
  /*
   * ################################
   * POLYGON COLOR SELECTION
   * ################################
   */
  
  public Color getColor(FPolygon p){
    return get(p);}
  
  private void doPolygonColors(ForsythiaComposition composition){
    Random rnd=new Random();
    Map<FPolygonSignature,Color> colorbysig=new HashMap<FPolygonSignature,Color>();
    Iterator<TreeNode> i=composition.getLeafPolygonIterator();
    FPolygon p;
    Color c;
    while(i.hasNext()){
      p=(FPolygon)i.next();
      c=getColor(p,rnd,colorbysig);
      put(p,c);}}
  
  private Color getColor(FPolygon polygon,Random rnd,Map<FPolygonSignature,Color> colorbysig){
    FPolygonSignature sig=polygon.getSignature();
    Color color=colorbysig.get(sig);
    if(color==null){
      int 
        eggdepth=getEggDepth(polygon),
        colorindex,
        di=eggdepth%3;
      if(di==0){
        colorindex=rnd.nextInt(palette0.length);
        color=palette0[colorindex];
      }else if(di==1){
        colorindex=rnd.nextInt(palette1.length);
        color=palette1[colorindex];
      }else{
        colorindex=rnd.nextInt(palette2.length);
        color=palette2[colorindex];}
      colorbysig.put(sig,color);}
    return color;}
  
  private static final String EGGTAG="egg";
  
  private int getEggDepth(TreeNode node){
    int c=0;
    TreeNode n=node;
    FPolygon p;
    while(n!=null){
      if(n instanceof FPolygon){
        p=(FPolygon)n;
        if(p.hasTags(EGGTAG))
          c++;}
      n=n.getParent();}
    return c;}
  
  /*
   * ################################
   * PALETTE
   * ################################
   */
  
  private Color[] palette0,palette1,palette2;
  
  private void initPalettes(Color[] palette){
    int a=palette.length/3;
    palette0=new Color[a];
    palette1=new Color[a];
    palette2=new Color[palette.length-a*2];
    for(int i=0;i<palette.length;i++){
      if(i<a){
        palette0[i]=palette[i];
      }else if(i>=a&&i<a*2){
        palette1[i-a]=palette[i];
      }else{
        palette2[i-a*2]=palette[i];}}
    //randomize the subpalette order
    List<Color[]> r=new ArrayList<Color[]>();
    r.add(palette0);
    r.add(palette1);
    r.add(palette2);
    Collections.shuffle(r,new Random());
    palette0=r.get(0);
    palette1=r.get(1);
    palette2=r.get(2);}

}
