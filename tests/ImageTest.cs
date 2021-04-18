using System.Collections.Generic;
using System.IO;
using Noise;

namespace ImageTest
{
    public delegate double Noise2D(double x, double y);
    public delegate double Noise3D(double x, double y, double z);
    public delegate double Noise4D(double x, double y, double z, double w);

    class Program
    {
        static OpenSimplex2S generator = new OpenSimplex2S(0);
        const int imgWidth = 600;
        const int imgHeight = 600;
        const int zdepth = 5;
        const int wdepth = 5;
        const int ox = 300;
        const int oy = 300;
        const int oz = 400;
        const int ow = 400;
        const double sx = .01;
        const double sy = .01;
        const double sz = .1;
        const double sw = .1;

        static void writeImage(string name, int w, int h, List<byte> data)
        {
            var writer = new StreamWriter(name);
            writer.Write($"P5 {w} {h} 255 ");
            writer.Close();

            var binWriter = new BinaryWriter(new FileStream(name, FileMode.Append));
            foreach (byte c in data)
            {
                binWriter.Write(c);
            }
            binWriter.Close();
        }

        static byte toLuminosity(double x)
        {
            var vrange = 2.0;
            var normalized = (x + (vrange / 2)) / vrange;
            return (byte)(normalized * 255);
        }

        static void test2D(string name, Noise2D noisef)
        {
            var data = new List<byte>();

            for (int y = 0; y < imgHeight; y++)
            {
                for (int x = 0; x < imgWidth; x++)
                {
                    var v = noisef((x + ox) * sx, (y + oy) * sy);
                    data.Add(toLuminosity(v));
                }
            }

            writeImage(name, imgWidth, imgHeight, data);
        }

        static void test3D(string name, Noise3D noisef) 
        { 
            var data = new List<byte>();

            for (int z = 0; z < zdepth; z++)
            {
                for (int y = 0; y < imgHeight; y++)
                {
                    for (int x = 0; x < imgWidth; x++)
                    {
                        var v = noisef((x + ox) * sx, (y + oy) * sy, (z + oz) * sz);
                        data.Add(toLuminosity(v));
                    }
                }
            }

            writeImage(name, imgWidth, imgHeight * zdepth, data);
        }
        static void test4D(string name, Noise4D noisef) 
        { 
            var data = new List<byte>();

            for (int z = 0; z < zdepth; z++)
            {
                for (int y = 0; y < imgHeight; y++)
                {
                    for (int w = 0; w < wdepth; w++)
                    {
                        for (int x = 0; x < imgWidth; x++)
                        {
                            var v = noisef((x + ox) * sx, (y + oy) * sy, (z + oz) * sz, (w + ow) * sw);
                            data.Add(toLuminosity(v));
                        }
                    }
                }
            }

            writeImage(name, imgWidth * wdepth, imgHeight * zdepth, data);
        }

        static void Main(string[] args)
        {
            test2D("noise2.pgm", generator.Noise2);
            test2D("noise2-XbeforeY.pgm", generator.Noise2_XBeforeY);

            test3D("noise3-classic.pgm", generator.Noise3_Classic);
            test3D("noise3-XYbeforeZ.pgm", generator.Noise3_XYBeforeZ);
            test3D("noise3-XZbeforeY.pgm", generator.Noise3_XZBeforeY);

            test4D("noise4-classic.pgm", generator.Noise4_Classic);
            test4D("noise4-XYbeforeZW.pgm", generator.Noise4_XYBeforeZW);
            test4D("noise4-XZbeforeYW.pgm", generator.Noise4_XZBeforeYW);
            test4D("noise4-XYZbeforeW.pgm", generator.Noise4_XYZBeforeW);
        }
    }
}
