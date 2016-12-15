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
using Emgu.CV;
using Emgu.CV.Structure;
using ImageManipulationExtensionMethods;

namespace IIntelligentSupervisor
{
    /// <summary>
    /// UserControlCapImage.xaml 的交互逻辑
    /// </summary>
    public partial class UserControlCapImage : UserControl
    {
        private string name;
        private BitmapSource imageSource;

        public UserControlCapImage(string name, Image<Bgr, byte> source)
        {
            InitializeComponent();
            this.name = name;
            this.imageSource = source.ToBitmapSource();
        }

        private void UserControl_Loaded(object sender, RoutedEventArgs e)
        {
            txtName.Text = name;
            imageFill.Source = this.imageSource;
            // 不要在设计时加载数据。
            // if (!System.ComponentModel.DesignerProperties.GetIsInDesignMode(this))
            // {
            // 	//在此处加载数据并将结果指派给 CollectionViewSource。
            // 	System.Windows.Data.CollectionViewSource myCollectionViewSource = (System.Windows.Data.CollectionViewSource)this.Resources["Resource Key for CollectionViewSource"];
            // 	myCollectionViewSource.Source = your data
            // }
        }
    }
}
