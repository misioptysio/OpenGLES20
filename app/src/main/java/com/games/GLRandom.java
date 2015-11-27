package com.games;

/**
 * Created by piotr.plys on 2015-11-27.
 */
public class GLRandom
{
  private long s0;
  private long s1;

  public GLRandom(long seed)
  {
    s0 = seed;
    s1 = seed ^ 0x5DEECE66DL;
  }

  public long nextInt()
  {
    long x = s0;
    long y = s1;

    s0 = y;
    x ^= x << 23; // a
    x ^= x >> 17; // b
    x ^= y ^ (y >> 26); // c
    s1 = x;
    return (long) x + y;
  }
}
