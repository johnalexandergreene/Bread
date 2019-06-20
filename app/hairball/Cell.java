package org.fleen.bread.app.hairball;

/*
 * a cell in our chain-loop of cells
 */
public class Cell{
  
  Cell(double[] a){
    setLocation(a);
  }
  
  //location of this cell
  public double x,y;
  //neighboring cells in the chain
  //n0 is probably counterclockwise, n1 is probably clockwise, nut not necessarily
  public Cell n0,n1;
  
  public void setLocation(double[] a){
    x=a[0];
    y=a[1];}

}
