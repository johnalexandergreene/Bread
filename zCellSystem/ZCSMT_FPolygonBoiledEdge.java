package org.fleen.bread.zCellSystem;

import java.awt.geom.AffineTransform;
import java.util.List;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.util.tag.TagManager;

public class ZCSMT_FPolygonBoiledEdge implements ZCSMappedThing{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public ZCSMT_FPolygonBoiledEdge(FPolygon polygon,AffineTransform transform,double glowspan,String[] tags){
    this.fpolygon=polygon;
    this.transform=transform;
    this.glowspan=glowspan;
    tagmanager.addTags(tags);}
  
  /*
   * ################################
   * THING
   * ################################
   */
  
  public FPolygon fpolygon;
  
  /*
   * ################################
   * TRANSFORM
   * ################################
   */
  
  public AffineTransform transform;
  
  /*
   * ################################
   * GLOWSPAN
   * Mapped things often have a presence that spreads a bit from their actual location
   * ################################
   */
  
  double glowspan;
  
  public double getGlowSpan(){
    return glowspan;}
  
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
