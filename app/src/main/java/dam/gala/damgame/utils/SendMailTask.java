package dam.gala.damgame.utils;

import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMailTask extends AsyncTask<GameMailMessage,Integer,Boolean>{
    public boolean sendMail(GameMailMessage gameMailMessage){
        //cambiar username y password por una cuenta con permisos para
        final String username = "damgame21@iesantoniogala.es";
        final String password = "2021_emaG";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication
                    getPasswordAuthentication() {
                        return new
                                PasswordAuthentication(username,
                                password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(gameMailMessage.getMailTo()));
            message.setSubject(gameMailMessage.getSubject());
            message.setText(gameMailMessage.getBody());

            Transport.send(message);

            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected Boolean doInBackground(GameMailMessage... gameMailMessages) {
        return this.sendMail(gameMailMessages[0]);
    }
}
