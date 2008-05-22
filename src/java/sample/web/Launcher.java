package sample.web;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 8080;
		String context = "/context";
		String webAppDir = "src/webapp";

		Server server = new Server(port);

		WebAppContext web = new WebAppContext();
		web.setContextPath(context);
		web.setWar(webAppDir);
		server.addHandler(web);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}

}
