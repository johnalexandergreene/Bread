package org.fleen.bread.composer;

import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

public interface Composer{
  
  //create a new composition
  ForsythiaComposition compose(ForsythiaGrammar grammar,double detaillimit);
  
}
