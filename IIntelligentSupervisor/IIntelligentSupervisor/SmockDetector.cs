using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Emgu.CV;
using Emgu.CV.Structure;
using System.Drawing;

namespace IIntelligentSupervisor
{
    public class SmockDetector
    {
        public bool ChechSmock(Bitmap input)
        {
            bool value;
            int counter = 0;
            Image<Bgr, byte> imgRgb = new Image<Bgr, byte>(input);
            Image<Lab, float> imgLab = imgRgb.Convert<Lab, float>();
            int width = imgLab.Width;
            int height = imgLab.Height;

            for (int i = 0; i < width; i++)
            {
                for (int j = 0; j < height; j++)
                {
                    if (((imgLab[j, i].X) * 100.0 / 255.0 >= 50.0) && ((imgLab[j, i].X) * 100.0 / 255.0 <= 70.0) && ((imgLab[j, i].Y) - 128 >= 67) && ((imgLab[j, i].Y) - 128 <= 75) && ((imgLab[j, i].Z) - 128 >= 60) && ((imgLab[j, i].Z) - 128 <= 80))
                    {
                        counter += 1;
                    }

                }
            }
            if (counter >= ((width * height) / 100) * 50)
            {
                value = true;
            }
            else
            {
                value = false;
            }


            //textBox2.Text = imgLab[50, 50].ToString();

            return value;
        }

        public static Image<Gray, byte> CheckColor(Image<Bgr, byte> original)
        {
            //var image = imgRgb.InRange(new Bgr(190, 190, 190), new Bgr(255, 255, 255));

            // 1. Convert the image to HSV
            using (Image<Hsv, byte> hsv = original.Convert<Hsv, byte>())
            {
                // 2. Obtain the 3 channels (hue, saturation and value) that compose the HSV image
                Image<Gray, byte>[] channels = hsv.Split();

                try
                {
                    // 3. Remove all pixels from the hue channel that are not in the range [40, 60]
                    CvInvoke.cvInRangeS(channels[0], new Gray(40).MCvScalar, new Gray(60).MCvScalar, channels[0]);

                    // 4. Display the result
                    //imageBox1.Image = channels[0];
                }
                finally
                {
                    channels[1].Dispose();
                    channels[2].Dispose();
                }
                return channels[0];
            }

        }
    }
}
