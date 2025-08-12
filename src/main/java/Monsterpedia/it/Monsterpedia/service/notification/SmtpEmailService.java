package Monsterpedia.it.Monsterpedia.service.notification;

import Monsterpedia.it.Monsterpedia.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public abstract class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;
    private final String noReplyAddress;

    public SmtpEmailService(JavaMailSender mailSender, @Value("${mail.no-reply}") String noReplyAddress) {
        this.mailSender = mailSender;
        this.noReplyAddress = noReplyAddress;
    }

    @Override
    public void sendRegistrationConfirmation(User user) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(noReplyAddress);
        msg.setTo(user.getEmail());
        msg.setSubject("Benvenuto in Arkadia Libris!");
        msg.setText("Ciao " + user.getUsername() + ",\n\n" +
                "Grazie per esserti registrato a MonsterPedia.\n\n" +
                "— Il team MonsterPedia");
        mailSender.send(msg);
    }

//    @Override
//    public void sendPasswordChangedNotice(User user) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(noReplyAddress);
//        msg.setTo(user.getEmail());
//        msg.setSubject("Arkadia Libris – Password aggiornata");
//        msg.setText("Ciao " + user.getUsername() + ",\n\n" +
//                "La tua password è stata aggiornata con successo.\n\n" +
//                "— Il team MonsterPedia");
//        mailSender.send(msg);
//    }

//    @Override
//    public void sendEmailChangeConfirmation(User user, String newEmail, String currentEmail) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(noReplyAddress);
//        msg.setTo(currentEmail, newEmail);
//        msg.setSubject("Arkadia Libris – Email aggiornata");
//        msg.setText("Ciao " + user.getUsername() + ",\n\n" +
//                "Il tuo indirizzo email è stato modificato da " + currentEmail + " a " + newEmail +  ".\n\n" +
//                "— Il team MonsterPedia");
//        mailSender.send(msg);
//    }

//    @Override
//    public void sendDeleteAccountNotice(User user, String reason) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(noReplyAddress);
//        msg.setTo(user.getEmail());
//        msg.setSubject("Arkadia Libris – Account eliminato");
//        msg.setText("Ciao " + user.getUsername() + ",\n\n" +
//                "Il tuo account è stato eliminato. Motivo: " + reason + ".\n\n" +
//                "— Il team MonsterPedia");
//        mailSender.send(msg);
//    }

//    @Override
//    public void sendPasswordReset(User user, String token) {
//        // punta al  server, non ancora alla pagina web, DA AGGIORNARE
//        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(noReplyAddress);
//        msg.setTo(user.getEmail());
//        msg.setSubject("Arkadia Libris – Reset Password");
//        msg.setText(
//                "Ciao " + user.getUsername() + ",\n\n" +
//                        "Abbiamo ricevuto una richiesta di reset password.\n" +
//                        "Clicca sul link qui sotto per impostare una nuova password:\n\n" +
//                        resetLink + "\n\n" +
//                        "Se non hai richiesto tu il reset, ignora questa email.\n\n" +
//                        "— Il team MonsterPedia"
//        );
//        mailSender.send(msg);
//    }


}
