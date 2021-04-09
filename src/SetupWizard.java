import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class SetupWizard extends JFrame{
    private JPanel mainPanel;
    private JLabel serverLabel;
    private JLabel hostLabel;
    private JLabel portLabel;
    private JTextField portText;
    private JTextField hostText;
    private JLabel clientDirLabel;
    private JTextField dataDirText;
    private JButton defaultButton;
    private JButton generateNewButton;
    private JButton chooseButton;
    private JButton runClientButton;
    private JPanel serverPanel;

    private String mainDir = System.getProperty("user.home");
    private String defaultDir = "ClientData";

    private String dir = defaultDir;
    private String dataDir = mainDir + File.separator + dir;

    public SetupWizard(){
        startRefreshing();
        hostText.setText("localhost");
        portText.setText("2700");
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataDir = mainDir + File.separator + defaultDir;
            }
        });
        generateNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Files.exists(Path.of(dataDir))){
                    int i = 1;
                    dataDir = mainDir + File.separator + defaultDir + i;
                    while (Files.exists(Path.of(dataDir))){
                        dataDir = mainDir + File.separator + defaultDir + i;
                        i ++;
                    }
                }else dir = defaultDir;
            }
        });
        runClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = new Client(dataDir,hostText.getText(),Integer.parseInt(portText.getText()));
                JFrame frame = new JFrame(dataDir);
                frame.setContentPane(new LoginGUI(client).getLoginMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser(mainDir);
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                    File f = fc.getSelectedFile();
                    dataDir=f.getAbsolutePath();

                }
            }
        });
    }
    void startRefreshing(){
        Runnable listener = new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                        dataDirText.setText(dataDir);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(listener).start();
    }
    public static void main(String[] args){
        JFrame frame = new JFrame("Setup Wizard");
        frame.setContentPane(new SetupWizard().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String p = null;
            try {
                p = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (p.equals("x"))
                break;
            if (p.equals("d"))
                continue; //debug here
        }

    }
}
