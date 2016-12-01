using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;

namespace IIntelligentSupervisor
{
    public class RawImageSource
    {
        public RawImageSource()
        {
            colorSetted = false;
            depthSetted = false;
            skeletonSetted = false;
        }

        public void Initialize(KinectSensor sensor)
        {
            this.depthWidth = sensor.DepthStream.FrameWidth;
            this.depthHeight = sensor.DepthStream.FrameHeight;
            this.depthPixels = new DepthImagePixel[sensor.DepthStream.FramePixelDataLength];
            this.colorWidth = sensor.ColorStream.FrameWidth;
            this.colorHeight = sensor.ColorStream.FrameHeight;
            this.colorPixels = new byte[sensor.ColorStream.FramePixelDataLength];
            this.colorCoordinates = new ColorImagePoint[sensor.DepthStream.FramePixelDataLength];
            this.frameSkeletons = new Skeleton[sensor.SkeletonStream.FrameSkeletonArrayLength];

            sensor.CoordinateMapper.MapDepthFrameToColorFrame(KinectManager.DepthFormat
                , this.depthPixels, KinectManager.ColorFormat, this.colorCoordinates);
        }

        public RawImageSource(byte[] colorPixels, KinectSensor sensor)
        {
            colorSetted = true;
            depthSetted = false;
            skeletonSetted = false;

            this.depthWidth = sensor.DepthStream.FrameWidth;

            this.depthHeight = sensor.DepthStream.FrameHeight;

            this.colorWidth = sensor.ColorStream.FrameWidth;
            this.colorHeight = sensor.ColorStream.FrameHeight;

            this.colorPixels = new byte[colorPixels.Length];
            Array.Copy(colorPixels, this.colorPixels, colorPixels.Length);
        }

        public RawImageSource(byte[] colorPixels, DepthImagePixel[] depthPixels, KinectSensor sensor)
        {
            colorSetted = true;
            depthSetted = true;
            skeletonSetted = false;

            this.colorPixels = new byte[colorPixels.Length];
            Array.Copy(colorPixels, this.colorPixels, colorPixels.Length);

            this.depthPixels = new DepthImagePixel[depthPixels.Length];
            Array.Copy(depthPixels, this.depthPixels, depthPixels.Length);

            this.colorCoordinates = new ColorImagePoint[sensor.DepthStream.FramePixelDataLength];

            sensor.CoordinateMapper.MapDepthFrameToColorFrame(KinectManager.DepthFormat
                , this.depthPixels, KinectManager.ColorFormat, this.colorCoordinates);
        }

        public RawImageSource(byte[] colorPixels, DepthImagePixel[] depthPixels, Skeleton[] frameSkeletons, KinectSensor sensor)
        {
            colorSetted = true;
            depthSetted = true;
            skeletonSetted = true;

            this.colorPixels = new byte[colorPixels.Length];
            Array.Copy(colorPixels, this.colorPixels, colorPixels.Length);

            this.depthPixels = new DepthImagePixel[depthPixels.Length];
            Array.Copy(depthPixels, this.depthPixels, depthPixels.Length);

            this.frameSkeletons = new Skeleton[frameSkeletons.Length];
            Array.Copy(frameSkeletons, this.frameSkeletons, frameSkeletons.Length);

            this.colorCoordinates = new ColorImagePoint[sensor.DepthStream.FramePixelDataLength];

            sensor.CoordinateMapper.MapDepthFrameToColorFrame(KinectManager.DepthFormat
                , this.depthPixels, KinectManager.ColorFormat, this.colorCoordinates);
        }

        public bool colorSetted;
        public bool depthSetted;
        public bool skeletonSetted;


        /// <summary>
        /// Intermediate storage for the depth data received from the sensor
        /// </summary>
        public DepthImagePixel[] depthPixels;


        /// <summary>
        /// Intermediate storage for the color data received from the camera
        /// </summary>
        public byte[] colorPixels;


        /// <summary>
        /// Intermediate storage for the depth to color mapping
        /// </summary>
        public ColorImagePoint[] colorCoordinates;


        /// <summary>
        /// Width of the depth image
        /// </summary>
        public int depthWidth;

        /// <summary>
        /// Height of the depth image
        /// </summary>
        public int depthHeight;

        /// <summary>
        /// Width of the color image
        /// </summary>
        public int colorWidth;

        /// <summary>
        /// Height of the color image
        /// </summary>
        public int colorHeight;

        public Skeleton[] frameSkeletons;
    }
}
