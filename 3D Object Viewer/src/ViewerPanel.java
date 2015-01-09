import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class ViewerPanel extends JPanel implements MouseListener,
		MouseMotionListener
{
	private Model currentModel;
	private Matrix3D transform;
	private Point dragStart;

	public ViewerPanel()
	{
		this.setPreferredSize(new Dimension(1024, 768));
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		transform = new Matrix3D();
		repaint();
	}
	
	public void openFile(String filename)
	{
		currentModel = new Model(filename);
		transform.identity();
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		if (currentModel != null)
			currentModel.draw((Graphics2D) g, transform);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		dragStart = e.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		transform.rotateX((e.getY() - dragStart.getY()) / 40);
		transform.rotateY((dragStart.getX() - e.getX()) / 40);
		dragStart = e.getPoint();
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
	}
}