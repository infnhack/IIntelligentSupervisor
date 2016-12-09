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
        public bool CheckSmock(RawImageSource data)
        {
            int[] players = { 0, 0, 0, 0, 0, 0 };
            int[] xMin = { 0, 0, 0, 0, 0, 0 };
            int[] xMax = { 0, 0, 0, 0, 0, 0 };
            int[] yMin = { 0, 0, 0, 0, 0, 0 };
            int[] yMax = { 0, 0, 0, 0, 0, 0 };

            for (int y = 0; y < data.depthHeight; y++)
            {
                for (int x = 0; x < data.depthWidth; x++)
                {
                    int depthIndex = x + y * data.depthWidth;

                    DepthImagePixel depthPixel = data.depthPixels[depthIndex];

                    int player = depthPixel.PlayerIndex - 1;

                    ColorImagePoint colorImagePoint = data.colorCoordinates[depthIndex];
                    //int colorIndex = (int)(colorImagePoint.X + colorImagePoint.Y * this.colorBitmap.Width);
                    if (player >= 0)
                    {
                        xMin[player] = Math.Min(xMin[player], colorImagePoint.X);
                        xMax[player] = Math.Max(xMax[player], colorImagePoint.X);
                        yMin[player] = Math.Min(xMin[player], colorImagePoint.Y);
                        yMax[player] = Math.Max(xMax[player], colorImagePoint.Y);
                        players[player] += 1;
                        //int multiVar = (PixelFormats.Bgr32.BitsPerPixel + 7) / 8 * colorIndex;
                        ////multiVar = kinectManager.sensor.ColorStream.FrameBytesPerPixel * colorIndex;
                        //filterdColorPixels[multiVar] = data.colorPixels[multiVar];
                        //filterdColorPixels[multiVar + 1] = data.colorPixels[multiVar + 1];
                        //filterdColorPixels[multiVar + 2] = data.colorPixels[multiVar + 2];
                        //filterdColorPixels[multiVar + 3] = data.colorPixels[multiVar + 3];
                    }
                }
            }
            int mostLikelyIndex = 0;
            for (int j = 1; j < 6; j++)
            {
                if (players[j] > players[j - 1])
                    mostLikelyIndex = j;
            }
            Image<Bgr, byte> cvImage = data.colorPixels.ToBitmap(data.colorWidth, data.colorHeight, System.Drawing.Imaging.PixelFormat.Format32bppArgb).ToOpenCVImage<Bgr, byte>();
            cvImage.ROI = new Rectangle(xMin[mostLikelyIndex], yMin[mostLikelyIndex], xMax[mostLikelyIndex] - xMin[mostLikelyIndex],
                yMax[mostLikelyIndex] - yMin[mostLikelyIndex]);
            Image<Bgr, byte> humanArea = cvImage.Copy();
            return true;
        }

        public bool IsBlueMost(Bitmap input)
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
