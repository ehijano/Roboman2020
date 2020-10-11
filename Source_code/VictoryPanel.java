import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


public class VictoryPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int score;

	private boolean submitted = false;
	private Connection dbConnection = null; 
	private String formattedTime;
	private long elapsed;
	private JFrame omniFrame;
	
	
	public VictoryPanel(CardLayout omniLayout,JPanel omniPanel,JFrame omniFrame, MenuPanel menuPanel) {
		super();
		
		this.omniFrame = omniFrame;

		submitted=false;
		
		repaint();
		
		setLayout(new GridLayout(4, 3));
		
		JButton exitButton = new JButton("EXIT");
		exitButton.setContentAreaFilled(false);
		exitButton.setBorder(null);
		exitButton.setFont(new Font("Lucida Sans Regular", Font.PLAIN, 80));
		exitButton.setForeground(Color.white);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				omniLayout.show(omniPanel,"MENU");
				menuPanel.requestFocus();
			}
		});
		
		JTextField nameTextField = new JTextField("Name: ",10);
		nameTextField.setBackground(Color.GRAY);
		nameTextField.setBorder(BorderFactory.createLineBorder(Color.red, 5));
		nameTextField.setFont( new Font("Arial", Font.PLAIN, 60)  );
		nameTextField.setForeground(Color.red);
		nameTextField.setHorizontalAlignment(JTextField.CENTER);
		nameTextField.setDocument(new LengthRestrictedDocument(8));
		nameTextField.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	nameTextField.setText("");
	        }
			@Override
			public void focusLost(FocusEvent arg0) {
				
			}
	    });
		
		JButton saveButton = new JButton("SAVE");
		saveButton.setContentAreaFilled(false);
		saveButton.setBorder(null);
		saveButton.setFont(new Font("Lucida Sans Regular", Font.PLAIN, 80));
		saveButton.setForeground(Color.white);

		saveButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent me) {
				saveButton.setFont(new Font("Lucida Sans Regular", Font.PLAIN, 40));
				saveButton.setText("connecting...");
			}
			@Override
			public void mouseReleased(MouseEvent me) {
				final String playerName = nameTextField.getText();
				if ((validateName(playerName))&&(!submitted)) {
					submitted = true;
					try {// Connect to DataBase and submit scores
						connectToDB();
						submitToDB(playerName,(int) elapsed,score);
					} catch (Exception e) {
						e.printStackTrace();
					}
					saveButton.setText("saved!");
					saveButton.setEnabled(false);
				}
			}
		});
		
		add(Box.createRigidArea(new Dimension(10, 0)));
		add(Box.createRigidArea(new Dimension(10, 0)));
		add(Box.createRigidArea(new Dimension(10, 0)));
		
		add(Box.createRigidArea(new Dimension(10, 0)));
		add(nameTextField);
		add(Box.createRigidArea(new Dimension(10, 0)));
		
		add(Box.createRigidArea(new Dimension(10, 0)));
		add(saveButton);
		add(Box.createRigidArea(new Dimension(10, 0)));
		
		add(Box.createRigidArea(new Dimension(10, 0)));
		add(exitButton);
		add(Box.createRigidArea(new Dimension(10, 0)));
	}
	
	public void receiveScore(int score, long elapsed) {
		this.score = score;
		this.elapsed = elapsed;
		formattedTime = formatTime(elapsed);
	}
	
	private boolean validateName(String s) {
		return true;
	}
	
	private String formatTime(long t) {
		long elapsed = (t)/1000;
		int seconds = (int) (elapsed%60);
		int minutes = (int) ((elapsed - seconds)/60.0);
		
		String zeroS = "";
		String zeroM = "";
		if (seconds < 10) {zeroS="0";}else {zeroS="";}
		if (minutes < 10) {zeroM="0";}else {zeroM="";}
		
		return zeroM+Integer.toString(minutes)+":"+zeroS+Integer.toString(seconds);
	}
	
	private void submitToDB(String name, int elapsed, int score) {
		try {
	        String sql = "INSERT INTO "+"Leaderboard"+"(Name,Money,Time,Date) VALUES(?,?,?,?)";
	        PreparedStatement pst = dbConnection.prepareStatement(sql);
			
	        pst.setString(1, name);
			
	        System.out.println(Integer.toString(elapsed));
	        
	        pst.setString(2, Integer.toString(score));
	        pst.setString(3, Integer.toString(elapsed));
	        
	        Date now = new Date();
	        String pattern = "yyyy-MM-dd";
	        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	        String mysqlDateString = formatter.format(now);
	        pst.setString(4, mysqlDateString);
	        
	        pst.executeUpdate();
        } catch (SQLException e) {
			e.printStackTrace();
		}
	        
        
	}
	
	public void paintComponent(Graphics g) {
		
		float vScale = (float) omniFrame.getHeight()/768;
		float hScale = (float) omniFrame.getWidth()/1024;
		
		// Background
		Rectangle2D r = new Rectangle2D.Double(0, 0, 1024*hScale, 768*vScale);
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.black);
		g2.fill(r);

		g2.setPaint(Color.white);
		String congrats = "YOU WON!";
		Font font = new Font("Arial", Font.PLAIN, 60);
		g2.setFont( font  );
		FontMetrics metrics = g2.getFontMetrics(font);
		g2.drawString( congrats, (1024/2 -  metrics.stringWidth(congrats)/2)*hScale , 60*vScale);
		
		String timeString = "Time: " + formattedTime;
		Font font2 = new Font("Arial", Font.PLAIN, (int) (50*vScale));
		g2.setFont( font2  );
		FontMetrics metrics2 = g2.getFontMetrics(font);
		g2.drawString(timeString, (1024/3 -  metrics2.stringWidth(timeString)/2)*hScale , (60 + 60 + 10)*vScale );
		
		String scoreString = "Score: " + Integer.toString(score);
		g2.drawString(scoreString, (2*1024/3 -  metrics2.stringWidth(scoreString)/2)*hScale , (60 + 60 + 10)*vScale );
		
	}
	
	private void connectToDB() throws Exception {
		try { 
			Properties props = getConnectionData();
			
			byte[] salt = new String("12345678").getBytes();
			int iterationCount = 40000;
			String keypassword = "Whatisthis";
			int keyLength = 128;
	        SecretKeySpec key = createSecretKey(keypassword.toCharArray(), salt, iterationCount, keyLength);

	        String user = decrypt(props.getProperty("db.user"),key);
	        String password = decrypt(props.getProperty("db.password"),key);
	        String host = decrypt(props.getProperty("db.host"),key);
	        String port = decrypt(props.getProperty("db.port"),key);
	        String dbName = decrypt(props.getProperty("db.dbname"),key);
	        //String tableName = decrypt(props.getProperty("db.tablename"),key);
	        
			String url = "jdbc:mysql://"+host+":"+port+"/"+dbName; 
			Properties info = new Properties(); 
			info.put("user", user);
			info.put("password", password); 
			
			dbConnection = DriverManager.getConnection(url, info); 
			
			if (dbConnection != null) { 
				//System.out.println("Successfully connected to MySQL database: "+ dbName); 
				} 
	        
			} catch (SQLException ex) { 
				System.out.println("An error occurred while connecting MySQL databse");
				ex.printStackTrace(); 
				} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
	}
	
	private static Properties getConnectionData() {
        Properties props = new Properties();
        try (InputStream in = CreditsPanel.class.getResourceAsStream("db.properties"); ) {  
        	props.load(in);
        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(CreditsPanel.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return props;
    }
	
	private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
    private static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }
    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }
}
