/**************************************************************************
*   vthierry@sophia.inria.fr, Copyright (C) 2004.  All rights reserved.   *
**************************************************************************/

package org.javascool;

// Used to catch exceptions
import java.lang.RuntimeException;

// Used for thread management
import java.lang.Thread;
import java.lang.Runnable;

// Used for timer management
import java.lang.System;
import java.lang.InterruptedException;

/** Defines the implementation of a periodic task.
 *
 * <div>The periodic task is <b>defined</b> by the method: <ul>
 *
 * <li><a href="#run()">run()</a> called at each iteration.
 * If a runtime exception occurs, the sampling is stopped and the exception is thrown, when calling any method of the sampler.</li>
 *
 * </ul></div>
 *
 * It is parameterized by the <b><i>sampling-period</i></b>.
 *
 * <div>The periodic task is <b>controlled</b> by the start/stop methods: <ul>
 *
 * <li><a href="#start(int)">start(samplingPeriod)</a> (re)starting the iteration.
 *  <div>User can overwrite the method with a construct of the form <tt>public void start() { &lt;specific-code> super.start(); }</tt></div></li>
 *
 * <li><a href="#stop()">stop()</a> which stops the iteration.
 *  <div>User can overwrite the method with a construct of the form <tt>public void stop() { super.stop(); &lt;specific-code> }</tt></div></li>
 *
 * </ul></div>
 *
 */
public abstract class Sampler {
  /**/public Sampler() {}

  /** Periodically called when the sampler is started. */
  public abstract void run();

  /** (Re)starts the sampling of the <tt>run()</tt> method.
   * @param samplingPeriod @optional<10ms or the previous sampling period> A positive sampling-period in milliseconds.
   */
  public void start(int samplingPeriod) {
    if(samplingPeriod > 0)
      this.samplingPeriod = samplingPeriod;
    start();
  }
  /**/public void start() {
    error = null;
    (new Thread(new Runnable() {
                  // Loop with a time-period of samplingPeriod
                  public void run() {
                    try {
                      for(loop = resume = true; loop;) {
                        long t = System.currentTimeMillis();
                        Thread.yield();
                        if(resume)
                          run();
                        spareTime = samplingPeriod - (int) (System.currentTimeMillis() - t);
                        if(spareTime > 0) {
                          try { Thread.sleep(spareTime);
                          } catch(InterruptedException e) {}
                        }
                      }
                    } catch(RuntimeException e) {
                      error = e;
                    }
                    loop = resume = false;
                  }
                }
                )).start();
  }
  // This flags is true during sampling loop
  private boolean loop = false, resume = false;

  /** Sets the sampling period.
   * @param samplingPeriod A positive sampling-period in milliseconds.
   */
  public void setSamplingPeriod(int samplingPeriod) {
    if(samplingPeriod > 0)
      this.samplingPeriod = samplingPeriod;
  }
  private int samplingPeriod = 10;

  /** Pauses/Resumes the iteration mechanism. */
  public void resume(boolean resume) {
    this.resume = resume;
  }
  // Throws a runtime-exception if any
  private void check() {
    Thread.yield();
    if(error != null) {
      RuntimeException e = error;
      error = null; throw new RuntimeException("Sampling exception: " + e);
    }
  }
  // Contains the last run-time error if any
  private RuntimeException error = null;

  /** Requires the sampling of the <tt>run()</tt> method to stop.
   * <div>The current iteration, if any, terminates before stopping.</div>
   *
   * @throws RuntimeException if a runtime exception has occurred during sampling.
   */
  public void stop() {
    loop = resume = false;
    check();
  }
  /** Returns the spare-time between two samplings.
   *
   * @return The last spare-time between two samplings, in milliseconds: <ul>
   *  <li> If negative, it indicates that the task overruns.</li>
   *  <li>It is equal to Integer.MIN_VALUE if no iteration has been completed. </li>
   *  <li> It depends on both the task execution time and other thread execution times.</li>
   * </ul>
   *
   * @throws RuntimeException if a runtime exception has occurred during sampling.
   */
  public int getSpareTime() {
    check();
    return spareTime;
  }
  private int spareTime = -Integer.MIN_VALUE;
}
