import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Cred extends Thread {
	CredKeyListener listenerKey;
	static CreditsPanel gpanel2;
	JFrame frameg2, framei;
	CardLayout omniLayout;
	JPanel omniPanel;
	private int t = 0;
	private Connection dbConnection = null; 
	private String tableName;
	private volatile boolean exit = false;
	
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
    
    private void loadDataBase() throws Exception{
		try { 
			Properties props = getConnectionData();
			
			byte[] salt = new String("YOURSALTNUMBER").getBytes();
			int iterationCount = 40000;
			String keypassword = "YOURKEYPASSWORD";
			int keyLength = 128;
	        SecretKeySpec key = createSecretKey(keypassword.toCharArray(), salt, iterationCount, keyLength);
	        
	        
	        String user = decrypt(props.getProperty("db.user"),key);
	        String password = decrypt(props.getProperty("db.password"),key);
	        String host = decrypt(props.getProperty("db.host"),key);
	        String port = decrypt(props.getProperty("db.port"),key);
	        String dbName = decrypt(props.getProperty("db.dbname"),key);
	        tableName = decrypt(props.getProperty("db.tablename"),key);
	        
			String url = "jdbc:mysql://"+host+":"+port+"/"+dbName; 

			Properties info = new Properties(); 
			info.put("user", user);
			info.put("password", password); 
			
			dbConnection = DriverManager.getConnection(url, info); 
			
			if (dbConnection != null) { 
				//System.out.println("Successfully connected to MySQL database: "+ dbName); 
				} 
	        
			} catch (SQLException ex) { 
				System.out.println("An error occurred while connecting MySQL database");
				ex.printStackTrace(); 
				} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
    }
    
    private ResultSet readDataBase() throws Exception {
    	// Reading the database
        String query = "SELECT * FROM "+tableName+" ORDER BY Time ASC;";
        PreparedStatement pst = dbConnection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = pst.executeQuery();
        return rs;
    }

	public Cred(CreditsPanel gpanel2, CardLayout omniLayout, JPanel omniPanel)  {
		this.omniLayout = omniLayout;
		this.omniPanel = omniPanel;
		Cred.gpanel2 = gpanel2;
		listenerKey = new CredKeyListener();
		//frameg2.addKeyListener(listenerKey);
		gpanel2.addKeyListener(listenerKey);
		
		// Load DataBase
		try {
			loadDataBase();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// Read DataBase
		try {
			ResultSet rs = readDataBase();
			gpanel2.sendData(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long startTime = (new Date()).getTime();
		while (!exit) {
			long elapsed = ((new Date()).getTime() - startTime);
			if (elapsed > 20) {
				startTime = (new Date()).getTime();
				t += 1;

				if (t > 768 + 350) {
					t = 0;
				}
				gpanel2.refresh(t);
			} 
		}
	}
	
	public void stopRun() {
		exit = true;
	}

	private class CredKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				omniLayout.show(omniPanel,"MENU");
				stopRun();
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}

}