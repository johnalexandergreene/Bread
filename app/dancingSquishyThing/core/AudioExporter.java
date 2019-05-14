package org.fleen.bread.app.dancingSquishyThing.core;

import java.io.File;

public interface AudioExporter{
  
  void setWhelmer(Whelmer w);
  
  Whelmer getWhelmer();
  
  /*
   * assemble the audioframes arrays into a single big sound array
   * convert the int[] to a WAV or whatever and write it
   */
  void exportAudio(int[] audio,File workingdirectory);

}
