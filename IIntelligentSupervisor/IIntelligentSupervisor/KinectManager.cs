using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using System.Windows.Media.Imaging;
using System.Windows.Media;
using System.IO;

namespace IIntelligentSupervisor
{
    public class KinectManager
    {
        public delegate void DataReadyHandler(RawImageSource data);

        public event DataReadyHandler DataReadyEvent;

        /// <summary>
        /// Format we will use for the depth stream
        /// </summary>
        public const DepthImageFormat DepthFormat = DepthImageFormat.Resolution320x240Fps30;

        /// <summary>
        /// Format we will use for the color stream
        /// </summary>
        public const ColorImageFormat ColorFormat = ColorImageFormat.RgbResolution640x480Fps30;

        /// <summary>
        /// Active Kinect sensor
        /// </summary>
        public KinectSensor sensor;

        /// <summary>
        /// Bitmap that will hold color information
        /// </summary>
        private WriteableBitmap colorBitmap;

        /// <summary>
        /// Bitmap that will hold opacity mask information
        /// </summary>
        private WriteableBitmap playerOpacityMaskImage = null;

        /// <summary>
        /// Intermediate storage for the depth data received from the sensor
        /// </summary>
        private DepthImagePixel[] depthPixels;

        /// <summary>
        /// Intermediate storage for the color data received from the camera
        /// </summary>
        private byte[] colorPixels;

        /// <summary>
        /// Intermediate storage for the green screen opacity mask
        /// </summary>
        private int[] greenScreenPixelData;

        /// <summary>
        /// Intermediate storage for the depth to color mapping
        /// </summary>
        private ColorImagePoint[] colorCoordinates;
        private DepthImagePoint[] depthCoordinates;

        private Skeleton[] frameSkeletons;


        public bool InitializeSensor()
        {
            foreach (var potentialSensor in KinectSensor.KinectSensors)
            {
                if (potentialSensor.Status == KinectStatus.Connected)
                {
                    this.sensor = potentialSensor;
                    break;
                }
            }

            if (null != this.sensor)
            {
                // Turn on the depth stream to receive depth frames
                this.sensor.DepthStream.Enable(DepthFormat);

                // Allocate space to put the depth pixels we'll receive
                this.depthPixels = new DepthImagePixel[this.sensor.DepthStream.FramePixelDataLength];
                this.sensor.ColorStream.Enable(ColorFormat);

                // Allocate space to put the color pixels we'll create
                this.colorPixels = new byte[this.sensor.ColorStream.FramePixelDataLength];

                // Turn on to get player masks
                this.sensor.SkeletonStream.Enable();
                this.frameSkeletons = new Skeleton[this.sensor.SkeletonStream.FrameSkeletonArrayLength];

                // Add an event handler to be called whenever there is new depth frame data
                this.sensor.AllFramesReady += this.SensorAllFramesReady;

                // Start the sensor!
                try
                {
                    this.sensor.Start();
                }
                catch (IOException)
                {
                    this.sensor = null;
                }
            }

            return (null != this.sensor);
        }

        public void StopSensor()
        {
            if (null != this.sensor)
            {
                this.sensor.Stop();
                this.sensor = null;
            }
        }

        /// <summary>
        /// Event handler for Kinect sensor's DepthFrameReady event
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void SensorAllFramesReady(object sender, AllFramesReadyEventArgs e)
        {
            // in the middle of shutting down, so nothing to do
            if (null == this.sensor)
            {
                return;
            }

            RawImageSource rawData = new RawImageSource();
            rawData.Initialize(this.sensor);

            using (ColorImageFrame colorFrame = e.OpenColorImageFrame())
            {
                if (null != colorFrame)
                {
                    // Copy the pixel data from the image to a temporary array
                    colorFrame.CopyPixelDataTo(rawData.colorPixels);
                    rawData.colorSetted = true;
                }
            }

            using (DepthImageFrame depthFrame = e.OpenDepthImageFrame())
            {
                if (null != depthFrame)
                {
                    // Copy the pixel data from the image to a temporary array
                    depthFrame.CopyDepthImagePixelDataTo(rawData.depthPixels);
                    rawData.depthSetted = true;
                }
            }


            using (SkeletonFrame skeletonFrame = e.OpenSkeletonFrame())
            {
                if (null != skeletonFrame)
                {
                    skeletonFrame.CopySkeletonDataTo(rawData.frameSkeletons);
                    rawData.skeletonSetted = true;
                }
            }

            if (rawData.colorSetted || rawData.depthSetted || rawData.skeletonSetted)
                OnDataReadyEvent(rawData);
        }

        protected void OnDataReadyEvent(RawImageSource rawData)
        {
            if (this.DataReadyEvent != null)
            {
                DataReadyEvent(rawData);
            }
        }
    }
}
