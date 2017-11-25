package org.fleen.bread.cellSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class R_Smooth implements Rule{
  
  static final int[][] VIEWRANGEOFFSETS={
    {-1,3},{0,3},{1,3},
    {-2,2},{-1,2},{0,2},{1,2},{2,2},
    {-3,1},{-2,1},{-1,1},{0,1},{1,1},{2,1},{3,1},
    {-3,0},{-2,0},{-1,0},{1,0},{2,0},{3,0},//skipping center
    {-3,-1},{-2,-1},{-1,-1},{0,-1},{1,-1},{2,-1},{3,-1},
    {-2,-2},{-1,-2},{0,-2},{1,-2},{2,-2},
    {-1,-3},{0,-3},{1,-3}};
  
//  static final int[][] VIEWRANGEOFFSETS={
//    {-1,1},{0,1},{1,1},
//    {-1,0},{1,0},//skipping center
//    {-1,-1},{0,-1},{1,-1}};
  
  /*
   * get c0 neighbors out to viewrange
   * get sum for each thing
   * set c1 cell to thing with greatest sum 
   */
  public void doRule(CellSystem cs0,CellSystem cs1){
    Cell c1;
    MappedThing majority;
    for(Cell c0: cs0){
      majority=getGreatestSumThing(c0,cs0);
      c1=cs1.getCell(c0.x,c0.y);
      c1.thing=majority;}}
  
  private MappedThing getGreatestSumThing(Cell c,CellSystem cs){
    Map<MappedThing,MappedThingSum> sumsbythings=new HashMap<MappedThing,MappedThingSum>();
    MappedThingSum sum;
    List<Cell> n=getNeighbors(c,cs);
    for(Cell d:n){
      if(d==null)continue;
      sum=sumsbythings.get(d.thing);
      if(sum==null){
        sum=new MappedThingSum(d.thing);
        sumsbythings.put(d.thing,sum);}
      sum.sum++;}
    List<MappedThingSum> sums=new ArrayList<MappedThingSum>(sumsbythings.values());
    Collections.sort(sums,new MTSComparator());
    return sums.get(0).t;}
  
  private List<Cell> getNeighbors(Cell c,CellSystem cs){
    List<Cell> n=new ArrayList<Cell>(VIEWRANGEOFFSETS.length);
    for(int[] a:VIEWRANGEOFFSETS)
      n.add(cs.getCell(c.x+a[0],c.y+a[1]));
    return n;}
  
  class MTSComparator implements Comparator<MappedThingSum>{
    public int compare(MappedThingSum a,MappedThingSum b){
      if(a.sum>b.sum){
        return 1;
      }else if(a.sum<b.sum){
        return -1;
      }else{
        return 0;}}}
  
  class MappedThingSum{
    
    MappedThingSum(MappedThing t){
      this.t=t;}
    
    MappedThing t;
    int sum=0;
  }
  
}
