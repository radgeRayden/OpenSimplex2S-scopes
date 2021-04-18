using System.IO;
using Noise;

namespace ImageTest
{
    class Program
    {
        static void writeImage(string name, int w, int h, byte[] data)
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
        
        static byte toLuminosity (double x)
        {
            var vrange = 2.0;
            var normalized = (x + (vrange / 2)) / vrange;
            return (byte)(normalized * 255);
        }


        static void Main(string[] args)
        {
            var generator = new OpenSimplex2S(0);
            var width = 600;
            var height = 600;
            var ox = 300;
            var oy = 300;
            var oz = 0;
            var ow = 0;
            var scale = 100.0;

            // 2D noise
            var data = new byte[width * height];

            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    var v = generator.Noise2_XBeforeY((x + ox) / scale, (y + oy) / scale);
                    data[y * width + x] = toLuminosity(v);
                }
            }
            writeImage("2dnoise.pgm", width, height, data);

            // 3D noise
            var zdepth = 5;
            var height3 = height * zdepth;
            data = new byte[width * height3];

            for (int z = 0; z < zdepth; z++)
            {
                for (int y = 0; y < height; y++)
                {
                    for (int x = 0; x < width; x++)
                    {
                        var v = generator.Noise3_XYBeforeZ((x + ox) / scale, (y + oy) / scale, (z * 10 + oz) / scale);
                        data[height * width * z + y * width + x] = toLuminosity(v);
                    }
                }
            }
            writeImage("3dnoise.pgm", width, height3, data);

            // 4D noise
            var wdepth = 5;
            var width4 = width * wdepth;
            data = new byte[width4 * height3];
            for (int z = 0; z < zdepth; z++)
            {
                for (int w = 0; w < wdepth; w++)
                {
                    for (int y = 0; y < height; y++)
                    {
                        for (int x = 0; x < width; x++)
                        {
                            var v = generator.Noise4_XYZBeforeW((x + ox) / scale, (y + oy) / scale, (z * 10 + oz) / scale, (w * 10 + ow) / scale);
                            data[width4 * height * z + width4 * y + width * w + x] = toLuminosity(v);
                        }
                    }
                }
            }
            writeImage("4dnoise.pgm", width4, height3, data);
        }
    }
}
