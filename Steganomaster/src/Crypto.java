import Steganography.Steganography;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigInteger;
import static java.awt.TextArea.SCROLLBARS_BOTH;
import java.security.*;
import java.text.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.*;
import java.util.logging.*;

@SuppressWarnings("serial")
public class Crypto extends JFrame {
  private JPanel contentPane;
  public String password = null, user_details = null, encrypted_data = null, tmp_buff = null;
  byte[] encrypted = null, decrypted = null;
  private JTextField target_image_textField;

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Crypto frame = new Crypto();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public Crypto() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(Crypto.class.getResource("/Images/logo.png")));
    setResizable(false);
    setTitle("Steganomaster");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 100, 975, 566);
    contentPane = new JPanel();
    // Image img = Toolkit.getDefaultToolkit().createImage("/Images/Logo1.jpeg");

    contentPane.setBackground(new Color(46, 64, 83));
    contentPane.setBorder(new EmptyBorder(50, 50, 50, 50));
    setContentPane(contentPane);
    contentPane.setLayout(null);
    UIManager.put("TabbedPane.selected", new Color(241, 196, 15));

    // tabbedPane_1 = new JTabbedPane();
    JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.LEFT);
    tabbedPane_1.setBounds(10, 11, 945, 515);
    contentPane.add(tabbedPane_1);

    // Starting Pane for Welcome Window
    String wel = "WELCOME TO APP_NAME\n" +
        "\n" +
        "1. Encrypt simple text messages or paragraphs into a\n" +
        "   secret format which can be encrypted by a key\n" +
        "   only you have!!\n" +
        "2. Image Steganography i.e. hiding text behind an image!!\n" +
        "\n" +
        "•  There are two main functional tabs:\n" +
        "1. Text Encryption\n" +
        "2. Image Steganography\n" +
        "   Select them according to the need.\n" +
        "\n" +
        "•  For Text Encryption:\n" +
        "\n" +
        "   •There are two modes:Encrypt and Decrypt.\n" +
        "   •For Text Encrytion,select Encrypt from dropdown menu.\n" +
        "   •Write/copy the text you want to encrypt in the user\n" +
        "     area" +
        "(having yellow background).\n" +
        "   •For limiting the access to only you,create a key by\n" +
        "     clicking Gen key button.Or you can type your own key \n" +
        "     in the lower right textbox given.\n" +
        "   •For saving your key,click Save Key Button.(Your key \n" +
        "     will be stored in a text file in the Application\n" +
        "     folder).\n" +
        "   •Click GO button to encypt the text.(Encrypted text will\n" +
        "     be saved in a text file in app folder as well as it will" +
        "\n     appear on" + "screen-user area).\n" +
        "   •You can use Clear Text Box for clearing user text area.\n" +
        "   •All the processes going on will be shown in Debug area\n" +
        "     present at bottom.\n" +
        "   •If you want to insert your encoded text output into an\n" +
        "     image,click Hide In Image button.This will redirect you  \n" +
        "     to Image Steganography Tab.\n" +
        "   •For Decryption,select Decrypt mode from the dropdown\n" +
        "     menu.\n" +
        "   •Now,copy the encoded text you want to decrypt in the\n" +
        "     user area.\n" +
        "   •Click Load Key and select the text file which  contains\n" +
        "     your key.(Go  to home directory in opened dialog box by\n" +
        "     clicking the home-symbol button).\n" +
        "   •Then click GO button.Decrypted text will appear in\n " +
        "     user area.\n" +
        "   \n" +
        "• For Image Steganography:\n" +
        "\n" +
        "   •There are two modes:Insert and Extract.\n" +
        "   •For insertion of text into image select Insert mode.\n" +
        "   •Write/copy the text you want to insert into image in\n" +
        "     user area.\n" +
        "   •Then click Image Input button,a dialog box opens.Select\n" +
        "     a jpeg/png image to insert text.\n" +
        "   •And click GO button.The text will be inserted into image.\n" +
        "     This edited image containing message will be saved\n" +
        "     in the same loaction as that of the original image with\n" +
        "     same name having copy suffix.\n" +
        "   •If you wish to encode the text before inserting it into \n" +
        "     image,click Encode Before Hide Button.This will redirect\n" +
        "     you to the Text Encryption tab and the text will also \n" +
        "     appear in the text encryption user area,and then follow \n" +
        "     the same steps as described for text encryption.\n" +
        "   •Here too, all processes will be notified in debug area \n " +
        "     present at bottom.\n" +
        "   •For Extracting text from image,select Extract mode.\n" +
        "   •Click Image Input button,select the editted image \n" +
        "     from the location of application and click GO button.\n" +
        "    (Text in image will be displayed in user area).";

    ImageIcon i1 = new ImageIcon(Crypto.class.getResource("/Images/welcome.png"));
    JPanel welcome = new JPanel();
    tabbedPane_1.addTab("Welcome", i1, welcome, null);

    welcome.setLayout(null);

    // TEXT AREA
    TextArea t1 = new TextArea(wel);
    t1.setFont(new Font("Monospaced", Font.PLAIN, 17));
    // t1.setText("Hide Text within images, with this simple tool!");
    t1.setBounds(0, 0, 617, 510);
    t1.setBackground(Color.BLACK);
    t1.setForeground(Color.WHITE);
    t1.setEditable(false);
    welcome.add(t1);
    // TEXTAREA OVER

    // Starting Pane for Text Ecryption
    JPanel text_encryption_panel = new JPanel();
    tabbedPane_1.addTab("Text Encryption", null, text_encryption_panel, null);
    text_encryption_panel.setLayout(null);
    text_encryption_panel.setForeground(new Color(0, 0, 0));

    // create the debugging area
    TextArea debug = new TextArea();
    debug.setBounds(10, 400, 600, 99);
    text_encryption_panel.add(debug);
    debug.setFont(new Font("Verdana", Font.PLAIN, 14));
    debug.setBackground(Color.BLACK);
    debug.setForeground(Color.WHITE);
    debug.setEditable(false);

    // Create the choice options
    Choice choice = new Choice();
    choice.setBounds(10, 323, 193, 20);
    choice.add("Encrypt");
    choice.add("Decrypt");
    text_encryption_panel.add(choice);

    // Create and a key for the user on startup!
    TextField passwordField = new TextField();
    passwordField.setBounds(212, 323, 176, 25);
    text_encryption_panel.add(passwordField);
    passwordField.setEditable(true);

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setBounds(10, 11, 600, 285);
    text_encryption_panel.add(scrollPane);

    // area for the user to add text to be encrypted
    JTextArea user_input = new JTextArea("Welcome To Crypto, Update the Text Here!");
    user_input.setBackground(new Color(255, 255, 153));
    user_input.setFont(new Font("Tahoma", Font.PLAIN, 14));

    // user_input.setText("Welcome To Crypto, Update the Text Here!");
    user_input.setToolTipText("Please Input Text HERE");
    scrollPane.setViewportView(user_input);

    JLabel lblNewLabel = new JLabel("Save OR Enter a Key");
    lblNewLabel.setBounds(212, 300, 170, 14);
    text_encryption_panel.add(lblNewLabel);

    JLabel lblDebugoutput = new JLabel("Debug_Output");
    lblDebugoutput.setBounds(10, 378, 88, 19);
    text_encryption_panel.add(lblDebugoutput);
    lblDebugoutput.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 9));

    JLabel lblMode = new JLabel("Mode");
    lblMode.setBounds(10, 300, 200, 24);
    text_encryption_panel.add(lblMode);

    // GO BUTTON
    JButton GO_button = new JButton("GO");
    GO_button.setBounds(399, 323, 50, 26);
    text_encryption_panel.add(GO_button);
    GO_button.setBackground(new Color(250, 200, 250));
    GO_button.setBorder(new RoundedBorder(10));// 10 is the radius

    GO_button.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    GO_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {

        String current_mode = choice.getSelectedItem();// picking the current mode
        String user_details = user_input.getText();
        String password = passwordField.getText();
        if (user_details == null || user_details.isEmpty()) {
          user_details = null;
        }
        if (password == null || password.isEmpty()) {
          password = null;
        }
        // Debugging data out to the user!
        debug.append("Running using Mode: " + current_mode + "\n");// debug out to the user..
        if (user_details == null || password == null) {// Check that the user has given a password and Text data!
          debug.append("### ERROR, Please make sure you have entered a Private Key and Encryptable Text ### \n");
          JOptionPane.showMessageDialog(null, "Please make sure you have entered a Private Key and Encryptable Text");
          throw new EmptyStackException();
        }
        debug.append("ingesting your Key...\n");
        Key aesKey = new SecretKeySpec(password.getBytes(), "AES");
        try {
          Cipher cipher = Cipher.getInstance("AES");
          if (current_mode == "Encrypt") {
            debug.append("Working on Encrypting data\n");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(user_details.getBytes());
            tmp_buff = new String(Base64.getEncoder().encodeToString(encrypted));
            user_input.setText(tmp_buff);
            // System.out.println(tmp_buff);
            DateFormat df = new SimpleDateFormat("dd-MM-yy_HH-mm-ss");
            Calendar calobj = Calendar.getInstance();
            String f_name = "Encoded_txt_file_" + df.format(calobj.getTime()) + ".txt";
            debug.append("Encoded Text saved in file: " + f_name + "\n");
            Path path = Paths.get(f_name);
            Files.write(path, tmp_buff.getBytes());

          }
          if (current_mode == "Decrypt") {
            debug.append("Working on Decrypting data\n");
            byte[] encrypted = Base64.getDecoder().decode(user_details);
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(encrypted));
            user_input.setText(decrypted);
          }
          debug.append("##################### END OF CODE #####################\n");
        } catch (Exception e) {
          debug.append(e.toString() + "\n");
        }
      }
    });
    // GO BUTTON OVER

    JButton Move_button = new JButton("Hide In Image");
    Move_button.setBounds(457, 323, 120, 26);

    text_encryption_panel.add(Move_button);
    Move_button.setBackground(new Color(200, 200, 255));
    Move_button.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    Move_button.setBorder(new RoundedBorder(10)); // 10 is the radius

    // SAVE BUTTON
    JButton Save_button = new JButton("Save Key");
    Save_button.setBounds(212, 353, 88, 26);
    Save_button.setBorder(new RoundedBorder(10)); // 10 is the radius

    text_encryption_panel.add(Save_button);
    Save_button.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    Save_button.setBackground(new Color(200, 200, 255));
    Save_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String save_pass = passwordField.getText();
        try {
          DateFormat df = new SimpleDateFormat("dd-MM-yy_HH-mm-ss");
          Calendar calobj = Calendar.getInstance();
          String file_name = df.format(calobj.getTime()) + ".txt";
          PrintWriter writer = new PrintWriter(file_name, "UTF-8");
          writer.println(save_pass);
          debug.append("Saved Key to File: " + file_name + "\n");
          writer.close();
        } catch (FileNotFoundException e1) {
          debug.append("### Saving Error: FileNotFoundException\n");
        } catch (UnsupportedEncodingException e1) {
          debug.append("### Saving Error: UnsupportedEncodingException\n");
        }
      }
    });
    // SAVE BUTTON OVER

    // GENERATE BUTTON
    JButton Gen_button = new JButton("Gen Key");
    Gen_button.setBorder(new RoundedBorder(10)); // 10 is the radius

    Gen_button.setBounds(399, 353, 88, 26);
    text_encryption_panel.add(Gen_button);
    Gen_button.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    Gen_button.setBackground(new Color(200, 200, 255));
    Gen_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        debug.append("Created a new Key!\n");
        KeyGenerator gen = null;
        try {
          gen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e1) {
          e1.printStackTrace();
        }
        gen.init(128); /* 128-bit AES */
        SecretKey secret = gen.generateKey();
        byte[] binary = secret.getEncoded();
        String key = String.format("%032X", new BigInteger(+1, binary));
        key = key.substring(0, Math.min(key.length(), 16));
        passwordField.setText(key);
      }
    });
    // GENERATE BUTTON OVER

    // LOAD BUTTON
    JButton Load_button = new JButton("Load Key");
    Load_button.setBorder(new RoundedBorder(10)); // 10 is the radius

    Load_button.setBounds(305, 353, 88, 26);
    text_encryption_panel.add(Load_button);
    Load_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        File workingDirectory = new File(System.getProperty("user.dir"));
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(workingDirectory);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          try (BufferedReader buffer = new BufferedReader(new FileReader(selectedFile))) {
            String firstLine = buffer.readLine();
            passwordField.setText(firstLine);
          } catch (IOException ex) {
            debug.append("### Loading Error: IOException\n");
          }
        }
      }
    });
    Load_button.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    Load_button.setBackground(new Color(200, 200, 255));
    // LOAD BUTTON OVER

    // CLEAR BUTTON
    JButton Clear_button = new JButton("Clear Text Box");
    Clear_button.setBorder(new RoundedBorder(10)); // 10 is the radius

    Clear_button.setForeground(new Color(0, 0, 0));
    Clear_button.setBounds(492, 353, 120, 26);
    text_encryption_panel.add(Clear_button);
    Clear_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        user_input.setText(" ");
        debug.append("Cleared the Text Box!\n");
      }
    });
    Clear_button.setBackground(new Color(200, 200, 255));
    Clear_button.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    // CLEAR BUTTON OVER

    KeyGenerator gen = null;
    try {
      gen = KeyGenerator.getInstance("AES");
    } catch (NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    }
    gen.init(128); /* 128-bit AES */
    SecretKey secret = gen.generateKey();
    byte[] binary = secret.getEncoded();
    String key = String.format("%032X", new BigInteger(+1, binary));
    key = key.substring(0, Math.min(key.length(), 16));

    // Starting Pane for steganography
    JPanel image_steganography = new JPanel();
    tabbedPane_1.addTab("Image Steganography", null, image_steganography, null);
    image_steganography.setLayout(null);
    image_steganography.setForeground(new Color(0, 0, 0));

    Choice stg_set_mode = new Choice();
    stg_set_mode.setBounds(10, 342, 182, 45);
    stg_set_mode.add("Insert");
    stg_set_mode.add("Extract");
    image_steganography.add(stg_set_mode);

    TextArea stg_debug_area = new TextArea();
    stg_debug_area.setFont(new Font("Verdana", Font.PLAIN, 14));
    stg_debug_area.setBounds(10, 400, 600, 99);
    stg_debug_area.setBackground(Color.BLACK);
    stg_debug_area.setForeground(Color.WHITE);
    stg_debug_area.setEditable(false);
    image_steganography.add(stg_debug_area);

    // TEXT AREA
    TextArea steg_textArea = new TextArea("Hide Text within images, with this simple tool!");
    steg_textArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
    // steg_textArea.setText("Hide Text within images, with this simple tool!");
    steg_textArea.setBounds(10, 10, 600, 274);
    steg_textArea.setBackground(new Color(255, 255, 153));
    image_steganography.add(steg_textArea);
    // TEXTAREA OVER

    Move_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String copy_enc = user_input.getText();
        steg_textArea.setText(copy_enc);
        debug.append("Moving to other panel!\n");
        int selectedIndex = tabbedPane_1.getSelectedIndex();
        tabbedPane_1.setSelectedIndex(selectedIndex + 1);
      }
    });

    // TARGET IMGE FIELD
    target_image_textField = new JTextField();
    target_image_textField.setToolTipText("Path to an image");
    target_image_textField.setEditable(false);
    target_image_textField.setBounds(120, 289, 377, 28);
    image_steganography.add(target_image_textField);
    target_image_textField.setColumns(16);
    // TARGET IMAGE FIELD OVER

    // TARGET IMAGE BUTTON
    JButton target_image_button = new JButton("Image Input");
    target_image_button.setBorder(new RoundedBorder(10)); // 10 is the radius

    target_image_button.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    target_image_button.setForeground(new Color(0, 0, 0));
    target_image_button.setBackground(new Color(200, 200, 250));
    target_image_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        File workingDirectory = new File(System.getProperty("user.dir"));
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(workingDirectory);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File targetimageFile = fileChooser.getSelectedFile();
          stg_debug_area.append("Using File: " + targetimageFile.toString() + "\n");
          target_image_textField.setText(targetimageFile.toString());
        }
      }
    });
    target_image_button.setBounds(10, 289, 99, 30);
    image_steganography.add(target_image_button);
    // TARGET IMAGE BUTTON OVER

    // CLEAR BUTTON
    JButton clear_text_box = new JButton("Clear Text Box");
    clear_text_box.setBorder(new RoundedBorder(10)); // 10 is the radius

    clear_text_box.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    clear_text_box.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        stg_debug_area.append("Clearing the Text Box!\n");
        steg_textArea.setText(" ");
      }
    });
    clear_text_box.setBackground(new Color(200, 200, 250));
    clear_text_box.setBounds(203, 342, 99, 30);
    image_steganography.add(clear_text_box);
    // CLEAR BUTTON OVER

    // GO BUTTON
    JButton stg_go_button = new JButton("GO");
    stg_go_button.setBorder(new RoundedBorder(10)); // 10 is the radius

    stg_go_button.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    stg_go_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String image_path = target_image_textField.getText();
        // System.out.println("img path="+image_path);

        if (!image_path.contains(".jpg") && !image_path.contains(".png") && !image_path.contains(".jpeg")) {
          JOptionPane.showMessageDialog(null, "Please make sure you have given the path to an image!");
          target_image_textField.setText("");
          throw new EmptyStackException();
        }

        Steganography steg = new Steganography();
        String srg_current_mode = stg_set_mode.getSelectedItem();// picking the current mode
        if (srg_current_mode == "Insert") {
          stg_debug_area.append("Working on Inserting.\n");
          String message_text = steg_textArea.getText();
          String dest_loc = steg.encode(image_path, "Testing_File", message_text);
          steg_textArea.setText("");
          stg_debug_area.append("Done Inserting the Text, into Image at Location =" + dest_loc + "\n");
        }
        if (srg_current_mode == "Extract") {
          stg_debug_area.append("Working on Extracting Text from your image.\n");
          String decoded_message = steg.decode(image_path);
          steg_textArea.setText("############### Found Data ###############\n" + decoded_message);
          stg_debug_area.append("Done Extracting the Text, from your image.\n");
        }
      }
    });
    stg_go_button.setBackground(new Color(250, 200, 250));
    stg_go_button.setBounds(310, 342, 50, 30);// 203, 342, 70, 30;
    image_steganography.add(stg_go_button);
    // GO BUTTON OVER

    // HELP BUTTON
    JButton help_button = new JButton("Help");
    help_button.setBorder(new RoundedBorder(10)); // 10 is the radius

    help_button.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    help_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stg_debug_area.append("Getting help information.\n");
        String help_text = "                                           Need Some Help?\n"
            + "1. Type out the Text you want to hide in the image\n"
            + "2. Select a Target image using the 'Target Image Button', This has to be a (.jpg or .png)\n"
            + "3. Pick your mode (Insert / Extract)\n"
            + "4. Then simply Hit 'Go' and your text will be hidden or shown\n";
        JOptionPane.showMessageDialog(null, help_text);
      }
    });
    help_button.setBackground(new Color(200, 200, 250));
    help_button.setBounds(368, 342, 60, 30);
    image_steganography.add(help_button);
    // HELP BUTTON OVER

    // ENCODE BUTTON
    JButton encode_prior = new JButton("Encode Before Hide");
    encode_prior.setBorder(new RoundedBorder(10)); // 10 is the radius

    encode_prior.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 12));
    encode_prior.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stg_debug_area.append("Moving to other panel!\n");
        String copy_str = steg_textArea.getText();
        user_input.setText(copy_str);
        int selectedIndex = tabbedPane_1.getSelectedIndex();
        tabbedPane_1.setSelectedIndex(selectedIndex - 1);
      }
    });
    encode_prior.setBackground(new Color(200, 200, 250));
    encode_prior.setBounds(436, 342, 150, 30);
    image_steganography.add(encode_prior);
    // ENCODE BUTTON OVER

    // LABEL MODE
    JLabel mode_lab = new JLabel("Mode");
    mode_lab.setBounds(10, 322, 150, 14);
    image_steganography.add(mode_lab);

    // Starting Pane for About Window
    ImageIcon i2 = new ImageIcon(Crypto.class.getResource("/Images/6.png"));
    JPanel about = new JPanel();
    tabbedPane_1.addTab("About", i2, about, null);
    about.setLayout(null);
    tabbedPane_1.setBackgroundAt(0, new Color(171, 178, 185));
    tabbedPane_1.setBackgroundAt(1, new Color(171, 178, 185));
    tabbedPane_1.setBackgroundAt(2, new Color(171, 178, 185));
    tabbedPane_1.setBackgroundAt(3, new Color(171, 178, 185));

    // textarea
    String ab = "      ABOUT\n" +
        "\n" +
        " •Its very important to secure confidential data\n" +
        "   and controll its accesebility.\n" +
        " •Using Steganography and cryptography in Steganography \n" +
        "   are some of the tools used to insert confidential data\n" +
        "   into images so that it is assumed that by the third\n" +
        "   person that the sender is sending a random image to \n" +
        "   receiver but" + "   in reality a secret message/data \n" +
        "   gets transferred.\n" +
        " •CryptoMaster provides a platform to insert text into \n" +
        "   images and also extract messages from such images.\n" +
        " •Also CryptoMaster provides us a utility of encrypting \n" +
        "   our message in a text file or in an image file output.\n";
    // TEXT AREA
    TextArea t2 = new TextArea(ab);
    t2.setFont(new Font("Monospaced", Font.PLAIN, 17));
    // t1.setText("Hide Text within images, with this simple tool!");
    t2.setBounds(0, 0, 617, 510);
    t2.setBackground(Color.BLACK);
    t2.setForeground(Color.WHITE);
    t2.setEditable(false);
    about.add(t2);
    // TEXTAREA OVER
  }
}
