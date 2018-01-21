package org.fleen.bread.app.longGarden.stripeChain;

import java.util.List;
import java.util.Random;

import org.fleen.bread.colorMap.CM_SymmetricChaos_EggLevelDoublePaletteSplit;
import org.fleen.bread.colorMap.ColorMap;
import org.fleen.bread.composer.Composer;
import org.fleen.bread.composer.Composer002_SplitBoil_WithALittleNoiseNearTheRoot;
import org.fleen.forsythia.core.composition.FGridRoot;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KPolygon;

/*
 * A single node in a chain of nodes
 * connects to 0..2 other nodes, prior and next
 * refers to a ForsythiaComposition with a rectangular root.
 * 
 */
public class Stripe_Composition implements Stripe{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Stripe_Composition(StripeChain stripechain){
    this.stripechain=stripechain;
    initComposition();
    initColorMap();}
  
  /*
   * ################################
   * STRIPECHAIN
   * The stripe chain that this stripe is a part of
   * ################################
   */
  
  StripeChain stripechain;
  
  /*
   * ################################
   * COMPOSITION
   * The rectangular forsythia composition that defines the geometry of this stripe.
   *   Ie, the proportions of the rectangle and the internal geometry
   * Get a random rectangular metagon
   * Get a random anchor
   * From those create a polygon
   * use that as the root polygon for a composition
   * compose nice geometry for that composition 
   * ################################
   */
  
  public ForsythiaComposition composition;
  
  private void initComposition(){
    //get the root polygon
    FMetagon rootmetagon=stripechain.fg.lg.config.getRandomRectangularMetagon();
    KPolygon p0=rootmetagon.getPolygon();
    List<KAnchor> anchors=rootmetagon.getAnchorOptions(p0);
    KAnchor a=anchors.get(new Random().nextInt(anchors.size()));
    FPolygon rootpolygon=new FPolygon(rootmetagon,a);
    //grid
    FGridRoot rootgrid=new FGridRoot();
    //create the composition and compose it up
    composition=new ForsythiaComposition();
    composition.setGrammar(stripechain.fg.lg.config.getGrammar());
    composition.initTree(rootgrid,rootpolygon);
    Composer composer=new Composer002_SplitBoil_WithALittleNoiseNearTheRoot();
    composer.compose(composition,getScaledDetailLimit(rootpolygon));}
  
  /*
   * The unscaled height of our rectangular compositions varies, 
   *   therefore the detail limit varies, 
   *   therefore it needs to be scaled accordingly.
   */
  private double getScaledDetailLimit(FPolygon polygon){
    return stripechain.fg.lg.config.getDetailLimit()*polygon.getDPolygon().getBounds().height;}
  
  /*
   * ################################
   * COLOR MAP
   * Colors mapped to composition leaf polygons 
   * ################################
   */
  
  public ColorMap colormap;
  
  private void initColorMap(){
    colormap=new CM_SymmetricChaos_EggLevelDoublePaletteSplit(composition,stripechain.fg.lg.config.getPolygonPalette());}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public double getImageScale(){
    double a=composition.getRootPolygon().getDPolygon().getBounds().height;
    double b=stripechain.fg.lg.ui.getViewport().getHeight()/a;
    return b;}
  
  public int getX(){
    int x=0;
    for(Stripe s:stripechain){
      if(s==this)
        return x;
      x+=s.getWidth();}
    return x;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * WIDTH
   * ++++++++++++++++++++++++++++++++
   */
  
  private int width=-1;
  
  public int getWidth(){
    if(width==-1)
      initWidth();
    return width;}
  
  private void initWidth(){
    double
      s=getImageScale(),
      w=composition.getRootPolygon().getDPolygon().getBounds().width;
    width=(int)(s*w);}
    
}
