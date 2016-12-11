using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections.Concurrent;
using Emgu.CV;
using Emgu.CV.Structure;
using ImageManipulationExtensionMethods;
using Microsoft.Kinect;
using System.Threading;

namespace IIntelligentSupervisor
{
    public class Supervisor
    {
        private SmockDetector detector;
        private ConcurrentQueue<RawImageSource> dataBuffer;
        private UIDataModel dataModel;

        private int lastPlayerCount = 0;
        private int frameCount = 0;
        private int lastPlayerPixelCount = 0;
        private int notWearSmockFrames = 0;
        private bool alarmed = false;

        private const int LEASTPIXELPERPERSON = 5000;

        public Supervisor(UIDataModel dm)
        {
            dataBuffer = new ConcurrentQueue<RawImageSource>();
            detector = new SmockDetector();
            dataModel = dm;
        }

        public void AddNewData(RawImageSource data)
        {
            dataBuffer.Enqueue(data);
        }

        public void DataHandleThreadMethod(object o)
        {
            SmockDetector detector = new SmockDetector();
            while (true)
            {
                if (!dataBuffer.IsEmpty)
                {
                    RawImageSource data;
                    if (dataBuffer.TryDequeue(out data))
                    {
                        HandleData(data);
                    }
                }
                Thread.Sleep(10);
            }
        }

        public void HandleData(RawImageSource data)
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
                    }
                }
            }

            int playersCount = 0;
            int mostLikelyIndex = 0;
            for (int j = 1; j < 6; j++)
            {
                if (players[j] > players[j - 1])
                    mostLikelyIndex = j;
                if (players[j - 1] > LEASTPIXELPERPERSON)
                {
                    playersCount++;
                }
            }

            if (playersCount == 0)
            {
                // players changed, new situation
                frameCount = 1;
                notWearSmockFrames = 0;
                alarmed = false;
                this.dataModel.Status = "NoPerson";
            }

            if (playersCount > 0)
            {
                if (playersCount == lastPlayerCount)
                {
                    frameCount++;
                    this.dataModel.Status = "The Same Person" + " " + frameCount + " " + notWearSmockFrames + (alarmed? "alarm" : "no alarm");
                }
                if (playersCount != lastPlayerCount)
                {
                    // players changed, new situation
                    frameCount = 1;
                    notWearSmockFrames = 0;
                    alarmed = false;
                    this.dataModel.Status = "Person changed";
                }

                if (players[mostLikelyIndex] > LEASTPIXELPERPERSON)
                {
                    using (Image<Bgr, byte> cvImage = data.colorPixels.ToBitmap(data.colorWidth, data.colorHeight, System.Drawing.Imaging.PixelFormat.Format32bppArgb).ToOpenCVImage<Bgr, byte>())
                    {
                        cvImage.ROI = new System.Drawing.Rectangle(xMin[mostLikelyIndex], yMin[mostLikelyIndex], xMax[mostLikelyIndex] - xMin[mostLikelyIndex],
                            yMax[mostLikelyIndex] - yMin[mostLikelyIndex]);
                        using (Image<Bgr, byte> humanArea = cvImage.Copy())
                        {
                            if (!detector.CheckSmock(humanArea))
                            {
                                // not wear a smock
                                notWearSmockFrames++;
                                if ( (frameCount > 10 && notWearSmockFrames > 0.6 * frameCount) && (!alarmed))
                                {
                                    this.dataModel.Status = "Alarm" + " " + frameCount + " " + notWearSmockFrames;
                                    alarmed = true;
                                }
                            }
                        }

                    }
                }
            }

            lastPlayerCount = playersCount;
        }

    }
}
