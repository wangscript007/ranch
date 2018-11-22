package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.freemarker.Freemarker;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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
    private DateTime dateTime;
    @Inject
    private Freemarker freemarker;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private PushService pushService;
    @Value("${" + PushModel.NAME + ".sender.smtp:}")
    private String smtp;
    private List<Sender> senders;

    @Override
    public String getName() {
        return "smtp";
    }

    @Override
    public boolean send(PushModel push, String receiver, JSONObject args) {
        if (senders.isEmpty())
            return false;

        try {
            Sender sender = senders.get(generator.random(0, senders.size() - 1));
            MimeMessage message = new MimeMessage(sender.session);
            message.setFrom(new InternetAddress(sender.from, push.getName(), context.getCharset(null)));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(pushService.parse(PushService.Type.Subject, push.getKey(), push.getSubject(), args), context.getCharset(null));
            Multipart multipart = new MimeMultipart();
            MimeBodyPart content = new MimeBodyPart();
            content.setText(pushService.parse(PushService.Type.Content, push.getKey(), push.getContent(), args),
                    context.getCharset(null), "html");
            multipart.addBodyPart(content);
            if (args.containsKey("files")) {
                JSONArray files = args.getJSONArray("files");
                for (int i = 0, size = files.size(); i < size; i++) {
                    MimeBodyPart file = new MimeBodyPart();
                    String filename = files.getString(i);
                    file.setDataHandler(new DataHandler(new FileDataSource(filename)));
                    file.setFileName(filename.substring(filename.lastIndexOf('/') + 1));
                    multipart.addBodyPart(file);
                }
            }
            message.setContent(multipart);
            message.setSentDate(dateTime.today());
            Transport transport = sender.session.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            return true;
        } catch (Exception e) {
            logger.warn(e, "发送SMTP邮件[{}:{}:{}]时发生异常！", receiver, modelHelper.toJson(push), args);

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
            senders.add(new Sender(Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(object.getString("user"), object.getString("password"));
                }
            }), object.getString("from")));

            if (logger.isInfoEnable())
                logger.info("初始化SMTP接口[{}]完成。", object.toJSONString());
        }
    }

    private class Sender {
        private Session session;
        private String from;

        private Sender(Session session, String from) {
            this.session = session;
            this.from = from;
        }
    }
}
