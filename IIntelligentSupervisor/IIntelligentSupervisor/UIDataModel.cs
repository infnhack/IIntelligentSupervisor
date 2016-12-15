using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ComponentModel;

namespace IIntelligentSupervisor
{
    public class UIDataModel : INotifyPropertyChanged
    {
        private string status = "Initialized";//私有  
        public string Status
        {
            get { return status; }
            set
            {
                status = value;
                RaisePropertyChanged("Status");
            }
        }

        private string name = "NotSet";
        public string Name
        {
            get { return name; }
            set
            {
                name = value;
                RaisePropertyChanged("Name");
            }
        }

        private string listLength = "NotSet";
        public string ListLength
        {
            get { return listLength; }
            set
            {
                listLength = value;
                RaisePropertyChanged("ListLength");
            }
        }

        public event PropertyChangedEventHandler PropertyChanged;
        protected void RaisePropertyChanged(string propertyName)
        {
            var handler = PropertyChanged;
            if (handler != null)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }
    }
}
