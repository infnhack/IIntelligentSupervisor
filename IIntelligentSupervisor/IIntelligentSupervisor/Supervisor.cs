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
        private FaceRecognition faceRec;

        private int lastPlayerCount = 0;
        private int frameCount = 0;
        private int notWearSmockFrames = 0;
        private bool alarmed = false;
        string nameRecognized = "unknown";

        private const int LEASTPIXELPERPERSON = 1000;
        
        public delegate void AlarmOccurHandler(object data);

        public event AlarmOccurHandler AlarmOccurEvent;

        public Supervisor(UIDataModel dm)
        {
            dataBuffer = new ConcurrentQueue<RawImageSource>();
            detector = new SmockDetector();
            dataModel = dm;

            faceRec = new FaceRecognition();
        }

        public void AddNewData(RawImageSource data)
        {
            dataBuffer.Enqueue(data);
            this.dataModel.ListLength = "" + dataBuffer.Count;
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

        public void HandleDataByBody(RawImageSource data)
        {
            int[] players = { 0, 0, 0, 0, 0, 0 };
            int[] xMin = { 0, 0, 0, 0, 0, 0 };
            int[] xMax = { 0, 0, 0, 0, 0, 0 };
            int[] yMin = { 0, 0, 0, 0, 0, 0 };
            int[] yMax = { 0, 0, 0, 0, 0, 0 };

            int playersCount = data.bodys.Count;

            if (playersCount == 0)
            {
                // players changed, new situation
                frameCount = 1;
                notWearSmockFrames = 0;
                alarmed = false;
                this.dataModel.Status = "NoPerson  by body";
                this.dataModel.Name = "";
            }


            if (playersCount > 0)
            {
                if (playersCount == lastPlayerCount)
                {
                    frameCount++;
                    this.dataModel.Status = "Person appear by body " + " " + playersCount + " " + frameCount + " " + notWearSmockFrames + (alarmed ? "alarm" : "no alarm");
                }
                if (playersCount != lastPlayerCount)
                {
                    // players changed, new situation
                    frameCount = 1;
                    notWearSmockFrames = 0;
                    alarmed = false;
                    this.dataModel.Name = "unknown";
                    this.dataModel.Status = "Person changed  by body";
                }


                for (int i = 0; i < data.bodys.Count; i++)
                {
                    xMin[i] = Math.Min(data.bodys[i].ShoulderLeft.X, data.bodys[i].ShoulderRight.X);
                    xMin[i] = Math.Min(xMin[i], data.bodys[i].HipLeft.X);
                    xMin[i] = Math.Min(xMin[i], data.bodys[i].HipRight.X);
                    if (xMin[i] < 0)
                        xMin[i] = 0;

                    xMax[i] = Math.Max(data.bodys[i].ShoulderLeft.X, data.bodys[i].ShoulderRight.X);
                    xMax[i] = Math.Max(xMax[i], data.bodys[i].HipLeft.X);
                    xMax[i] = Math.Max(xMax[i], data.bodys[i].HipRight.X);
                    if (xMax[i] > data.colorWidth)
                        xMax[i] = data.colorWidth;

                    yMin[i] = Math.Min(data.bodys[i].ShoulderCenter.Y, data.bodys[i].HipCenter.Y);
                    yMin[i] = Math.Min(yMin[i], data.bodys[i].HipLeft.Y);
                    yMin[i] = Math.Min(yMin[i], data.bodys[i].HipRight.Y);
                    if (yMin[i] < 0)
                        yMin[i] = 0;

                    yMax[i] = Math.Max(data.bodys[i].ShoulderCenter.Y, data.bodys[i].HipCenter.Y);
                    yMax[i] = Math.Max(yMax[i], data.bodys[i].HipLeft.Y);
                    yMax[i] = Math.Max(yMax[i], data.bodys[i].HipRight.Y);
                    if (yMax[i] > data.colorHeight)
                        yMax[i] = data.colorHeight;

                    if (xMax[i] > xMin[i] && yMax[i] > yMin[i])
                    {
                        using (Image<Bgr, byte> cvImage = data.colorPixels.ToBitmap(data.colorWidth, data.colorHeight, System.Drawing.Imaging.PixelFormat.Format32bppRgb).ToOpenCVImage<Bgr, byte>())
                        {
                            if (frameCount % 20 == 0)
                            {
                                // Recognize faces. the name will return by out parameter, it will be "UNKNOW" if not recognized.
                                string name;
                                Image<Bgr, byte> newFrame = faceRec.FaceRec(cvImage, out name);
                                if (name != "UNKNOW")
                                    this.dataModel.Name = name;
                            }

                            System.Drawing.Rectangle rect = new System.Drawing.Rectangle(xMin[i], yMin[i], xMax[i] - xMin[i],
                                yMax[i] - yMin[i]);
                            using (Image<Bgr, byte> humanArea = cvImage.Copy(rect))
                            {
                                if (!detector.CheckSmock(humanArea))
                                {
                                    // not wear a smock
                                    notWearSmockFrames++;
                                    if ((frameCount > 10 && notWearSmockFrames > 0.6 * frameCount)) // && (!alarmed))
                                    {
                                        this.dataModel.Status = "Alarm by body" + " " + frameCount + " " + notWearSmockFrames;
                                        alarmed = true;
                                        if (frameCount % 10 == 0)
                                        {
                                            System.Console.WriteLine("" + xMin[i] + "\t" + xMax[i] + "\t" + yMin[i] + "\t" + yMax[i]);
                                            if (AlarmOccurEvent != null)
                                                AlarmOccurEvent(humanArea.Resize(1.0, Emgu.CV.CvEnum.INTER.CV_INTER_LINEAR));
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }

            lastPlayerCount = playersCount;
        }

        public void HandleData(RawImageSource data)
        {

            if (data.hasbody)
            {
                HandleDataByBody(data);
            }
            else
            {
                HandleDataByDepth(data);
            }
        }

        public void HandleDataByDepth(RawImageSource data)
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
                    if (colorImagePoint.X < data.colorWidth && colorImagePoint.Y < data.colorHeight)
                    {
                        //int colorIndex = (int)(colorImagePoint.X + colorImagePoint.Y * this.colorBitmap.Width);
                        if (player >= 0)
                        {
                            xMin[player] = Math.Min(xMin[player], colorImagePoint.X);
                            xMax[player] = Math.Max(xMax[player], colorImagePoint.X);
                            yMin[player] = Math.Min(yMin[player], colorImagePoint.Y);
                            yMax[player] = Math.Max(yMax[player], colorImagePoint.Y);
                            players[player] += 1;
                        }
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
                this.dataModel.Name = "";
            }

            if (playersCount > 0)
            {
                if (playersCount == lastPlayerCount)
                {
                    frameCount++;
                    this.dataModel.Status = "Person appear " + frameCount + " " + notWearSmockFrames + (alarmed ? "alarm" : "no alarm");
                }
                if (playersCount != lastPlayerCount)
                {
                    // players changed, new situation
                    frameCount = 1;
                    notWearSmockFrames = 0;
                    alarmed = false;
                    this.dataModel.Name = "unknown";
                    this.dataModel.Status = "Person changed";
                }

                if (players[mostLikelyIndex] > LEASTPIXELPERPERSON)
                {
                    using (Image<Bgr, byte> cvImage = data.colorPixels.ToBitmap(data.colorWidth, data.colorHeight, System.Drawing.Imaging.PixelFormat.Format32bppRgb).ToOpenCVImage<Bgr, byte>())
                    {
                        if (frameCount % 20 == 0)
                        {
                            // Recognize faces. the name will return by out parameter, it will be "UNKNOW" if not recognized.
                            string name;
                            Image<Bgr, byte> newFrame = faceRec.FaceRec(cvImage, out name);
                            if (name != "UNKNOW")
                                this.dataModel.Name = name;
                        }

                        System.Drawing.Rectangle rect = new System.Drawing.Rectangle(xMin[mostLikelyIndex], yMin[mostLikelyIndex], xMax[mostLikelyIndex] - xMin[mostLikelyIndex],
                            yMax[mostLikelyIndex] - yMin[mostLikelyIndex]);
                        using (Image<Bgr, byte> humanArea = cvImage.Copy(rect))
                        {
                            if (!detector.CheckSmock(humanArea))
                            {
                                // not wear a smock
                                notWearSmockFrames++;
                                if ((frameCount > 10 && notWearSmockFrames > 0.6 * frameCount) && (!alarmed))
                                {
                                    this.dataModel.Status = "Alarm" + " " + frameCount + " " + notWearSmockFrames;
                                    alarmed = true;
                                    if (AlarmOccurEvent != null)
                                        AlarmOccurEvent(humanArea.Resize(data.colorWidth / 8, data.colorHeight / 8, Emgu.CV.CvEnum.INTER.CV_INTER_AREA));
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
