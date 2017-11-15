package org.fleen.bread.RDSystem.test0;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.fleen.bread.RDSystem.Cell;
import org.fleen.bread.RDSystem.Presence;
import org.fleen.bread.RDSystem.RDSystem;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.geom_2D.DPolygon;

public class Test0{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test0(){
    initUI();
    initComposition();
    initRDS();
    initCompositionRDSTransform();
    castCompositionToRDS();
    initRenderer();}
  
  /*
   * ################################
   * MAIN LOOP
   * ################################
   */
  
  public void run(){
//    for(int i=0;i<3;i++){
////      doRules();
//      render();
//    }

    render();
    
  }
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  UI ui;
  
  private void initUI(){
    ui=new UI(this);}
  
  /*
   * ################################
   * COMPOSITION
   * ################################
   */
  
  Composition composition;
  
  private void initComposition(){
    composition=new Composition();}
  
  /*
   * ################################
   * COMPOSITION RDS TRANSFORM
   * scale up the composition because, dimensionally, it's pretty small
   * translate it to put the left and top edges at 0, + padding 
   * ################################
   */
  
  int padding=16;
  double scale=15;
  AffineTransform compositionrdstransform;
  
  private void initCompositionRDSTransform(){
    //note that we flip the y to convert cartesian coors to array coors
    compositionrdstransform=AffineTransform.getScaleInstance(scale,-scale);
    //
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    double 
      tx=-bounds.x*scale+padding/scale,
      ty=-bounds.y*scale+padding/scale-rds.getHeight()/scale;
    compositionrdstransform.translate(tx,ty);}
  
  /*
   * ################################
   * RDS
   * Our Reaction Diffusion System
   * ################################
   */
  
  double glowspan=1.5;
  
  RDSystem rds;
  
  void initRDS(){
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    int 
      w=(int)(bounds.width*scale+padding+padding),
      h=(int)(bounds.height*scale+padding+padding);
    rds=new RDSystem(w,h);}
  
  void castCompositionToRDS(){
//    mapRootArea();
    mapLeafAreas();
    mapHexEdge();
    rds.clean();
    //
    for(Cell c:rds){
      System.out.println("cell");
      for(Presence p:c.presences){
        System.out.print(p.intensity+" ");
      }
      System.out.println("");
    }}
  
  void mapRootArea(){
    DPolygon d=composition.getRootPolygon().getDPolygon();
    rds.mapPolygonArea(d,compositionrdstransform,glowspan);}
  
  void mapLeafAreas(){
    DPolygon d;
    for(FPolygon p:composition.getLeafPolygons()){
      d=p.getDPolygon();
      rds.mapPolygonArea(d,compositionrdstransform,glowspan);}}
  
  void mapHexEdge(){
    for(FPolygon p:composition.getLeafPolygons()){
      if(p.hasTags("hex")){
        DPolygon d=p.getDPolygon();
        rds.mapPolygonEdge(d,compositionrdstransform,glowspan);}}}
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  Renderer renderer;
  BufferedImage image=null;
  
  private void initRenderer(){
    renderer=new Renderer(this);}
  
  private void render(){
    renderer.render();
    ui.repaint();}
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    Test0 t=new Test0();
    t.run();}
  
}
