package org.fleen.bread.app.dancingSquishyThing.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.GD;
import org.fleen.whelmer.core.ring.Projector;

/*
 * it's an audiovideo generation system
 * it's a 3d function. 2d frames over time
 * we project a system of moving rings and maybe a few other things upon an array of cells.
 * from each cell we get a value
 * from those values we get colors and sounds. Thus our audiovideo
 * 
 */
public class Whelmer{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Whelmer(
    int size,int duration,Projector rings,
    ProgressListener progresslistener,File exportdir,
    VideoRenderer videorenderer,VideoExporter videoexporter,
    AudioRenderer audiorenderer,AudioExporter audioexporter){
    this.duration=duration;
    this.projector=rings;
    rings.setWhelmer(this);
    this.exportdir=exportdir;
    this.progresslistener=progresslistener;
    this.videorenderer=videorenderer;
    this.videoexporter=videoexporter;
    this.audiorenderer=audiorenderer;
    this.audioexporter=audioexporter;
    audioexporter.setWhelmer(this);
    initCells(size);}
  
  /*
   * ################################
   * GLOBAL METRICS
   * ################################
   */
  
  /*
   * 720=1*2*3*4*5*6
   * thus 720 has lots of factors, which is why we chose it
   * we do 60 frames per second
   * 60*720=43200
   * So 43200 is our per-second sample rate
   */
  static final int
    //video and audio frame rate
    FRAMERATE=60,
    //sound sample rate over a single 1/60th of a second frame. 
    AUDIOSAMPLERATEPERFRAME=720,
    //sound sample rate over a whole second
    AUDIOSAMPLERATEPERSECOND=FRAMERATE*AUDIOSAMPLERATEPERFRAME;//43200
  
  public int getFrameRate(){
    return FRAMERATE;}
  
  public int getAudioSampleRatePerFrame(){
    return AUDIOSAMPLERATEPERFRAME;}
  
  public int getAudioSampleRatePerSecond(){
    return AUDIOSAMPLERATEPERSECOND;}
  
  /*
   * ################################
   * TIME AND MAIN PROCESS
   * increment ring system until we hit duration
   *   at each increment 
   *     render a frame
   *     render a soundpiece
   *     export frame
   *   when finished export sound
   * ################################
   */
  
  public int time=-1,duration;
  
  static long PROGRESSINFOINCREMENTMS=1000;
  
  private long 
    runstartms,
    priorprogressinfoms=-PROGRESSINFOINCREMENTMS;
  
  public void run(){
    System.out.println("whelmer run : duration="+duration);
    runstartms=System.currentTimeMillis();
    for(int i=0;i<duration;i++){
      incrementTime();}
    //
    exportAudio();
    //
    System.out.println("#### FINISHED ####");
    System.out.println("time elapsed : "+(System.currentTimeMillis()-runstartms));
    progresslistener.finished(this);
    }
  
  private void incrementTime(){
    time++;
    long presentms=System.currentTimeMillis();
    if(presentms>=priorprogressinfoms+PROGRESSINFOINCREMENTMS){
      priorprogressinfoms=presentms;
      System.out.println("================================");
      System.out.println("framecount="+time+" / "+duration);
      System.out.println("ringcount="+projector.getRingCount());
      long timeelapsed=presentms-runstartms;
      System.out.println("timeelapsed="+timeelapsed);
      int z=time;
      if(z<1)z=1;
      System.out.println("eta="+((timeelapsed/z)*(duration-z)));
      System.out.println("================================");}
    renderAndExportVideoFrame();
    renderAndStoreAudioFrame();
    projector.conditionallyCreateRings();
    projector.conditionallyDestroyRings();
    progresslistener.timeIncremented(this);}
  
  /*
   * ################################
   * PROGRESS LISTENER
   * ################################
   */
  
  private ProgressListener progresslistener;
  
  /*
   * ################################
   * EXPORT DIR
   * ################################
   */
  
  File exportdir;
  
  /*
   * ################################
   * CONTROLLER
   * ################################
   */
  
  public Projector projector;

  /*
   * ################################
   * CELLS
   * a field of squares upon which the projector projects stuff
   * it's just a 2d array of doubles
   * the double value is the 2d distance from the center of the array-square
   *   in terms of the array-square span /2
   *   the normalized radius, in other words
   *   the value ranges [0,1] 
   * ################################
   */
  
  //a cell is a component of the ground upon which rings cast their radiance
  public double[][] cells;
  
  public int getCellArraySpan(){
    return cells.length;}
  
  private void initCells(int size){
    double whelmercenter=((double)size)/2;//being a square this serves for both x and y
    cells=new double[size][size];
    for(int x=0;x<size;x++){
      for(int y=0;y<size;y++){
        cells[x][y]=getCellDistance(whelmercenter,x,y);}}}
  
  /*
   * remember!
   * our distances are in terms of size/2.
   * ie, in range 0..1
   * clip everything bigger than 1
   */
  private double getCellDistance(double whelmercenter,double x,double y){
    double 
      cellcenterx=x+0.5,
      cellcentery=y+0.5,
      d=GD.getDistance_PointPoint(whelmercenter,whelmercenter,cellcenterx,cellcentery);
    d=(d*2)/((double)getCellArraySpan());//normalize it to [0,1]
    return d;}
  
  /*
   * ################################
   * VIDEO RENDERER
   * ################################
   */
  
  private VideoRenderer videorenderer=null;
  public BufferedImage videoframe=null;
  
  private void renderAndExportVideoFrame(){
    if(videorenderer!=null)
      videoframe=videorenderer.renderFrame(this);
    if(videoframe!=null)
      exportVideoFrameImage();}
  
  public BufferedImage getVideoFrameImage(){
    return videoframe;}
  
  /*
   * ################################
   * VIDEO EXPORTER
   * ################################
   */
  
  private VideoExporter videoexporter=null;
  
  private void exportVideoFrameImage(){
    if(exportdir!=null&&videoexporter!=null)
      videoexporter.exportVideoFrame(videoframe,time,exportdir);}
  
  /*
   * ################################
   * AUDIO RENDERER
   * ################################
   */
  
  private AudioRenderer audiorenderer=null;
  private List<int[]> audioframes=new ArrayList<int[]>();
  
  private void renderAndStoreAudioFrame(){
    if(audiorenderer!=null){
      int[] a=audiorenderer.renderAudioFrame(this);
      audioframes.add(a);}}
  
  public List<int[]> getAudioFrames(){
    return audioframes;}
  
  /*
   * ################################
   * AUDIO EXPORTER
   * ################################
   */
  
  private AudioExporter audioexporter=null;
  
  void exportAudio(){
    if(exportdir!=null&&audioexporter!=null){
      System.out.println("export audio");
      //assemble the sound array
      int framelength=getAudioSampleRatePerFrame();
      int[] audio=new int[audioframes.size()*framelength];
      for(int i=0;i<audioframes.size();i++)
        System.arraycopy(audioframes.get(i),0,audio,i*framelength,framelength);
      //export it
      audioexporter.exportAudio(audio,exportdir);}}

}
