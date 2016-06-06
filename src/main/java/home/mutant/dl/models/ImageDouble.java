package home.mutant.dl.models;

public class ImageDouble extends Image {
	private static final long serialVersionUID = 1L;
	private double[] data = null ;
	
	public ImageDouble(double[] data, int x, int y)
	{
		super(x,y);
		this.data = data;
	}
	public ImageDouble(double[] data)
	{
		this(data, (int)Math.sqrt(data.length), (int)Math.sqrt(data.length));
	}
	public ImageDouble(int size)
	{
		super(size);
		this.data = new double[size];
	}
	public ImageDouble(int x, int y)
	{
		super(x,y);
		this.data = new double[x*y];
	}
	
	public ImageDouble(byte[][] bs) 
	{
		this( bs.length, bs[0].length);
		convert(bs);
	}
	
	public void normalize(){
		for(int i=0;i<data.length;i++){
			data[i] = data[i]/128-1;
		}
	}
	@Override
	public void setValue(int offset, double value) {
		data[offset] = value;
		
	}
	@Override
	public double getValue(int offset) {
		return data[offset];
	}
	@Override
	public double[] getDataDouble() {
		return data;
	}
	@Override
	public float[] getDataFloat() {
		return null;
	}
	@Override
	public short[] getDataShort() {
		return null;
	}
	@Override
	public Image createImage(int X, int Y) {
		return new ImageDouble(X, Y);
	}
	@Override
	public Image transformGauss(Image[] subImages) {
		// TODO Auto-generated method stub
		return null;
	}
}
