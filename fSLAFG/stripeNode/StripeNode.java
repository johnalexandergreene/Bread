package org.fleen.bread.fSLAFG.stripeNode;

import java.util.List;
import java.util.Random;

import org.fleen.bread.fSLAFG.FSLAFG;
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
public class StripeNode{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public StripeNode(FSLAFG generator){
    this.generator=generator;
    initComposition();}
  
  /*
   * ################################
   * GENERATOR
   * The forsythia spinner looping animation frame generator 
   *   associated with this stripe node
   * ################################
   */
  
  FSLAFG generator;
  
  /*
   * ################################
   * LINKED NODES IN CHAIN
   * ################################
   */
  
  public StripeNode 
    prior=null,
    next=null;
  
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
    FMetagon rootmetagon=generator.getRandomRectangularMetagon();
    KPolygon p0=rootmetagon.getPolygon();
    List<KAnchor> anchors=rootmetagon.getAnchorOptions(p0);
    KAnchor a=anchors.get(new Random().nextInt(anchors.size()));
    FPolygon rootpolygon=new FPolygon(rootmetagon,a);
    //grid
    FGridRoot rootgrid=new FGridRoot();
    //create the composition and compose it up
    composition=new ForsythiaComposition();
    composition.setGrammar(generator.grammar);
    composition.initTree(rootgrid,rootpolygon);
    generator.composer.compose(composition,getScaledDetailLimit(rootpolygon));}
  
  private double getScaledDetailLimit(FPolygon polygon){
    return generator.compositiondetaillimit*polygon.getDPolygon().getBounds().height;
  }

}
