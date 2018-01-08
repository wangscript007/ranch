package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author lpw
 */
@Service(PushModel.NAME + ".sender.smtp")
public class SmtpSenderImpl implements PushSender, ContextRefreshedListener {
    @Inject
    private Validator validator;
    @Inject
    private Json json;
    @Inject
    private Generator generator;
    @Inject
    private Context context;
    @Inject
    private Logger logger;
    @Value("${" + PushModel.NAME + ".sender.smtp:}")
    private String smtp;
    private List<Sender> senders;

    @Override
    public String getName() {
        return "smtp";
    }

    @Override
    public boolean send(String receiver, String subject, String content) {
        if (senders.isEmpty())
            return false;

        try {
            Sender sender = senders.get(generator.random(0, senders.size() - 1));
            MimeMessage message = new MimeMessage(sender.session);
            message.setFrom(new InternetAddress(sender.fromEmail, sender.fromName, context.getCharset(null)));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(subject, context.getCharset(null));
            message.setText(content, context.getCharset(null));
            Transport transport = sender.session.getTransport();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            return true;
        } catch (Exception e) {
            logger.warn(e, "发送SMTP邮件[{}:{}:{}]时发生异常！", receiver, subject, content);

            return false;
        }
    }

    @Override
    public int getContextRefreshedSort() {
        return 31;
    }

    @Override
    public void onContextRefreshed() {
        senders = new ArrayList<>();
        if (validator.isEmpty(smtp))
            return;

        JSONArray array = json.toArray(smtp);
        if (validator.isEmpty(array))
            return;

        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            Properties properties = new Properties();
            properties.put("mail.transport.protocol", getName());
            properties.put("mail.smtp.host", object.getString("host"));
            properties.put("mail.smtp.port", object.getString("port"));
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.user", object.getString("from"));
            senders.add(new Sender(Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(object.getString("user"), object.getString("password"));
                }
            }), object.getJSONObject("from")));
        }
    }

    private class Sender {
        private Session session;
        private String fromEmail;
        private String fromName;

        private Sender(Session session, JSONObject from) {
            this.session = session;
            fromEmail = from.getString("email");
            fromName = from.getString("name");
        }
    }
}
