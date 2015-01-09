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

	public Model(String filename)
	{
		String[] tokens = filename.split("\\.");
		String extension = tokens[tokens.length - 1];

		vertices = new ArrayList<Vector3D>();
		transVertices = new ArrayList<Vector3D>();
		faces = new ArrayList<Surface>();

		if (extension.equals("obj"))
		{
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(filename));
				String line = br.readLine();
				while (line != null)
				{
					StringTokenizer st = new StringTokenizer(line);

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
						break;
					case "f":
						faces.add(new Surface());
						while (st.hasMoreTokens())
						{
							faces.get(faces.size() - 1).addPoint(
									Integer.parseInt(st.nextToken()) - 1);
						}
					}
					line = br.readLine();
				}
				br.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void draw(Graphics2D g, Matrix3D transform)
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
				xPoints[point] = (int) (transVertices.get(face.getPoint(point)).get(0) * 40 + 500);
				yPoints[point] = (int) (transVertices.get(face.getPoint(point)).get(1) * 40 + 400);
			}

			g.setColor(Color.BLUE);
			//g.fillPolygon(xPoints, yPoints, face.getSideCount());

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
				sum += transVertices.get(i).get(1);
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