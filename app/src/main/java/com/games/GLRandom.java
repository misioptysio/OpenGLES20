package com.games;

import java.util.List;

/**
 * Created by piotr.plys on 2015-11-27.
 */
public class GLRandom
{
  //stack of seeds: [0] = current vale, [1],[2] = state of generator, [3] = original seed
  private long[][] s = new long[32][4];
  private short seedIndex = 0;

  private final static double DOUBLE_MULTIPLIER = 1.0 / 0x7FFFFFFFFFFFFFFFL;
  private final static float FLOAT_MULTIPLIER = 1.0f / 0x7FFFFFFFFFFFFFFFL;


  public GLRandom(long seed)
  {
    initSeed(seed);
  }

  private void initSeed(long seed)
  {
    s[seedIndex][1] = seed ^ 0x55AA55AA55AA55AAL;
    s[seedIndex][2] = seed ^ 0x5DEE34985FACE66DL;
    //set original seed
    s[seedIndex][3] = seed;
  }

  public void pushSeed(long newSeed)
  {
    if (seedIndex < (s.length - 1))
      seedIndex++;
    initSeed(newSeed);
  }

  public void popSeed()
  {
    if (seedIndex > 0)
      seedIndex--;
  }

  public long nextLong()
  {
    long x = s[seedIndex][1];
    long y = s[seedIndex][2];

    s[seedIndex][1] = y;
    x ^= x << 23; // a
    x ^= x >>> 17; // b
    x ^= y ^ (y >>> 26); // c
    s[seedIndex][2] = x;

    s[seedIndex][0] = (x + y) & 0x7FFFFFFFFFFFFFFFL;
    return s[seedIndex][0];
  }

  public int nextInt()
  {
    return (int) (nextLong() & 0x000000007FFFFFFFL);
  }

  //maxValue is inclusive
  public int nextInt(int maxValue)
  {
    return (nextInt() % (maxValue + 1));
  }

  public double nextDouble()
  {
    return (nextLong() * DOUBLE_MULTIPLIER);
  }

  public float nextFloat()
  {
    return (nextLong() * FLOAT_MULTIPLIER);
  }

  //n: number of discreet values from <0.0, 1.0>. N=20 will give you 21 possible values: 0.00, 0.05, 0.10, 0.15, ... , 0.95 and 1.00
  public float nextFloatDiscreet(int n)
  {
    float res;

    if (n <= 0)
      res = 0.0f;
    else
      res = nextInt(n) / (float) n;

    return (res);
  }
}
