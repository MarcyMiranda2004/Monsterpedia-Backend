package Monsterpedia.it.Monsterpedia.service.notification;

import Monsterpedia.it.Monsterpedia.model.User;

public interface EmailService {
    void sendRegistrationConfirmation(User user);

    void sendPasswordChangedNotice(User user);

    void sendEmailChangeConfirmation(User user, String currentEmail, String newEmail );

    void sendDeleteAccountNotice(User user, String reason);

    void sendPasswordReset(User user, String token);

}
