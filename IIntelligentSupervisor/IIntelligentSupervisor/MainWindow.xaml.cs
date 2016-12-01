using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Kinect;

namespace IIntelligentSupervisor
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        KinectManager kinectManager;

        /// <summary>
        /// Bitmap that will hold color information
        /// </summary>
        private WriteableBitmap colorBitmap;

        public MainWindow()
        {
            InitializeComponent();

        }

        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            kinectManager = new KinectManager();
            if (kinectManager.InitializeSensor())
            {
                // This is the bitmap we'll display on-screen
                this.colorBitmap = new WriteableBitmap(kinectManager.sensor.ColorStream.FrameWidth, kinectManager.sensor.ColorStream.FrameHeight, 96.0, 96.0, PixelFormats.Bgr32, null);

                // Set the image we display to point to the bitmap where we'll put the image data
                this.Displayer.Source = this.colorBitmap;

                // sensor started
                kinectManager.DataReadyEvent += new KinectManager.DataReadyHandler(kinectManager_DataReadyEvent);
            }
            else
            {
                MessageBox.Show("Please connect Kinect first");
                this.Close();
            }
        }

        void kinectManager_DataReadyEvent(RawImageSource data)
        {
            if (data.colorSetted)
            {
                byte[] filterdColorPixels = new byte[data.colorPixels.Length];
                //Array.Clear(filterdColorPixels, 0, filterdColorPixels.Length);
                for (int j = 0; j < data.colorPixels.Length; j++)
                    filterdColorPixels[j] = 0xFF;

                int[] players = { 0, 0, 0, 0, 0, 0 };
                for (int y = 0; y < data.depthHeight; y++)
                {
                    for (int x = 0; x < data.depthWidth; x++)
                    {
                        int depthIndex = x + y * data.depthWidth;

                        DepthImagePixel depthPixel = data.depthPixels[depthIndex];

                        int player = depthPixel.PlayerIndex - 1;

                        ColorImagePoint colorImagePoint = data.colorCoordinates[depthIndex];
                        int colorIndex = (int)(colorImagePoint.X + colorImagePoint.Y * this.colorBitmap.Width);
                        if (player >= 0)
                        {
                            players[player] += 1;
                            int multiVar = (PixelFormats.Bgr32.BitsPerPixel + 7) / 8 * colorIndex;
                            //multiVar = kinectManager.sensor.ColorStream.FrameBytesPerPixel * colorIndex;
                            filterdColorPixels[multiVar] = data.colorPixels[multiVar];
                            filterdColorPixels[multiVar + 1] = data.colorPixels[multiVar + 1];
                            filterdColorPixels[multiVar + 2] = data.colorPixels[multiVar + 2];
                            filterdColorPixels[multiVar + 3] = data.colorPixels[multiVar + 3];
                        }
                    }
                }
                int mostLikelyIndex = 0;
                for (int j = 1; j < 6; j++)
                {
                    if (players[j] > players[j - 1])
                        mostLikelyIndex = j;
                }
                if (players[mostLikelyIndex] > 1000)
                {
                    this.colorBitmap.WritePixels(
                        new Int32Rect(0, 0, this.colorBitmap.PixelWidth, this.colorBitmap.PixelHeight),
                        filterdColorPixels,
                        this.colorBitmap.PixelWidth * sizeof(int),
                        0);
                }
            }
        }

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            MessageBox.Show("IS will close.");
            if (kinectManager != null)
                kinectManager.StopSensor();
        }


    }
}
