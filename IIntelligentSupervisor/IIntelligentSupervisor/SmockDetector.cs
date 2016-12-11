using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Emgu.CV;
using Emgu.CV.Structure;
using System.Drawing;
using Microsoft.Kinect;
using ImageManipulationExtensionMethods;

namespace IIntelligentSupervisor
{
    public class SmockDetector
    {
        public bool CheckSmock(Image<Bgr, byte> humanArea)
        {
            int totalPixel = humanArea.Width * humanArea.Height;
            int blueCount = CvInvoke.cvCountNonZero(this.IsBlueMost(humanArea));
            if (blueCount > totalPixel * 0.5)
                return true;
            else
                return false;
        }

        public Image<Gray, byte> IsBlueMost(Image<Bgr, byte> original)
        {
            return IsBlueMost(original, 90, 115);
        }

        public Image<Gray, byte> IsBlueMost(Image<Bgr, byte> original, double minScalar, double maxScalar)
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
                    CvInvoke.cvInRangeS(channels[0], new Gray(minScalar).MCvScalar, new Gray(maxScalar).MCvScalar, channels[0]);

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
