package io.github.zowayix.ROMRecognizer;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Adds right click menus to a JTable's headers that allow the user to hide and show columns.
 * 
 * Currently it will drag the columns around if you hold down the button while using the context
 * menu which annoys the fucking shit out of me but I guess there's nothing I can do about that, or
 * maybe there is, but I can't be fucked figuring that out
 * 
 * @author megan
 */
class TableContextMenuListener extends MouseAdapter {

	public static void addTableHeaderEventHandlers(JTable table) {
		table.getTableHeader().addMouseListener(new TableContextMenuListener(table));
	}
	
	private final JTable table;
	private final TableColumnModel model;

	public TableContextMenuListener(JTable table) {
		this.table = table;
		this.model = table.getColumnModel();
	}

	private void decideShowContextMenu(MouseEvent e) {
		if (e.isPopupTrigger()) {
			showContextMenu(e);
			e.consume();
		}
	}

	private void showContextMenu(MouseEvent e) {
		int colIndex = table.columnAtPoint(e.getPoint());
		JPopupMenu menu = new JPopupMenu();
		JMenuItem hideItem = new JMenuItem("Hide Column");
		hideItem.addActionListener((ActionEvent ae) -> {
			TableColumn column = model.getColumn(table.convertColumnIndexToModel(colIndex));
			column.setPreferredWidth(0);
			column.setMinWidth(0);
			column.setMaxWidth(0);
		});
		JMenu showSubmenu = new JMenu("Show Hidden");
		for (int i = 0; i < model.getColumnCount(); ++i) {
			TableColumn column = model.getColumn(i);
			if (column.getWidth() == 0) {
				JMenuItem showItem = new JMenuItem(column.getHeaderValue().toString());
				showItem.addActionListener((ActionEvent e1) -> {
					column.setMaxWidth(Integer.MAX_VALUE);
					column.setPreferredWidth(75); //According to getWidth(), default value
				});
				showSubmenu.add(showItem);
			}
		}
		menu.add(hideItem);
		if (showSubmenu.getMenuComponentCount() > 0) {
			menu.add(showSubmenu);
		}
		menu.show(e.getComponent(), e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		decideShowContextMenu(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		decideShowContextMenu(e);
	}
	
}
