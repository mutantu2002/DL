package home.mutant.dl.models;

public class ImageFloat extends Image {
	private float[] data = null ;
	
	public ImageFloat(float[] data, int x, int y)
	{
		super(x,y);
		this.data = data;
	}
	public ImageFloat(float[] data)
	{
		this(data, (int)Math.sqrt(data.length), (int)Math.sqrt(data.length));
	}
	public ImageFloat(int size)
	{
		super(size);
		this.data = new float[size];
	}
	public ImageFloat(int x, int y)
	{
		super(x,y);
		this.data = new float[x*y];
	}
	
	public ImageFloat(byte[][] bs) 
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
		data[offset] = (float) value;
		
	}
	@Override
	public double getValue(int offset) {
		return data[offset];
	}
	@Override
	public double[] getDataDouble() {
		return null;
	}
	@Override
	public float[] getDataFloat() {
		return data;
	}
	@Override
	public Image createImage(int X, int Y) {
		return new ImageFloat(X, Y);
	}
}
