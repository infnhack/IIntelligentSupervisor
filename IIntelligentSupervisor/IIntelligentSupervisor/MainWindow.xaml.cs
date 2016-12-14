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
using ImageManipulationExtensionMethods;
using System.Threading;
using System.Collections.Concurrent;
using Emgu.CV;
using Emgu.CV.Structure;
using System.Drawing;

namespace IIntelligentSupervisor
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        KinectManager kinectManager;

        Supervisor sv;

        UIDataModel uiData;

        //public List<RawImageSource> dataBuffer;

        /// <summary>
        /// Bitmap that will hold color information
        /// </summary>
        private WriteableBitmap colorBitmap;

        private Image<Bgr, byte> imgUT = null;        

        public MainWindow()
        {
            InitializeComponent();
            
        }

        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            uiData = new UIDataModel();
            this.gridRoot.DataContext = uiData;
            sv = new Supervisor(uiData);
            sv.AlarmOccurEvent += new Supervisor.AlarmOccurHandler(sv_AlarmOccurEvent);
            kinectManager = new KinectManager();
            if (kinectManager.InitializeSensor())
            {
                // This is the bitmap we'll display on-screen
                this.colorBitmap = new WriteableBitmap(kinectManager.sensor.ColorStream.FrameWidth, kinectManager.sensor.ColorStream.FrameHeight, 96.0, 96.0, PixelFormats.Bgr32, null);

                // Set the image we display to point to the bitmap where we'll put the image data
                this.Displayer.Source = this.colorBitmap;

                // sensor started
                kinectManager.DataReadyEvent += new KinectManager.DataReadyHandler(kinectManager_DataReadyEvent);

                Thread bgWorker = new Thread(this.sv.DataHandleThreadMethod);
                bgWorker.Name = "Background Image Worker";
                bgWorker.IsBackground = true;
                bgWorker.Start();

                uiData.Status = "Kinect Connected";
            }
            else
            {
                uiData.Status = "Kinect Not Connected";
                MessageBox.Show("Please connect Kinect first and restart this application.");
                //this.Close();
            }
        }

        void sv_AlarmOccurEvent(object data)
        {
            //RawImageSource humanArea = (RawImageSource)data;
            this.Dispatcher.BeginInvoke((Action)delegate()
            {
                Image<Bgr, byte> humanArea = (Image<Bgr, byte>)data;
                //BitmapSource bs = humanArea.colorPixels.ToBitmapSource(PixelFormats.Bgr32, humanArea.colorWidth, humanArea.colorHeight);
                System.Windows.Controls.Image image = new System.Windows.Controls.Image();
                image.Source = humanArea.ToBitmapSource();
                this.panelPicList.Children.Add(image);
            }
            );
        }

        

        // 挪到线程处理
        void kinectManager_DataReadyEvent(RawImageSource data)
        {
            #region
            if (data.colorSetted)
            {
                this.Displayer.Source = data.colorPixels.ToBitmapSource(System.Windows.Media.PixelFormats.Bgr32, data.colorWidth, data.colorHeight);
                this.sv.AddNewData(data);
            }
            #endregion
        }

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            MessageBox.Show("IS will close.");
            if (kinectManager != null)
                kinectManager.StopSensor();
        }

        private void btnLoadImg_Click(object sender, RoutedEventArgs e)
        {
            // Create OpenFileDialog 
            Microsoft.Win32.OpenFileDialog dlg = new Microsoft.Win32.OpenFileDialog();



            // Set filter for file extension and default file extension 
            dlg.DefaultExt = ".jpg";
            dlg.Filter = "JPG Files (*.jpg)|*.jpg|JPEG Files (*.jpeg)|*.jpeg|PNG Files (*.png)|*.png|GIF Files (*.gif)|*.gif";


            // Display OpenFileDialog by calling ShowDialog method 
            Nullable<bool> result = dlg.ShowDialog();


            // Get the selected file name and display in a TextBox 
            if (result == true)
            {
                // Open document 
                string filename = dlg.FileName;
                this.txtImageFile.Text = filename;
                imgUT = new Image<Bgr, byte>(filename);
                this.LoadedImage.Source = imgUT.ToBitmapSource();
            }
        }

        private void btnCheckImg_Click(object sender, RoutedEventArgs e)
        {
            if (imgUT != null)
            {
                SmockDetector detector = new SmockDetector();
                double minValue = Convert.ToDouble(this.txtMinorValue.Text);
                double maxValue = Convert.ToDouble(this.txtMaxValue.Text);
                this.CheckedImage.Source = detector.IsBlueMost(imgUT, minValue, maxValue).ToBitmapSource();
            }        
        }

        private void btnTakePic_Click(object sender, RoutedEventArgs e)
        {
            BitmapSource bs = (BitmapSource)this.Displayer.Source;
            this.imgUT = bs.ToBitmap().ToOpenCVImage<Bgr, byte>();
            this.LoadedImage.Source = bs;
        }

        private void btnFaceRec_Click(object sender, RoutedEventArgs e)
        {
            // test2
            if (imgUT != null)
            {
                FaceRecognition faceRec = new FaceRecognition();
                string name;
                Image<Bgr, byte> newFrame = faceRec.FaceRec(imgUT, out name);
                this.CheckedImage.Source = newFrame.ToBitmapSource();
            }
        }


    }
}
