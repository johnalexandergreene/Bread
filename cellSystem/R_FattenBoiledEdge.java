package org.fleen.bread.cellSystem;

public class R_FattenBoiledEdge implements Rule{
  
  /*
   * got each cell in cs0
   * apply rules to get new cell contents
   * set cell in cs1
   */
  public void doRule(CellSystem cs0,CellSystem cs1){
    Cell d;
    for(Cell c: cs0){
      d=getBoiledNeighbor(c,cs0);
      if(d!=null){
        cs1.getCell(c.x,c.y).thing=d.thing;
      }else{
        cs1.getCell(c.x,c.y).thing=c.thing;}}}
  
  private Cell getBoiledNeighbor(Cell c,CellSystem cs0){
    Cell[] n=cs0.getNeighbors(c);
    for(Cell d:n){
      if(d!=null&&d.thing.hasTags("boiled")){
        return d;}}
    return null;}
  
}
