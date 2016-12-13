using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ConsoleApplicationTest
{
    public static class AccessRecord
    {
        public static void addRecord(String name, bool isValidate, String photoPath)
        {
            String datetime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
            int visitorId = Visitor.getId(name);

            String sqlStr = "insert into tb_user_access_records (user_id, photo_path, access_date, is_legal) values (" + visitorId + ", '" +
                photoPath + "', '" + datetime + "', " + (isValidate ? 1 : 0) + ");";
            Console.WriteLine(sqlStr); //2016/12/10 12:12:35

            DBUtils.insert(sqlStr);
            
        }
    }
}
