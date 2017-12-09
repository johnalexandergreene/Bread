package org.fleen.bread.zCellSystem;

/*
 * the degree to which a mapped thing exists at a particular zcell
 */
public class ZCSMappedThingPresence{
  
  ZCSMappedThingPresence(ZCSMappedThing thing,double intensity){
    this.thing=thing;
    this.intensity=intensity;}
  
  public ZCSMappedThing thing;
  public double intensity;

}
