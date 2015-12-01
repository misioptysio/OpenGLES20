package com.games;

import static com.games.Utils.*;
/**
 * Created by piotr.plys on 2015-12-01.
 */

/*
  source: http://mrl.nyu.edu/~perlin/noise/
 */
public class PerlinNoise
{
  static final int p[] = new int[512];
  static int permutation[] =
  {
    151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225,
    140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148,
    247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32,
    57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175,
    74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122,
    60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54,
    65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169,
    200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64,
    52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212,
    207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213,
    119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
    129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104,
    218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241,
    81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157,
    184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93,
    222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
  };

  public PerlinNoise()
  {
    for (int i = 0; i < 256; i++)
    {
      p[i] = permutation[i];
      p[256 + i] = permutation[i];
    }
  }

  static public double noise(double x, double y, double z)
  {
    //find out the cube the point is in
    int indexX = (int) Math.floor(x) & 0x000000FF;
    int indexY = (int) Math.floor(y) & 0x000000FF;
    int indexZ = (int) Math.floor(z) & 0x000000FF;

    //relative position of x, y and z in the cube
    x -= Math.floor(x);
    y -= Math.floor(y);
    z -= Math.floor(z);

    double tX = interpolationFunctionPerlin(x);
    double tY = interpolationFunctionPerlin(y);
    double tZ = interpolationFunctionPerlin(z);

    int indexX0Y0 = p[indexX] + indexY;
    int indexX0Y0Z0 = p[indexX0Y0] + indexZ;
    int indexX0Y1Z0 = p[indexX0Y0 + 1] + indexZ;
    int indexX0Y0Z1 = p[indexX0Y0Z0 + 1];
    int indexX0Y1Z1 = p[indexX0Y1Z0 + 1];

    int indexX1Y0 = p[indexX + 1] + indexY;
    int indexX1Y0Z0 = p[indexX1Y0] + indexZ;
    int indexX1Y1Z0 = p[indexX1Y0 + 1] + indexZ;
    int indexX1Y0Z1 = p[indexX1Y0Z0 + 1];
    int indexX1Y1Z1 = p[indexX1Y1Z0 + 1];

    double x0y0z0 = gradient(p[indexX0Y0Z0], x, y, z);
    double x1y0z0 = gradient(p[indexX1Y0Z0], x - 1, y, z);
    double x0y1z0 = gradient(p[indexX0Y1Z0], x, y - 1, z);
    double x1y1z0 = gradient(p[indexX1Y1Z0], x - 1, y - 1, z);

    double x0y0z1 = gradient(p[indexX0Y0Z1], x, y, z - 1);
    double x1y0z1 = gradient(p[indexX1Y0Z1], x - 1, y, z - 1);
    double x0y1z1 = gradient(p[indexX0Y1Z1], x, y - 1, z - 1);
    double x1y1z1 = gradient(p[indexX1Y1Z1], x - 1, y - 1, z - 1);

    double y0z0 = interpolateValues(x0y0z0, x1y0z0, tX);
    double y1z0 = interpolateValues(x0y1z0, x1y1z0, tX);
    double y0z1 = interpolateValues(x0y0z1, x1y0z1, tX);
    double y1z1 = interpolateValues(x0y1z1, x1y1z1, tX);

    double z0 = interpolateValues(y0z0, y1z0, tY);
    double z1 = interpolateValues(y0z1, y1z1, tY);

    return interpolateValues(z0, z1, tZ);
  }

  static double gradient(int hash, double x, double y, double z)
  {
    //convert lower 4 bits into 12 gradient directions
    int h = hash & 0x0000000F;

    double u = h < 8 ? x : y;
    double v = h < 4 ? y : h == 12 || h == 14 ? x : z;

    return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
  }
}
