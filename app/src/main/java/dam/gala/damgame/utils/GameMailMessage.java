package dam.gala.damgame.utils;

public class GameMailMessage {
    private String mailTo,subject,body;

    public GameMailMessage(String mailTo, String subject, String body) {
        this.mailTo = mailTo;
        this.subject = subject;
        this.body = body;
    }

    public String getMailTo() {
        return mailTo;
    }

    public String getSubject() {
        return subject;
    }
    public String getBody() {
        return body;
    }
}
