/*
<body bgcolor=white>
<h2>
Java classes to implement 3D transformation matrices
</h2>
&nbsp; &nbsp; &nbsp;
by <a href=http://mrl.nyu.edu/perlin>Ken Perlin</a> @ NYU, 1998.
<p>
<font color=red><i>
You have my permission to use freely, as long
as you keep the attribution. - Ken Perlin
</i></font>
<p>
<b>
Note: this <i>Matrix3D.html</i> file also
works as a legal <i>Matrix3D.java</i> file.
If you save the source under that name, you can just run
<i>javac</i> on it.
</b>
<h3>
Why does this class exist?
</h3>
<blockquote>
<i>
I created this class to support general purpose
3D transformations.
I use it in a number of the demos that run on my Web page.
</i>
</blockquote>
<h3>
What does the class do?
</h3>
<blockquote>
<i>
You can use it to create 3D points and homogeneous vectors,
and also to create transformation matrices with these.
There are methods to rotate, translate, and scale transformations,
and to apply transformations to vectors.
You can also get and set the elements of matrices and vectors.
<p>
The classes <b>Vector3D</b> and <b>Matrix3D</b>
are extended from respective generic classes
<b>VectorN</b> and <b>MatrixN</b>,
which do most of the bookkeeping for
arithmetic vectors of length <b>N</b> and
square matrices of size <b>N &#215; N</b>, respectively.
</i>
</blockquote>
<pre>
*/

// <b>Homogeneous transformation matrices in three dimensions</b>

public class Matrix3D extends MatrixN {

   Matrix3D() { // <b>create a new identity transformation</b>
      super(4);
      identity();
   }

   void rotateX(double theta) { // <b>rotate transformation about the X axis</b>

      Matrix3D tmp = new Matrix3D();
      double c = Math.cos(theta);
      double s = Math.sin(theta);

      tmp.set(1,1, c);
      tmp.set(1,2,-s);
      tmp.set(2,1, s);
      tmp.set(2,2, c);

      preMultiply(tmp);
   }
   void rotateY(double theta) { // <b>rotate transformation about the Y axis</b>

      Matrix3D tmp = new Matrix3D();
      double c = Math.cos(theta);
      double s = Math.sin(theta);

      tmp.set(2,2, c);
      tmp.set(2,0,-s);
      tmp.set(0,2, s);
      tmp.set(0,0, c);

      preMultiply(tmp);
   }
   void rotateZ(double theta) { // <b>rotate transformation about the Z axis</b>

      Matrix3D tmp = new Matrix3D();
      double c = Math.cos(theta);
      double s = Math.sin(theta);

      tmp.set(0,0, c);
      tmp.set(0,1,-s);
      tmp.set(1,0, s);
      tmp.set(1,1, c);

      preMultiply(tmp);
   }

   void translate(double a, double b, double c) { // <b>translate</b>

      Matrix3D tmp = new Matrix3D();

      tmp.set(0,3, a);
      tmp.set(1,3, b);
      tmp.set(2,3, c);

      preMultiply(tmp);
   }
   void translate(Vector3D v) { translate(v.get(0), v.get(1), v.get(2)); }

   void scale(double s) { // <b>scale uniformly</b>

      Matrix3D tmp = new Matrix3D();

      tmp.set(0,0, s);
      tmp.set(1,1, s);
      tmp.set(2,2, s);

      preMultiply(tmp);
   }
   void scale(double r, double s, double t) { // <b>scale non-uniformly</b>

      Matrix3D tmp = new Matrix3D();

      tmp.set(0,0, r);
      tmp.set(1,1, s);
      tmp.set(2,2, t);

      preMultiply(tmp);
   }
   void scale(Vector3D v) { scale(v.get(0), v.get(1), v.get(2)); }
}

// <b>Homogeneous vectors in three dimensions</b>

class Vector3D extends VectorN {

   Vector3D() { super(4); }                                    // <b>create a new 3D homogeneous vector</b>

