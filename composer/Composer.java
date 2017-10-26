package org.fleen.bread.composer;

import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

/*
 * creates a tree of forsythia composition nodes
 */
public interface Composer{
  
  //create a new composition
  ForsythiaComposition compose(ForsythiaGrammar grammar,double detaillimit);
  
  //build on an an existing composition
  void compose(ForsythiaComposition c,double detaillimit);
  
  
  
}
