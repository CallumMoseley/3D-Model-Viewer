import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class Model
{
	private ArrayList<Vector3D> vertices;
	private ArrayList<Vector3D> transVertices;
	private ArrayList<Surface> faces;
	private Matrix3D transform;
	private int frameWidth, frameHeight;
	
	public Model()
	{
		vertices = new ArrayList<Vector3D>();
		transVertices = new ArrayList<Vector3D>();
		faces = new ArrayList<Surface>();
		
		transform = new Matrix3D();
	}
	
	public void loadModel(String filename)
	{
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		
		String[] tokens = filename.split("\\.");
		String extension = tokens[tokens.length - 1];

		if (extension.equals("obj"))
		{
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(filename));
				String line = br.readLine();
				while (line != null)
				{
					StringTokenizer st = new StringTokenizer(line);

					if (st.hasMoreTokens())
					{
						switch (st.nextToken())
						{
						case "v":
							vertices.add(new Vector3D());
							vertices.get(vertices.size() - 1).set(
									Double.parseDouble(st.nextToken()),
									Double.parseDouble(st.nextToken()),
									Double.parseDouble(st.nextToken()));
							transVertices.add(new Vector3D());
							transVertices.get(transVertices.size() - 1).set(
									vertices.get(vertices.size() - 1));
							minX = Math.min(minX, vertices.get(vertices.size() - 1).get(0));
							minY = Math.min(minY, vertices.get(vertices.size() - 1).get(0));
							maxX = Math.max(maxX, vertices.get(vertices.size() - 1).get(1));
							maxY = Math.max(maxY, vertices.get(vertices.size() - 1).get(1));
							break;
						case "f":
							faces.add(new Surface());
							while (st.hasMoreTokens())
							{
								faces.get(faces.size() - 1).addPoint(
										Integer.parseInt(st.nextToken()) - 1);
							}
						}
					}
					line = br.readLine();
				}

				double width = maxX - minX;
				double height = maxY - minY;
				transform.scale(Math.min(frameWidth / width, frameHeight / height) * 0.9);
				
				br.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void rotateX(double theta)
	{
		transform.rotateX(theta);
	}
	
	public void rotateY(double theta)
	{
		transform.rotateY(theta);
	}
	
	public void rotateZ(double theta)
	{
		transform.rotateZ(theta);
	}
	
	public void scale(double s)
	{
		transform.scale(s);
	}
	
	public void updateWindowSize(int x, int y)
	{
		this.frameWidth = x / 2;
		this.frameHeight = y / 2;
	}

	public void resetTransform()
	{
		transform.identity();
	}

	public void draw(Graphics2D g, boolean wireframe)
	{
		for (int vert = 0; vert < transVertices.size(); vert++)
		{
			transVertices.get(vert).set(vertices.get(vert));
			transVertices.get(vert).transform(transform);
		}
		Collections.sort(faces);
		for (Surface face : faces)
		{
			int[] xPoints = new int[face.getSideCount()];
			int[] yPoints = new int[face.getSideCount()];

			for (int point = 0; point < face.getSideCount(); point++)
			{
				xPoints[point] = (int) (transVertices.get(face.getPoint(point)).get(0) + frameWidth);
				yPoints[point] = (int) (transVertices.get(face.getPoint(point)).get(1) + frameHeight);
			}

			if (!wireframe)
			{
				g.setColor(Color.BLUE);
				g.fillPolygon(xPoints, yPoints, face.getSideCount());
			}

			g.setColor(Color.BLACK);
			g.drawPolygon(xPoints, yPoints, face.getSideCount());
		}
	}

	class Surface implements Comparable<Surface>
	{
		private ArrayList<Integer> points;

		public Surface()
		{
			points = new ArrayList<Integer>();
		}

		public void addPoint(int point)
		{
			points.add(point);
		}

		public int getPoint(int index)
		{
			return points.get(index);
		}

		public int getSideCount()
		{
			return points.size();
		}

		public double zScore()
		{
			double sum = 0;
			for (int i : points)
			{
				sum += transVertices.get(i).get(2);
			}
			return sum / getSideCount();
		}

		@Override
		public int compareTo(Surface o)
		{
			return ((Double) zScore()).compareTo(o.zScore());
		}
	}
}