import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ViewerMain extends JFrame implements ActionListener
{
	private ViewerPanel viewer;
	final JFileChooser fc;
	
	public ViewerMain()
	{
		super("3D Model Viewer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(this);
		
		file.add(open);
		menu.add(file);
		setJMenuBar(menu);
		
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("java.class.path") + "\\.."));
		
		viewer = new ViewerPanel();
		setContentPane(viewer);
	}

	public static void main(String[] args)
	{
		ViewerMain main = new ViewerMain();
		main.pack();
		main.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Open"))
		{
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				viewer.openFile(fc.getSelectedFile().getAbsolutePath());
			}
		}
	}
}