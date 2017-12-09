package org.fleen.bread.hCellSystem.test0;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.Jig;

@SuppressWarnings("serial")
public class TestComposition000 extends ForsythiaComposition{
  
  static final String GRAMMARPATH="/home/john/Desktop/grammars/rdtest.grammar";
  
  public TestComposition000(){
    super();
    initGrammar();
    FMetagon rm=gleanRootMetagon(grammar);
    initTree(rm);
    Jig j=grammar.getJigs(rm).get(0);
    j.createNodes(getRootPolygon());}
  
  private FMetagon gleanRootMetagon(ForsythiaGrammar grammar){
    for(FMetagon m:grammar.getMetagons()){
      if(m.hasTags("root"))
        return m;}
    throw new IllegalArgumentException("exception in root metagon acquirement");}
  
  private void initGrammar(){
    File file=new File(GRAMMARPATH);
    FileInputStream fis;
    ObjectInputStream ois;
    ForsythiaGrammar g=null;
    try{
      fis=new FileInputStream(file);
      ois=new ObjectInputStream(fis);
      g=(ForsythiaGrammar)ois.readObject();
      ois.close();
    }catch(Exception x){
      System.out.println("exception in grammar import");
      x.printStackTrace();}
    setGrammar(g);}

}