public class Motion {
	private int mass, radius, velocity, angle;
	public Motion(int m, int r, int v, int theta) {
		this.mass = m;
		this.radius = r;
		this.velocity = v;
		this.angle = theta;
	}
	
	public void setMotion(int m, int r, int v) {
		this.mass = m;
		this.radius = r;
		this.velocity = v;
	}
	
	public void setMotion(int theta) {
		this.angle = theta;
	}
	
	public int getMass() {
		return this.mass;
	}

	public int getRadius() {
		return this.radius;
	}
	
	public int getVelocity() {
		return this.velocity;
	}
	
	public int getAngle() {
		return this.angle;
	}
}