   void set(double x, double y, double z, double w) {          // <b>set value of vector</b>
      set(0, x);
      set(1, y);
      set(2, z);
      set(3, w);
   }
   void set(double x, double y, double z) { set(x, y, z, 1); } // <b>set value of a 3D point</b>
}

// <b>Geometric vectors of size N</b>

class VectorN {
   private double v[];

   VectorN(int n) { v = new double[n]; }    // <b>create a new vector</b>

   int size() { return v.length; }          // <b>return vector size</b>

   double get(int j) { return v[j]; }       // <b>get one element</b>

   void set(int j, double f) { v[j] = f; }  // <b>set one element</b>

   void set(VectorN vec) {                  // <b>copy from another vector</b>
      for (int j = 0 ; j < size() ; j++)
	 set(j, vec.get(j));
   }

   public String toString() {               // <b>convert to string representation</b>
      String s = "{";
      for (int j = 0 ; j < size() ; j++)
	 s += (j == 0 ? "" : ",") + get(j);
      return s + "}";
   }

   void transform(MatrixN mat) {            // <b>multiply by an N &#215; N matrix</b>
      VectorN tmp = new VectorN(size());
      double f;

      for (int i = 0 ; i < size() ; i++) {
	 f = 0.;
         for (int j = 0 ; j < size() ; j++)
	    f += mat.get(i,j) * get(j);
         tmp.set(i, f);
      }
      set(tmp);
   }

   double distance(VectorN vec) {          // <b>euclidean distance</b>
      double x, y, d = 0;
      for (int i = 0 ; i < size() ; i++) {
	 x = vec.get(0) - get(0);
	 y = vec.get(1) - get(1);
	 d += x * x + y * y;
      }
      return Math.sqrt(d);
   }
}

// <b>Geometric matrices of size N &#215; N</b>

class MatrixN { // <b>N &#215; N matrices</b>
   private VectorN v[];

   MatrixN(int n) {                                    // <b>make a new square matrix</b>
      v = new VectorN[n];
      for (int i = 0 ; i < n ; i++)
	 v[i] = new VectorN(n);
   }

   int size() { return v.length; }                     // <b>return no. of rows</b>

   double get(int i, int j) { return get(i).get(j); }  // <b>get one element</b>

   void set(int i, int j, double f) { v[i].set(j,f); } // <b>set one element</b>

   VectorN get(int i) { return v[i]; }                 // <b>get one row</b>

   void set(int i, VectorN vec) { v[i].set(vec); }     // <b>set one row</b>

   void set(MatrixN mat) {                             // <b>copy from another matrix</b>
      for (int i = 0 ; i < size() ; i++)
	 set(i, mat.get(i));
   }

   public String toString() {                   // <b>convert to string representation</b>
      String s = "{";
      for (int i = 0 ; i < size() ; i++)
	 s += (i == 0 ? "" : ",") + get(i);
      return s + "}";
   }

   void identity() {                            // <b>set to identity matrix</b>
      for (int j = 0 ; j < size() ; j++)
      for (int i = 0 ; i < size() ; i++)
         set(i, j, (i == j ? 1 : 0));
   }

   void preMultiply(MatrixN mat) {              // <b>mat &#215; this</b>
      MatrixN tmp = new MatrixN(size());
      double f;

      for (int j = 0 ; j < size() ; j++)
      for (int i = 0 ; i < size() ; i++) {
	 f = 0.;
         for (int k = 0 ; k < size() ; k++)
	    f += mat.get(i,k) * get(k,j);
	 tmp.set(i, j, f);
      }
      set(tmp);
   }

   void postMultiply(MatrixN mat) {             // <b>this &#215; mat</b>
      MatrixN tmp = new MatrixN(size());
      double f;

      for (int j = 0 ; j < size() ; j++)
      for (int i = 0 ; i < size() ; i++) {
	 f = 0.;
         for (int k = 0 ; k < size() ; k++)
	    f += get(i,k) * mat.get(k,j);
	 tmp.set(i, j, f);
      }
      set(tmp);
   }
}

