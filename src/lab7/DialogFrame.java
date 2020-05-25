package lab7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

//Класс личной переписки
public class DialogFrame extends JFrame {

    private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
    private static final int OUTGOING_AREA_DEFAULT_ROWS = 5;

    private static final int MEDIUM_GAP = 10;

    private static final int WIDTH = 300;
    private static final int HEIGHT = 400;

    private String date;
    private MainFrame frame;

    private final JTextArea textAreaIn;
    private final JTextArea textAreaOut;


    public DialogFrame(final User user, MainFrame frame){
        this.frame = frame;
        setTitle("беседа с " + user.getName());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth())/2,
                (kit.getScreenSize().height - getHeight())/2);


        textAreaIn = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
        textAreaIn.setEditable(false);
        final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIn);
        textAreaOut = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
        final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOut);
        final JPanel messagePanel = new JPanel();

        messagePanel.setBorder(BorderFactory.createTitledBorder("Сообщение"));
        final JButton sendButton = new JButton("Отправить");
        sendButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                date = frame.getDateTime();
                sendMessage(user);
            }
        });
        final GroupLayout layout2 = new GroupLayout(messagePanel);
        messagePanel.setLayout(layout2);
        layout2.setHorizontalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout2.createSequentialGroup())
                        .addComponent(scrollPaneOutgoing)
                        .addComponent(sendButton))
                .addContainerGap());
        layout2.setVerticalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE))
                .addGap(MEDIUM_GAP)
                .addComponent(scrollPaneOutgoing)
                .addGap(MEDIUM_GAP)
                .addComponent(sendButton)
                .addContainerGap());

        final GroupLayout layout1 = new GroupLayout(getContentPane());
        setLayout(layout1);
        layout1.setHorizontalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout1.createParallelGroup()
                        .addComponent(scrollPaneIncoming)
                        .addComponent(messagePanel))
                .addContainerGap());
        layout1.setVerticalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneIncoming)
                .addGap(MEDIUM_GAP)
                .addComponent(messagePanel)
                .addContainerGap());
        setVisible(true);

    }
    // метод отправки сообщений
    public void sendMessage(User user) {
        try {
            if(textAreaOut.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите текст сообщения", "Ошибка", 0);
                return;
            }

            Socket e = new Socket(user.getId(), frame.getServerPort());
            DataOutputStream out = new DataOutputStream(e.getOutputStream());
            out.writeUTF(frame.getLogin().getText());
            out.writeUTF(textAreaOut.getText());
            out.writeUTF(user.getName());
            out.writeUTF("true");
            e.close();
            textAreaIn.append(date + " Я: " + textAreaOut.getText() + "\n");
            textAreaOut.setText("");
        } catch (UnknownHostException var6) {
            var6.printStackTrace();
            JOptionPane.showMessageDialog(this, "Сообщение не было отправлено: адресс не найден", "Ошибка", 0);
        } catch (IOException var7) {
            var7.printStackTrace();
            JOptionPane.showMessageDialog(this, "Сообщение не было отправлено", "Ошибка", 0);
        }

    }

}