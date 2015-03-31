package database.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableCellWrapRenderer extends JTextArea implements TableCellRenderer
{

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		this.setText(value.toString());
		this.setWrapStyleWord(true);
		this.setLineWrap(true);
		int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
		int textPixelLength = this.getFontMetrics(this.getFont()).stringWidth(this.getText());
		TableColumn columnSelected = table.getColumnModel().getColumn(column);
		int lines = (textPixelLength / (columnSelected.getWidth())) + 1;
		int height = fontHeight * (lines + 3);
		table.setRowHeight(row, height);
		if(row % 2 == 0){
			this.setBackground(Color.GRAY);
		} else {
			this.setBackground(Color.CYAN);
		}
		return this;
	}

}
