using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Mail;
using System.Net;


namespace ConsoleApplicationTest
{
    public static class EmailSender
    {
        public static void sendEmail() 
        {
            SmtpClient client = new SmtpClient("bruins.infinera.com"); //sv-smtp-prod2.infinera.com
            //client.Host = "bruins.infinera.com";
            //client.UseDefaultCredentials = true;
            //client.DeliveryMethod = SmtpDeliveryMethod.Network;
            client.Credentials = new NetworkCredential("jliu", "SUNlight2017");
            //client.EnableSsl = true;
            //client.
            MailMessage Message = new MailMessage();
            Message.From = new MailAddress("jLiu@infinera.com");
            Message.To.Add("jLiu@infinera.com");
            Message.Subject = "测试标体";
            Message.Body = "测试邮件体";
            Message.SubjectEncoding = Encoding.UTF8;
            Message.BodyEncoding = Encoding.UTF8;
            Message.Priority = MailPriority.Normal;
            Message.IsBodyHtml = true;
            try
            {
                client.Send(Message);
            }
            catch (SmtpException ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        public static void sendEmailQQ()
        {
            SmtpClient smtp = new SmtpClient("smtp.qq.com");
            // SMTP服务器用户名，密码
            smtp.Credentials = new NetworkCredential("jinguang.liu", "zaq12wsx");

            MailMessage mail = new MailMessage();
            mail.From = new MailAddress("jinguang.liu@qq.com");   // 发件人
            mail.To.Add("infinera@163.com");  // 收件人
            mail.Subject = "文件已经发给你了";
            mail.Body = "请及时查收";
            mail.BodyEncoding = Encoding.UTF8;
            mail.IsBodyHtml = false;
            mail.Priority = MailPriority.Normal;

            try
            {
                smtp.Send(mail);
                Console.WriteLine("发送成功");
            }
            catch (System.Net.Mail.SmtpException ex)
            {
                Console.WriteLine(ex.Message);
            }
        }
    }
}
