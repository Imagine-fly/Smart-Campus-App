package com.arima.healthyliving.eyesighttest;

import java.util.ArrayList;

import android.util.Log;



public class ApplicationContext
{
  private static ApplicationContext instance = new ApplicationContext();
  private int[] scores = new int[2];

  public static ApplicationContext getInstance()
  {
    return instance;
  }

  public int[] getScores()
  {
    return this.scores;
  }

  public void quit_all()
  {
    resetScores();
 
  }

  public void resetScores()
  {
	int[] arrayOfInt = this.scores;
    this.scores[1] = 0;
    arrayOfInt[0] = 0;
  }

  public void setLeftScore(int paramInt)
  {
    this.scores[1] = paramInt;
  }

  public void setRightScore(int paramInt)
  {
    this.scores[0] = paramInt;
  }
}