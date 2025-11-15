package projeto_amor_e_acao.TCC.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(String para, String assunto, String mensagem) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(para);
            mail.setSubject(assunto);
            mail.setText(mensagem);
            mail.setFrom("projetoamoreacaolinks@gmail.com");

            mailSender.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
