package mw.notifytweet.src.manager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeMap;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class AccountManager implements Serializable {

	public static final String KEY = "NjmzppL9pYlSFykDoNg";

	public static final String SECRET = "XsRMKLGEerg1uLPziq5VfPQgsxiOR6i1iZUCD8KxTI";

	public static final String PATH = "./accounts.dat";

	private AccountManager() {
	}

	private static final long serialVersionUID = 1111L;

	private static AccountManager instance;

	private TreeMap<String, AccessToken> accounts = new TreeMap<>();

	public String selected = "";

	public static void load() {
		try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(
				PATH))) {
			instance = (AccountManager) is.readObject();
			if (instance.accounts == null) {
				instance.accounts = new TreeMap<>();
			}
			if (instance.selected == null) {
				instance.selected = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			instance = new AccountManager();
		}
	}

	public static void save() {
		try (ObjectOutputStream os = new ObjectOutputStream(
				new FileOutputStream("./accounts.dat"))) {
			os.writeObject(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AccountManager getInstance() {
		return instance;
	}

	public void put(String screenName, AccessToken token) {
		accounts.put(screenName, token);
	}

	public AccessToken get(String screenName) {
		return accounts.get(screenName);
	}

	public String[] getAccounts() {
		return accounts.keySet().toArray(new String[0]);
	}

	public Twitter getTwitter() {
		Twitter t = new TwitterFactory().getInstance();
		t.setOAuthConsumer(KEY, SECRET);
		t.setOAuthAccessToken(get(selected));
		return t;
	}

}
