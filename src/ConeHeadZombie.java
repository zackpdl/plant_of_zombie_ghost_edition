
public class ConeHeadZombie extends Zombie {

    public ConeHeadZombie(GamePanel parent, int lane) {
        super(parent, lane);
        setHealth(getHealth() *2);
    }
}
