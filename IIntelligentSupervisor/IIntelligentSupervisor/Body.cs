using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;

namespace IIntelligentSupervisor
{
    public class Body
    {
        public ColorImagePoint Head, ShoulderCenter, ShoulderLeft, Spine, ShoulderRight, HipCenter, HipLeft, HipRight;

        public string ToString()
        {
            return "" + Head.X + "\t" + Head.Y + "\t"
                      + ShoulderLeft.X + "\t" + ShoulderLeft.Y + "\t"
                      + ShoulderRight.X + "\t" + ShoulderRight.Y + "\t"
                      + HipLeft.X + "\t" + HipLeft.Y + "\t"
                      + HipRight.X + "\t" + HipRight.Y + "\t";
        }

    }
}
