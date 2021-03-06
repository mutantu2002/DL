package home.mutant.dl.models;

public class ImageShort extends Image {
	private static final long serialVersionUID = 1L;
	private short[] data = null ;
	
	public ImageShort(short[] data, int x, int y)
	{
		super(x,y);
		this.data = data;
	}
	public ImageShort(short[] data)
	{
		this(data, (int)Math.sqrt(data.length), (int)Math.sqrt(data.length));
	}
	public ImageShort(int size)
	{
		super(size);
		this.data = new short[size];
	}
	public ImageShort(int x, int y)
	{
		super(x,y);
		this.data = new short[x*y];
	}
	
	public ImageShort(byte[][] bs) 
	{
		this( bs.length, bs[0].length);
		convert(bs);
	}
	
	public void normalize(){
		double sum=0;
		for(int i=0;i<data.length;i++){
			sum += data[i];
		}
		sum/=data.length;
		for(int i=0;i<data.length;i++){
			data[i] = (short) ((data[i]-sum)/128);
		}
	}
	@Override
	public void setValue(int offset, double value) {
		data[offset] = (short) value;
		
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
		return null;
	}
	@Override
	public short[] getDataShort() {
		return data;
	}
	@Override
	public Image createImage(int X, int Y) {
		return new ImageShort(X, Y);
	}
	@Override
	public Image transformGauss(Image[] subImages) {
		
		int subImageX=subImages[0].imageX;
		int subImagesSqrt = (int) Math.sqrt(subImages.length);
		float normalize=0;
		float max=0;
		float min=1000000000;
		Image result = new ImageShort((imageX-subImageX+1)*subImagesSqrt, (imageY-subImageX+1)*subImagesSqrt);
		for(int x=0;x<=imageX-subImageX;x++){
			for(int y=0;y<=imageX-subImageX;y++){
				int offsetSubImage=0;
				for(int sx=0;sx<subImagesSqrt;sx++){
					for(int sy=0;sy<subImagesSqrt;sy++){
						float gauss = getGauss(x, y, subImages[offsetSubImage++]);
						result.setPixel(x*subImagesSqrt+sx, y*subImagesSqrt+sy, gauss);
						normalize+=gauss;
						if (gauss>max)max=gauss;
						if (gauss<min)min=gauss;
					}
				}
			}
		}
		System.out.println(max);
		System.out.println(min);
		for (int i = 0; i < result.getDataFloat().length; i++) {
			result.getDataFloat()[i]=256*result.getDataFloat()[i]/normalize;
		}
		return result;
	}
	private float getGauss(int X, int Y, Image subImage){
		float s=0;
		int subImageX=subImage.imageX;
		int offsetSubImage=0;
		for(int x=X;x<X+subImageX;x++){
			for(int y=Y;y<Y+subImageX;y++){
				float t = subImage.getDataShort()[offsetSubImage++];
				s+=(data[y*imageX+x]-t)*(data[y*imageX+x]-t);
			}
		}
		System.out.println(s);
		float exp = (float) Math.exp(-s/1000000);
		//System.out.println(exp);
		return exp*250;//10000000/s;
	}
	
}
