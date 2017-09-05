/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.zowayix.ROMRecognizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.xml.sax.SAXException;

/**
 *
 * @author megan
 */
public class ROMRecognizer {

	public static ArrayList<Game> getAllDataFiles(File rootDir) throws IOException {
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

		return games;
	}

	public static Game identify(File datDir, File path) throws IOException {
		return identifyBySHA1(datDir, Hash.sha1(path));
	}

	public static Game identify(Collection<Game> gameList, File path) throws IOException {
		return identifyBySHA1(gameList, Hash.sha1(path));
	}

	public static Game identifyByCRC32(File datDir, String crc32) throws IOException {
		return identifyByCRC32(getAllDataFiles(datDir), crc32);
	}

	public static Game identifyByCRC32(Collection<Game> gameList, String crc32) throws IOException {
		for (Game g : gameList) {
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
		return identifyBySHA1(getAllDataFiles(datDir), sha1);
	}

	public static Game identifyBySHA1(Collection<Game> gameList, String sha1) throws IOException {
		for (Game g : gameList) {
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
		return identifyByMD5(getAllDataFiles(datDir), md5);
	}

	public static Game identifyByMD5(Collection<Game> gameList, String md5) throws IOException {
		for (Game g : gameList) {
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
		ArrayList<Game> gameList = getAllDataFiles(datDir);
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

				Game game = identify(gameList, f);
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

	public static void scanGames(Future<Collection<Game>> gameList, File rootDir, JTable table) throws IOException {
		//for (File f : rootDir.listFiles((File dir, String name) -> !(name.endsWith(".zip") || name.endsWith(".7z")))) {
		Files.walkFileTree(rootDir.toPath(), new GameScanner(table, gameList)); //}
	}

	public static void main(String[] args) throws Exception {
		//TODO Make this a nicer CLI maybe
		String datDir = args.length == 0 ? "./DATs" : args[0];
		String romDir = args.length <= 1 ? "." : args[1];

		System.out.println(identifyAllGames(new File(datDir), new File(romDir)));
	}

	private static class GameScanner implements FileVisitor<Path> {

		private final JTable table;
		private final Future<Collection<Game>> gameList;

		public GameScanner(JTable table, Future<Collection<Game>> gameList) {
			this.table = table;
			this.gameList = gameList;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		private void addFile(File f) throws IOException {
			//"Filename", "Name", "CRC32", "MD5", "SHA-1", "Path", "Platform", "ROM Name", "Description", "Size", "Status"
			DefaultTableModel model = (DefaultTableModel) table.getModel();

			Object[] row = new Object[]{f.getName(), null, "Calculating..", "Calculating..", "Calculating..", f.getPath(), null, null, null, 0, null};
			model.addRow(row);
			//int rowNum = table.convertRowIndexToModel(model.getRowCount() - 1);
			int rowNum = model.getRowCount() - 1;

			Thread thready = new Thread(new RowUpdater(f, rowNum, model, gameList));
			thready.start();

		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			System.out.println("Visiting file: " + file.toString());

			File f = file.toFile();
			if (f.isDirectory()) {
				return FileVisitResult.CONTINUE;
			}

			addFile(f);

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

		static class RowUpdater implements Runnable {

			private final File f;
			private final int rowNumber;
			private final DefaultTableModel model;
			private final Future<Collection<Game>> gameList;

			public RowUpdater(File f, int rowNumber, DefaultTableModel model, Future<Collection<Game>> gameList) {
				this.f = f;
				this.rowNumber = rowNumber;
				this.model = model;
				this.gameList = gameList;
			}

			@Override
			public void run() {
				//"Filename", "Name", "CRC32", "MD5", "SHA-1", "Path", "Platform", "ROM Name", "Description", "Size", "Status"
				try {
					String crc32 = Hash.crc32(f);
					String md5 = Hash.md5(f);
					String sha1 = Hash.sha1(f);

					model.setValueAt(crc32, rowNumber, 2);
					model.setValueAt(md5, rowNumber, 3);
					model.setValueAt(sha1, rowNumber, 4);
					model.setValueAt("Waiting...", rowNumber, 1);

					//Game game = identifyBySHA1(gameList, sha1);
					Game game = identifyBySHA1(gameList.get(), sha1);
					if (game != null) {
						model.setValueAt(game.getName(), rowNumber, 1);
						model.setValueAt(game.getDataFile().getName(), rowNumber, 6);
						model.setValueAt(game.getRomName(), rowNumber, 7);
						model.setValueAt(game.getDescription(), rowNumber, 8);
						model.setValueAt(game.getSize(), rowNumber, 9);
						model.setValueAt(game.getStatus(), rowNumber, 10);
					} else {
						model.setValueAt("Unrecognized!", rowNumber, 1);
						model.setValueAt(null, rowNumber, 6);
						model.setValueAt(null, rowNumber, 7);
						model.setValueAt(null, rowNumber, 8);
						model.setValueAt(null, rowNumber, 9);
						model.setValueAt(null, rowNumber, 10);
					}

				} catch (IOException | InterruptedException | ExecutionException ex) {
					//Fuck off
					throw new RuntimeException(ex);
				}

			}
		}
	}

}
