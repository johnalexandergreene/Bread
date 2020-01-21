package org.fleen.bread.app.flerp0flerp;

public class Collision{
  
  public Collision(PCircle c0,PCircle c1){
    this.c0=c0;
    this.c1=c1;
  }
  
  PCircle c0,c1;
  
  public boolean equals(Object a){
    Collision c=(Collision)a;
    if((c.c0==c0&&c.c1==c1)||(c.c0==c1&&c.c1==c0))
      return true;
    return false;}
  

}
