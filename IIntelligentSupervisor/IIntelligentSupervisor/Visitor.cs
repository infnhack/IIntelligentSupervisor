using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;

namespace ConsoleApplicationTest
{
    public static class Visitor
    {
        public static int getId(String name)
        {
            String sqlStr = "select id from tb_users where name = '" + name + "'";
            MySqlConnection conn = DBUtils.getConnection();
            MySqlCommand sqlcmd = new MySqlCommand(sqlStr, conn);
            MySqlDataReader reader = null;
            int id = 0;

            conn.Open();

            reader = sqlcmd.ExecuteReader();
            try
            {
                while (reader.Read())
                {
                    if (reader.HasRows)
                    {
                        id = reader.GetInt32(0);
                        Console.WriteLine("Id: " + id);

                        break;
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Get result failed " + ex.Message);
            }
            finally
            {
                reader.Close();
            }
            conn.Close();

            return id;
        }
    }
}
