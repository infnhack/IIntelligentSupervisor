using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Windows.Media.Imaging;

namespace IIntelligentSupervisor
{
    class ImageUtil
    {
        public static byte[] LoadImage(string filename){ return null;}

        public static void SaveImage(BitmapSource image)
        {
            String fileName = DateTime.Now.ToString("yyyy-MM-dd-HH-mm-ss-ff") + ".jpg";
            SaveImage(image, fileName);
        }

        public static void SaveImage(BitmapSource image, string fileName)
        {
            if (File.Exists(fileName))
            {
                File.Delete(fileName);
            }

            using (FileStream savedSnapshot = new FileStream(fileName, FileMode.CreateNew))
            {
                JpegBitmapEncoder jpgEncoder = new JpegBitmapEncoder();
                jpgEncoder.QualityLevel = 70;
                jpgEncoder.Frames.Add(BitmapFrame.Create(image));
                jpgEncoder.Save(savedSnapshot);

                savedSnapshot.Flush();
                savedSnapshot.Close();
                savedSnapshot.Dispose();
            } 
        }
    }
}
