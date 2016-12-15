using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;

namespace ConsoleApplicationTest
{
    public static class DBUtils
    {
        public static MySqlConnection getConnection()
        {
            if (sqlConn == null)
            {
                Console.WriteLine("MySQL connection doesn't exist!");
                String mysqlStr = "Database=db_hackthon;Data Source=127.0.0.1;User Id=root;Password=infinera;pooling=false;CharSet=utf8;port=3306";

                sqlConn = new MySqlConnection(mysqlStr);
            }

            return sqlConn;
        }

        public static void insert(String sqlStr)
        {
            MySqlConnection conn = getConnection();
            MySqlCommand sqlcmd = new MySqlCommand(sqlStr, conn);

            conn.Open();

            try
            {
                sqlcmd.ExecuteNonQuery();
            }
            catch (Exception ex)
            {
                String message = ex.Message;
                Console.WriteLine("Insert entry failed" + message);
            }

            conn.Close();
        }

        public static void update(String sqlStr)
        {
            MySqlConnection conn = getConnection();
            MySqlCommand sqlcmd = new MySqlCommand(sqlStr, conn);

            conn.Open();

            try
            {
                sqlcmd.ExecuteNonQuery();
            }
            catch (Exception ex)
            {
                String message = ex.Message;
                Console.WriteLine("Update entry failed" + message);
            }

            conn.Close();
        }

        public static void delete(String sqlStr)
        {
            MySqlConnection conn = getConnection();
            MySqlCommand sqlcmd = new MySqlCommand(sqlStr, conn);

            conn.Open();

            try
            {
                sqlcmd.ExecuteNonQuery();
            }
            catch (Exception ex)
            {
                String message = ex.Message;
                Console.WriteLine("Delete entry failed" + message);
            }

            conn.Close();
        }

        public static MySqlDataReader getResult(String sqlStr)
        {
            MySqlConnection conn = getConnection();
            MySqlCommand sqlcmd = new MySqlCommand(sqlStr, conn);
            MySqlDataReader dataReader = null;

            conn.Open();

            try
            {
                dataReader = sqlcmd.ExecuteReader();
            }
            catch (Exception ex)
            {
                String message = ex.Message;
                Console.WriteLine("Get result failed" + message);
            }

            //conn.Close();

            return dataReader;
        }


        private static MySqlConnection sqlConn = null;
    }

    
}
