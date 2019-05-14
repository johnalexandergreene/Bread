package org.fleen.bread.composer;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.app.compositionGenerator.composer.Composer_Abstract;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.FJig;
import org.fleen.forsythia.core.grammar.FJigSection;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.GD;
import org.fleen.util.tree.TreeNodeIterator;

public class Composer003_SplitBoil_DoubleRootEntropy_YAxisDistanceEntropyGradient extends Composer_Abstract{
  
  Random rnd=new Random();
  
  /*
   * ################################
   * BUILD
   * overriding the abstract here
   * at buildcycleindex 1 and 2 (ie polygon tree level 1 and 2) we entropize the chorus indices
   * ################################
   */
  
  protected int buildcycleindex;
  
  protected void build(ForsythiaComposition composition,double detaillimit){
    initArbitraryEntropy(composition);
    boolean creatednodes=true;
    buildcycleindex=0;
    while(creatednodes){
      System.out.println("buildcycleindex="+buildcycleindex);
      if(buildcycleindex==0)entropize(composition);
      if(buildcycleindex==1)entropize(composition);
      if(buildcycleindex==2)entropize(composition);
      buildcycleindex++;
      creatednodes=createNodes(composition,detaillimit);}}
  
  private void entropize(ForsythiaComposition composition){
    int a=0;
    for(FPolygon p:composition.getPolygons()){
      p.chorusindex=a;
      a++;}}
  
  static final int ARBITRARYENTROPYINDEXSTARTVAL=1000000;
  int arbitraryentropychorusindex;
  double arbitraryentropyyaxis,arbitraryentropydistancemax;
  
  private void initArbitraryEntropy(ForsythiaComposition composition){
    arbitraryentropychorusindex=ARBITRARYENTROPYINDEXSTARTVAL;
    Rectangle2D.Double b=composition.getRootPolygon().getDPolygon().getBounds();
    arbitraryentropyyaxis=(b.getMaxY()+b.getMinY())/2;
    arbitraryentropydistancemax=b.getMaxY()-b.getMinY();}
  
  /*
   * ################################
   * CREATE NODES
   * ################################
   */
  
  protected boolean createNodes(ForsythiaComposition composition,double detaillimit){
    FJig jig;
    boolean creatednodes=false;
    TreeNodeIterator i=composition.getLeafPolygonIterator();
    //
    FPolygon leaf;
    ForsythiaGrammar grammar=composition.getGrammar();
    while(i.hasNext()){
      leaf=(FPolygon)i.next();
      doArbitraryEntropy(leaf);
      if(isCapped(leaf))continue;
      jig=selectJig(grammar,leaf,detaillimit);
      if(jig==null){
        cap(leaf);
      }else{
        jig.createNodes(leaf);
        creatednodes=true;}}
    jigbypolygonsig.clear();
    return creatednodes;}
  
  /*
   * do entropy at probability proportional to distance from yaxis
   */
  private void doArbitraryEntropy(FPolygon leaf){
    DPoint p0=GD.getPoint_Mean(leaf.getDPolygon());
    double 
      dis=Math.abs(p0.y-arbitraryentropyyaxis),
      ndis=dis/arbitraryentropydistancemax;
    
//    System.out.println("------------------------");
//    System.out.println("arbitraryentropyyaxis="+arbitraryentropyyaxis);
//    System.out.println("p0.y="+p0.y);
//    System.out.println("dis="+dis);
//    System.out.println("ndis="+ndis);
    
//    ndis*=0.5;//looks pretty good. good constant. gonna try lower, for a skinnier entropy band
    ndis*=0.25;
      //go with natural probability first
    if(rnd.nextDouble()<ndis){
      leaf.chorusindex=arbitraryentropychorusindex;
      arbitraryentropychorusindex++;}}
  
  
  
  /*
   * ################################
   * JIG SELECTOR
   * ################################
   */
  
  Map<FPolygonSignature,FJig> jigbypolygonsig=new Hashtable<FPolygonSignature,FJig>();
  
  private FJig selectJig(ForsythiaGrammar forsythiagrammar,FPolygon polygon,double detaillimit){
    //get a jig by signature
    //polygons with the same sig get the same jig
    FJig j=jigbypolygonsig.get(polygon.getSignature());
    if(j!=null){
      return j;
    //no jig found keyed by that signature
    //so get one from the grammar using various random selection techniques
    }else{
      j=getRandomJigUsingSplitBoilLogic(forsythiagrammar,polygon,detaillimit);
      if(j==null)return null;
      jigbypolygonsig.put(polygon.getSignature(),j);
      return j;}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * SPLIT BOIL LOGIC
   * get the set of prospective jigs
   * separate them into 2 lists : splitters and boilers
   * if the target is an egg then pick a splitter
   * otherwise the target is a shard, pick a boiler
   * in either case, if there is nonesuch then pick from the other list
   * 
   * ++++++++++++++++++++++++++++++++
   */
  
  private List<FJig> 
    boilers=new ArrayList<FJig>(),
    splitters=new ArrayList<FJig>();
  
  private FJig getRandomJigUsingSplitBoilLogic(ForsythiaGrammar fg,FPolygon target,double detaillimit){
    List<FJig> jigs=fg.getJigsAboveDetailSizeFloor(target,detaillimit);
    if(jigs.isEmpty())return null;
    //
    createBoilersAndSplittersLists(jigs);
    FJig jig;
//    if(target.isRootPolygon()||(rnd.nextDouble()>0.5&&target.hasTags("egg"))){
    if(target.isRootPolygon()||target.hasTags("egg")){
      jig=getRandomSplitter();
      if(jig==null)jig=getRandomBoiler();
    }else{
      jig=getRandomBoiler();
      if(jig==null)jig=getRandomSplitter();}
    return jig;}
  
  
  private FJig getRandomBoiler(){
    if(boilers.isEmpty())return null;
    FJig jig=boilers.get(rnd.nextInt(boilers.size()));
    return jig;}
  
  private FJig getRandomSplitter(){
    if(splitters.isEmpty())return null;
    FJig jig=splitters.get(rnd.nextInt(splitters.size()));
    return jig;}
  
  private void createBoilersAndSplittersLists(List<FJig> jigs){
    boilers.clear();
    splitters.clear();
    for(FJig jig:jigs){
      if(isBoiler(jig))
        boilers.add(jig);
      else
        splitters.add(jig);}}
  
  /*
   * If a jig has a section tagged "egg" then that jig is a boiler
   * and if it isn't a boiler then it's a splitter
   */
  private boolean isBoiler(FJig jig){
    for(FJigSection s:jig.sections)
      if(s.tags.hasTag("egg"))
        return true;
    return false;}
  
  
}
