package com.chrisprimes.FusionNotifications;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.MessagingException;

import com.singlewire.RestClient;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

public class App {
	public static void main(String[] args)
			throws IOException, JSONException, ClassNotFoundException, MessagingException {
		Properties prop = new Properties();
		InputStream is = new FileInputStream("notification.properties");
		prop.load(is);
		
		RestClient icmClient = new RestClient(
				prop.getProperty("TOKEN"),
				"https://api.icmobile.singlewire.com/api/v1");

		ArrayList<IfcDevice> currentDevices = new ArrayList<IfcDevice>();
		ArrayList<IfcDevice> prevDevices;

		try {
			prevDevices = ObjectFileIO.readObjectFromFile("devices.txt");
		} catch (FileNotFoundException e) {
			prevDevices = new ArrayList<IfcDevice>();
		}

		System.out.println("***************\nDevices found in previous run...");
		for (IfcDevice s : prevDevices) {
			System.out.println("\t" + s.description);
		}
		System.out.println("***************\n");

		// Get first page of users
		icmClient.json("/devices/?q=speakers");

		// Paginate through all users
		Iterator<JSONObject> users = icmClient.list("/devices/?q=speakers");

		System.out.println("***************\nDevices found in current run...");
		while (users.hasNext()) {
			JSONObject curr = users.next();

			if (!curr.getString("type").equals("speakers")) {
				continue;
			}

			System.out.println("\t" + curr.getString("description"));
			currentDevices.add(new IfcDevice(curr.getString("id"), curr.getString("description")));
		}
		System.out.println("***************\n");

		ObjectFileIO.writeObjectToFile(currentDevices, "devices.txt");

		ArrayList<IfcDevice> notFoundDevices = new ArrayList<IfcDevice>();

		for (IfcDevice d : prevDevices) {
			Boolean found = false;
			for (IfcDevice c : currentDevices) {
				if (d.id.equals(c.id)) {
					found = true;
				}
			}
			if (!found) {
				notFoundDevices.add(d);
			}
		}

		System.out.println("***************\nDevices offline...");
		String offlineString = "";
		for (IfcDevice s : notFoundDevices) {
			System.out.println("\t" + s.description);
			offlineString += s.description + "<br>\n";
		}
		System.out.println("***************\n");

		if (notFoundDevices.size() != 0) {
			EmailSender eml = new EmailSender(prop.getProperty("FROM"), prop.getProperty("SMTP"));
			eml.sendEmailMessage(prop.getProperty("TO"), "Speakers offline",
					"InformaCast speakers have been detected to be offline:<br><br>\n" + offlineString);
		}

	}
}
