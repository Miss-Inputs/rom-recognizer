/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.zowayix.ROMRecognizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author megan
 */
public class DataFile {

	private String name;
	private String description;
	private String version;
	private String author;
	private String homepage;
	private String url;
	private final ArrayList<Game> gameList = new ArrayList<>();
	private Document doc;

	private static Node getFirstChildNodeByTagName(Node parent, String tagName) {
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			Node child = children.item(i);
			if (child.getNodeName().equals(tagName)) {
				return child;
			}
		}
		return null;
	}

	private static String getAttribute(Node node, String attributeName) {
		NamedNodeMap attributes = node.getAttributes();
		Node attribute = attributes.getNamedItem(attributeName);
		return attribute == null ? null : attribute.getTextContent();
	}

	public DataFile(String path) throws SAXException, IOException {
		this(new File(path));
	}

	public DataFile(File dataFile) throws SAXException, IOException {
		//TODO Support those wack ones that aren't XML (probably new class)

		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(dataFile);
			Node header = getFirstChildNodeByTagName(doc.getDocumentElement(), "header");

			this.name = getFirstChildNodeByTagName(header, "name").getTextContent();
			this.description = getFirstChildNodeByTagName(header, "description").getTextContent();
			this.version = getFirstChildNodeByTagName(header, "version").getTextContent();
			this.author = getFirstChildNodeByTagName(header, "author").getTextContent();
			this.homepage = getFirstChildNodeByTagName(header, "homepage").getTextContent();
			this.url = getFirstChildNodeByTagName(header, "url").getTextContent();
		} catch (ParserConfigurationException pce) {
			//Can you actually fuck off?
			throw new RuntimeException(pce);
		}

	}

	public void initGames() {
		NodeList games = doc.getElementsByTagName("game");
		for (int i = 0; i < games.getLength(); ++i) {
			Node game = games.item(i);
			Node rom = getFirstChildNodeByTagName(game, "rom");

			//TODO Some games have multiple ROMs, eg some in Commodore - Amiga (20170315-224115).dat
			String theName = getAttribute(game, "name");
			String desc = getFirstChildNodeByTagName(game, "description").getTextContent();
			String romName = getAttribute(rom, "name");
			String sizeStr = getAttribute(rom, "size");
			long size = sizeStr == null ? 0 : Long.parseLong(sizeStr, 10);
			String crc32 = getAttribute(rom, "crc");
			if ("00000000".equals(crc32)) {
				//Don't be stupid
				continue;
			}

			String md5 = getAttribute(rom, "md5");
			String sha1 = getAttribute(rom, "sha1");
			String status = getAttribute(rom, "status");

			gameList.add(new Game(theName, desc, romName, size, crc32, md5, sha1, status, this));
		}
	}

	public ArrayList<Game> getGameList() {
		return gameList;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the homepage
	 */
	public String getHomepage() {
		return homepage;
	}

	/**
	 * @param homepage the homepage to set
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
