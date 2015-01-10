import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

public class ViewerPanel extends JPanel implements KeyListener, MouseListener,
		MouseMotionListener, MouseWheelListener, ComponentListener
{
	private Model currentModel;
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
		addComponentListener(this);

		wireframe = false;
		repaint();
	}

	public void openFile(String filename)
	{
		currentModel = new Model();
		currentModel.resetTransform();
		currentModel.updateWindowSize(getWidth(), getHeight());
		currentModel.loadModel(filename);
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		if (currentModel != null)
			currentModel.draw((Graphics2D) g, wireframe);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		dragStart = e.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		currentModel.rotateX((dragStart.getY() - e.getY()) / 50);
		currentModel.rotateY((e.getX() - dragStart.getX()) / 50);
		dragStart = e.getPoint();
		repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0)
	{
		currentModel.scale(Math.pow(1.1, -arg0.getWheelRotation()));
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
	public void componentResized(ComponentEvent e)
	{
		if (currentModel != null)
			currentModel.updateWindowSize(getWidth(), getHeight());
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

	@Override
	public void keyReleased(KeyEvent arg0)
	{
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{
	}
}