import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

public class ViewerPanel extends JPanel implements KeyListener, MouseListener,
		MouseMotionListener, MouseWheelListener
{
	private Model currentModel;
	private Matrix3D transform;
	private Point dragStart;
	private boolean wireframe;

	public ViewerPanel()
	{
		setPreferredSize(new Dimension(1024, 768));
		setFocusable(true);

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		transform = new Matrix3D();
		wireframe = false;
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
			currentModel.draw((Graphics2D) g, transform, wireframe);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		dragStart = e.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		transform.rotateX((dragStart.getY() - e.getY()) / 50);
		transform.rotateY((e.getX() - dragStart.getX()) / 50);
		dragStart = e.getPoint();
		repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0)
	{
		transform.scale(Math.pow(1.1, -arg0.getWheelRotation()));
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		if (arg0.getKeyCode() == KeyEvent.VK_Z)
		{
			wireframe = !wireframe;
			repaint();
		}
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

	@Override
	public void keyReleased(KeyEvent arg0)
	{
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
	}
}