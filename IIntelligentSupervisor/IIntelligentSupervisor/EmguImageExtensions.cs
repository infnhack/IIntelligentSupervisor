using Emgu.CV;
using Microsoft.Kinect;
using System.Drawing;
namespace ImageManipulationExtensionMethods
{
    public static class EmguImageExtensions
    {
        public static Image<TColor, TDepth> ToOpenCVImage<TColor, TDepth>(this ColorImageFrame image)
            where TColor : struct, IColor
            where TDepth : new()
        {
            var bitmap = image.ToBitmap();
            return new Image<TColor, TDepth>(bitmap);
        }

        public static Image<TColor, TDepth> ToOpenCVImage<TColor, TDepth>(this Bitmap bitmap)
            where TColor : struct, IColor
            where TDepth : new()
        {
            return new Image<TColor, TDepth>(bitmap);
        }

        public static System.Windows.Media.Imaging.BitmapSource ToBitmapSource(this IImage image)
        {
            var source = image.Bitmap.ToBitmapSource();
            return source;
        }
    }
}