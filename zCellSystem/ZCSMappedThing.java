package org.fleen.bread.zCellSystem;

import java.awt.geom.AffineTransform;
import java.util.List;

import org.fleen.util.tag.TagManager;
import org.fleen.util.tag.Tagged;

/*
 * a thing that is mapped to a cellsystem
 * the thing and some associated tags
 */
public class ZCSMappedThing implements Tagged{

  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public ZCSMappedThing(){}
  
  public ZCSMappedThing(Object thing){
    this.thing=thing;}
  
  public ZCSMappedThing(Object thing,AffineTransform transform,double glowspan,String[] tags){
    this(thing);
    this.transform=transform;
    this.glowspan=glowspan;
    tagmanager.addTags(tags);}
  
  /*
   * ################################
   * THING
   * The thing that got mapped, or enough of it to get a handle on what we're talking about
   * See tags for more clues if necessary
   * ################################
   */
  
  public Object thing;
  
  /*
   * ################################
   * TRANSFORM
   * Mapped things often need transforms
   * ################################
   */
  
  public AffineTransform transform;
  
  /*
   * ################################
   * GLOWSPAN
   * Mapped things often have a presence that spreads a bit from their actual location
   * ################################
   */
  
  public double glowspan;
  
  /*
   * ################################
   * GENERAL PURPOSE OBJECT
   * ################################
   */
  
  public Object gpobject;
  
  /*
   * ################################
   * TAGS
   * ################################
   */
  
  private TagManager tagmanager=new TagManager();
  
  public void setTags(String... tags){
    tagmanager.setTags(tags);}
  
  public void setTags(List<String> tags){
    tagmanager.setTags(tags);}
  
  public List<String> getTags(){
    return tagmanager.getTags();}
  
  public boolean hasTags(String... tags){
    return tagmanager.hasTags(tags);}
  
  public boolean hasTags(List<String> tags){
    return tagmanager.hasTags(tags);}
  
  public void addTags(String... tags){
    tagmanager.addTags(tags);}
  
  public void addTags(List<String> tags){
    tagmanager.addTags(tags);}
  
  public void removeTags(String... tags){
    tagmanager.removeTags(tags);}
  
  public void removeTags(List<String> tags){
    tagmanager.removeTags(tags);}
  
}
