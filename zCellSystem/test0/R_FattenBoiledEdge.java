package org.fleen.bread.zCellSystem.test0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fleen.bread.zCellSystem.ZCSMT_FPolygonBoiledEdge;
import org.fleen.bread.zCellSystem.ZCSMappedThing;
import org.fleen.bread.zCellSystem.ZCSMappedThingPresence;
import org.fleen.bread.zCellSystem.ZCell;
import org.fleen.bread.zCellSystem.ZCellSystem;

/*
 * dupe present presences state
 * get sum of neighboring boiled edge presences : s
 * add s*k presence to cell
 * 
 */
public class R_FattenBoiledEdge implements Rule{
  
  static final int[][] NOFF_RANGE1={
      {-1,1},{0,1},{1,1},
      {1,0},
      {1,-1},{0,-1},{-1,-1},
      {-1,0}};
  
//  static final int[][] NOFF_RANGE3={
//    {-1,3},{0,3},{1,3},
//    {-2,2},{-1,2},{0,2},{1,2},{2,2},
//    {-3,1},{-2,1},{-1,1},{0,1},{1,1},{2,1},{3,1},
//    {-3,0},{-2,0},{-1,0},{1,0},{2,0},{3,0},//skipping center
//    {-3,-1},{-2,-1},{-1,-1},{0,-1},{1,-1},{2,-1},{3,-1},
//    {-2,-2},{-1,-2},{0,-2},{1,-2},{2,-2},
//    {-1,-3},{0,-3},{1,-3}};
  
  public void doRule(ZCellSystem s0,ZCellSystem s1){
    List<ZCSMappedThingPresence> psum;
    ZCell c1;
    for(ZCell c0:s0){
      c1=s1.getCell(c0.x,c0.y);
      c1.setPresences(c0.presences);
      psum=getBoiledEdgeWeightedPresenceSum(c0,s0);
      c1.addPresences(psum);
      c1.clean();}}
  
  /*
   * TODO we need a PresenceSum object, not just a list
   * something we can add to, remove from, copy, etc
   */
  private List<ZCSMappedThingPresence> getBoiledEdgeWeightedPresenceSum(ZCell c,ZCellSystem s){
    Map<ZCSMappedThing,ZCSMappedThingPresence> summedpresencebything=new HashMap<ZCSMappedThing,ZCSMappedThingPresence>();
    ZCell n;
    ZCSMappedThingPresence p;
    for(int[] a:NOFF_RANGE1){
      n=s.getCell(c.x+a[0],c.y+a[1]);
      if(n!=null){
        for(ZCSMappedThingPresence p0:n.presences){
          if(p0.thing instanceof ZCSMT_FPolygonBoiledEdge){
            p=summedpresencebything.get(p0.thing);
            if(p==null){
              p=new ZCSMappedThingPresence(p0);
              summedpresencebything.put(p.thing,p);
            }else{
              p.intensity+=p0.intensity;}}}}}
    List<ZCSMappedThingPresence> sum=new ArrayList<ZCSMappedThingPresence>(summedpresencebything.values());
    return sum;}
  
}
