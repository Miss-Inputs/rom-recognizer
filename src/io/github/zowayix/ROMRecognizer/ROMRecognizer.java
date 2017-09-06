/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.zowayix.ROMRecognizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.xml.sax.SAXException;

/**
 *
 * @author megan
 */
public class ROMRecognizer {
	
	public static Map<String, String> getKnownExtensions(){
		Map<String, String> m = new HashMap<>();
		m.put("zip", "Zipped file(s)");
		m.put("gz", "GZipped file");
		m.put("bz2", "Bzip2ed file");
		m.put("7z", "7zipped file(s)");
		m.put("rar", "Evil bad terrible format");
		
		m.put("bin", "Binary file"); 
		m.put("img", "Raw image");
		m.put("dsk", "Disk image");
		m.put("rom", "ROM image");
		m.put("cdi", "CD disc image");
		m.put("iso", "Optical disc image");
		m.put("elf", "Executable and Linkable Format executable");
		m.put("tap", "Tape image");
		
		m.put("3ds", "3DS cart"); 
		m.put("3dsx", "3DS homebrew"); 
		m.put("cia", "3DS/DSiWare downloadable title (CTR Importable Archive)");
		m.put("32x", "Sega 32X ROM");
		m.put("adf", "Amiga disk image");
		m.put("adz", "Gzipped Amiga disk image");
		m.put("idf", "Amiga Interchangable Preservation Format disk image");
		m.put("dms", "Amiga DiskSmasher archive");
		m.put("shk", "Apple II ShrinkIt archive");
		m.put("a26", "Atari 2600 ROM");
		m.put("a78", "Atari 7800 ROM");
		m.put("a78", "Atari 7800 ROM");
		m.put("j64", "Atari Jaguar ROM");
		m.put("jag", "Atari Jaguar Server executable");
		m.put("lnx", "Atari Lynx ROM");
		m.put("msa", "Atari Magic Shadow Archiver archive");
		m.put("xex", "Atari XL/XE executable");
		m.put("atr", "Atari disk image");
		m.put("ssd", "BBC Micro single-sided disk image");
		m.put("dsd", "BBC Micro double-sided disk image");
		m.put("col", "ColecoVision ROM");
		m.put("d64", "Commodore 16/64/128 disk image");
		m.put("prg", "Commodore 16/64/128 program"); //Seems like other 8-bit computers use this format too
		m.put("crt", "Commodore 64 cartridge");
		m.put("nds", "Nintendo DS ROM");
		m.put("gb", "Nintendo Gameboy ROM");
		m.put("gbc", "Nintendo Gameboy Colour ROM");
		m.put("gcm", "Nintendo GameCube disc image");
		m.put("gcz", "Dolphin compressed GameCube/Wii disc image");
		m.put("dol", "Nintendo GameCube/Wii executable");
		m.put("dol", "Nintendo GameCube executable");
		m.put("gba", "Nintendo Gameboy Advance ROM");
		m.put("int", "Intellivision ROM");
		m.put("gg", "Sega Game Gear ROM");
		m.put("sms", "Sega Master System ROM");
		m.put("gen", "Sega Mega Drive/Genesis ROM");
		m.put("smd", "Sega Mega Drive/Genesis interleaved ROM");
		m.put("md", "Sega Mega Drive/Genesis ROM"); //More commonly Markdown, but we are not concerned with that
		m.put("n64", "Nintendo 64 little-endian ROM");
		m.put("v64", "Nintendo 64 Doctor V64 ROM");
		m.put("z64", "Nintendo 64 ROM"); //Big endian, which is what the real N64 uses
		m.put("ngp", "Neo Geo Pocket ROM");
		m.put("nes", "Nintendo Entertainment System ROM");
		m.put("fds", "Nintendo Famicom Disk System ROM");
		m.put("prc", "PalmOS application");
		m.put("pce", "TurboGrafx-16/PC Engine ROM");
		m.put("sgx", "PC Engine Supergrafx ROM");
		m.put("min", "Pokémon Mini ROM");
		m.put("sad", "SAM Coupé disk image");
		m.put("mzf", "Sharp MZ tape image");
		m.put("sfc", "Super Nintendo Entertainment System ROM");
		m.put("smc", "SNES ROM with Super Magic Card header");
		m.put("swc", "SNES ROM with Super Wild Card header");
		m.put("fig", "SNES ROM with Fighter Partner header");
		m.put("82b", "TI-82 backup");
		m.put("82g", "TI-82 grouped files");
		m.put("82p", "TI-82 program");
		m.put("83b", "TI-83 backup memory image");
		m.put("83g", "TI-83 grouped files");
		m.put("83p", "TI-83 program");
		m.put("83z", "TI-83 assembly program");
		m.put("8xb", "TI-83+/84+ backup memory image");
		m.put("8xg", "TI-83+/84+ grouped files");
		m.put("8xk", "TI-83+/84+ application");
		m.put("8xp", "TI-83+/84+ program");
		m.put("8xu", "TI-83+/84+ OS");
		m.put("8xz", "TI-83+/84+ assembly program");
		m.put("86g", "TI-86 grouped files");
		m.put("86p", "TI-86 program");
		m.put("89b", "TI-89/92/92+ backup");
		m.put("89c", "TI-89/92/92+ data");
		m.put("89g", "TI-89/92/92+ group");
		m.put("89k", "TI-89/92/92+ application");
		m.put("89p", "TI-89/92/92+ program");
		m.put("89u", "TI-89/92/92+ OS");
		m.put("89z", "TI-89/92/92+ assembly program");
		m.put("cas", "TRS-80 casette tape image");
		m.put("vb", "Nintendo Virtual Boy ROM"); //Not Visual Basic source file in this case
		m.put("wad", "Nintendo Wii channel/IOS");
		m.put("ws", "Bandai WonderSwan ROM");
		m.put("wsc", "Bandai WonderSwan Color ROM");
		m.put("z80", "ZX Spectrum memory snapshot");
		
		return m;
	}

