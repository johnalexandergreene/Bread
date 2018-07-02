package org.fleen.bread.app.cruncher;

public class Test{
  
  public static final void main(String[] a){
    Cruncher c=new Cruncher();
    new UI(c);
    c.start();
  }

}
