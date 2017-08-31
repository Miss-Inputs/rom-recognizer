/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.romrecognizer;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.SAXException;

/**
 *
 * @author megan
 */
public class ROMRecognizer {

	private static ArrayList<Game> allDats;

	public static ArrayList<Game> getAllDataFiles(File rootDir) throws IOException {
		if (allDats != null) {
			return allDats;
		}
		ArrayList<Game> games = new ArrayList<>();

		for (File f : rootDir.listFiles((File dir, String name) -> name.endsWith(".dat"))) {
			try {
				DataFile df = new DataFile(f);
				df.initGames();
				games.addAll(df.getGameList());
			} catch (SAXException saxe) {
				//Skip this file, though this will print an error message anyway
				//even though I don't want it to because fuck you I guess
			}
		}

		allDats = games;
		return games;
	}

	public static Game identify(File datDir, File path) throws IOException {
		return identifyBySHA1(datDir, Hash.sha1(path));
	}

	public static Game identifyByCRC32(File datDir, String crc32) throws IOException {
		for (Game g : getAllDataFiles(datDir)) {
			if (g.getCrc32() == null) {
				continue;
			}
			if (g.getCrc32().equals(crc32)) {
				return g;
			}
		}
		return null;
	}

	public static Game identifyBySHA1(File datDir, String sha1) throws IOException {
		for (Game g : getAllDataFiles(datDir)) {
			if (g.getSha1() == null) {
				continue;
			}
			if (g.getSha1().equals(sha1)) {
				return g;
			}
		}
		return null;
	}

	public static Game identifyByMD5(File datDir, String md5) throws IOException {
		for (Game g : getAllDataFiles(datDir)) {
			if (g.getMd5() == null) {
				continue;
			}
			if (g.getMd5().equals(md5)) {
				return g;
			}
		}
		return null;
	}

	public static Map<File, Game> identifyAllGames(File datDir, File rootDir) throws IOException {
		Map<File, Game> games = new HashMap<>();
		//for (File f : rootDir.listFiles((File dir, String name) -> !(name.endsWith(".zip") || name.endsWith(".7z")))) {
		Files.walkFileTree(rootDir.toPath(), new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				File f = file.toFile();
				//TODO support compressed files
				if (f.isDirectory()) {
					return FileVisitResult.CONTINUE;
				}
				
				Game game = identify(datDir, f);
				if (game != null) {
					System.out.println("Identified " + f.getAbsolutePath() + " as:\n" + game + "\n---------\n");
					games.put(f, game);
				} else {
					games.put(f, null);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				//Whomstd've give a shit
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		}); //}
		return games;
	}

	public static void main(String[] args) throws Exception {
		//System.out.println(identify(new File("/media/Stuff/Roms/DATs"), new File("/media/Stuff/Roms/DS/CNVPv01.nds")));
		System.out.println(identifyAllGames(new File("/media/Stuff/Roms/DATs"), new File("/media/Stuff/Roms/")));
	}

}