	public static Collection<Game> getAllDataFiles(File rootDir) throws IOException {
		List<Game> games = Collections.synchronizedList(new ArrayList<>());
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<Callable<Object>> tasks = new ArrayList<>();

		for (File f : rootDir.listFiles((File dir, String name) -> Utils.endsWithIgnoreCase(name, ".dat"))) {
			tasks.add(Executors.callable(() -> {
				try {
					DataFile df = new DataFile(f);
					df.initGames();
					games.addAll(df.getGameList());
				} catch (SAXException saxe) {
					//Skip this file, though this will print an error message anyway
					//even though I don't want it to because fuck you I guess
				} catch (IOException ex) {
					//fuck
					throw new RuntimeException(ex);
				}
			}));

		}
		try {
			pool.invokeAll(tasks);
		} catch (InterruptedException ex) {
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
		//TODO Should merge this into scanGames (refactoring it to not always use JTable) and deprecate/delet this
		Collection<Game> gameList = getAllDataFiles(datDir);
		Map<File, Game> games = new HashMap<>();
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
	
	public static void scanGames(Future<Collection<Game>> gameList, File rootDir, JTable table, int workerCount, List<String> filteredExtensions) throws IOException {
		ExecutorService pool = Executors.newFixedThreadPool(workerCount);
		Files.walkFileTree(rootDir.toPath(), new GameScanner(table, gameList, pool, filteredExtensions));
	}

	public static void scanGames(Future<Collection<Game>> gameList, File rootDir, JTable table, int workerCount) throws IOException {
		scanGames(gameList, rootDir, table, workerCount, null);
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
		private final ExecutorService pool;
		private final List<String> filteredExtensions;

		public GameScanner(JTable table, Future<Collection<Game>> gameList, ExecutorService pool, List<String> filteredExtensions) {
			this.table = table;
			this.gameList = gameList;
			this.pool = pool;
			this.filteredExtensions = filteredExtensions;
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
			int rowNum = model.getRowCount() - 1;
			
			Utils.setTableValue(model, rowNum, "File Type (from Ext)", getKnownExtensions().getOrDefault(Utils.getFileExtension(f), "Unknown"));

			//(new Thread(new RowUpdater(f, rowNum, model, gameList))).start();
			Runnable updater = new RowUpdater(f, rowNum, model, gameList);
			pool.execute(updater);
		}

		private void addZip(File f) throws IOException {
			try (ZipFile zippy = new ZipFile(f)) {
				for (ZipEntry ze : Collections.list(zippy.entries())) {
					InputStream stream = zippy.getInputStream(ze);

					DefaultTableModel model = (DefaultTableModel) table.getModel();

					String zippedName = f.getName() + File.separator + ze.getName();
					String zippedPath = f.getPath() + File.separator + ze.getName();
					Object[] row = new Object[]{zippedName, null, "Calculating..", "Calculating..", "Calculating..", zippedPath, null, null, null, 0, null};
					model.addRow(row);
					int rowNum = model.getRowCount() - 1;
					
					Utils.setTableValue(model, rowNum, "File Type (from Ext)", getKnownExtensions().getOrDefault(Utils.getFileExtension(ze.getName()), "Unknown"));

					Runnable updater = new RowUpdater(Utils.cloneInputStream(stream), rowNum, model, gameList);
					pool.execute(updater);
				}
			}
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			try {

				File f = file.toFile();
				if (f.isDirectory()) {
					//walkFileTree already visits subdirectories, so we don't need to visit it here
					return FileVisitResult.CONTINUE;
				}
				
				if(filteredExtensions != null){
					boolean scanned = false;
					for(String extension : filteredExtensions){
						if(Utils.endsWithIgnoreCase(f.getName(), '.' + extension)){
							scanned = true;
							break;
						}
					}
					if(!scanned){
						return FileVisitResult.CONTINUE;
					}
				}

				if (Utils.endsWithIgnoreCase(f.getName(), ".zip")){
					addZip(f);
				} else {
					addFile(f);
				}

				return FileVisitResult.CONTINUE;
			} catch (Exception ex) {
				//Don't add this file obviously, but move along
				System.err.println("Oh no! " + file + " got an error: " + ex.toString());
				//TODO Should report this to the user graphically, but non-intrusively

				return FileVisitResult.CONTINUE;
			}
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

			private final InputStream is;
			private final int rowNumber;
			private final DefaultTableModel model;
			private final Future<Collection<Game>> gameList;
			private final File f;

			public RowUpdater(File f, int rowNumber, DefaultTableModel model, Future<Collection<Game>> gameList) {
				this.is = null;
				this.rowNumber = rowNumber;
				this.model = model;
				this.gameList = gameList;
				this.f = f;
			}

			public RowUpdater(InputStream is, int rowNumber, DefaultTableModel model, Future<Collection<Game>> gameList) {
				this.is = is;
				this.rowNumber = rowNumber;
				this.model = model;
				this.gameList = gameList;
				this.f = null;
			}

			@Override
			public void run() {
				try {
					String crc32, md5, sha1;
					if (f == null) {
						is.reset();
						crc32 = Hash.crc32(is);
						is.reset();
						md5 = Hash.md5(is);
						is.reset();
						sha1 = Hash.sha1(is);
						is.close();
					} else {
						crc32 = Hash.crc32(f);
						md5 = Hash.md5(f);
						sha1 = Hash.sha1(f);
					}

					//model.setValueAt(crc32, rowNumber, model.findColumn("CRC32"));
					Utils.setTableValue(model, rowNumber, "CRC32", crc32);
					Utils.setTableValue(model, rowNumber, "MD5", md5);
					Utils.setTableValue(model, rowNumber, "SHA-1", sha1);
					Utils.setTableValue(model, rowNumber, "Name", "Waiting for dat files to load...");

					Collection<Game> theList = gameList.get();
					model.setValueAt("Identifying...", rowNumber, 1);

					Game game = identifyBySHA1(theList, sha1);
					if (game != null) {
						Utils.setTableValue(model, rowNumber, "Name", game.getName());
						Utils.setTableValue(model, rowNumber, "Platform", game.getDataFile().getName());
						Utils.setTableValue(model, rowNumber, "ROM Name", game.getRomName());
						Utils.setTableValue(model, rowNumber, "Description", game.getDescription());
						Utils.setTableValue(model, rowNumber, "Size", game.getSize());
						Utils.setTableValue(model, rowNumber, "Status", game.getStatus());
					} else {
						Utils.setTableValue(model, rowNumber, "Name", null);
						Utils.setTableValue(model, rowNumber, "Platform", null);
						Utils.setTableValue(model, rowNumber, "ROM Name", null);
						Utils.setTableValue(model, rowNumber, "Description", null);
						Utils.setTableValue(model, rowNumber, "Size", 0);
						Utils.setTableValue(model, rowNumber, "Status", "Unrecognized!");
					}

				} catch (IOException | InterruptedException | ExecutionException ex) {
					//Fuck off
					throw new RuntimeException(ex);
				}

			}
		}
	}

}
