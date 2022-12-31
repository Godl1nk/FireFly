package ac.firefly.util.interaction;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

public class Distance
{
    private Location to;
    private Location from;
    private double xDiff;
    private double yDiff;
    private double zDiff;
    private boolean goingUp;
    private boolean goingDown;

    public Distance(PlayerMoveEvent e)
    {
        this(e.getFrom(), e.getTo());
    }

    //public Distance(PacketPlayerMoveEvent e)
    //{
    //    this(e.getFrom(), e.getTo());
    //}

    public Distance(Location a, Location b)
    {
        this.from = a;
        this.to = b;
        this.xDiff = Math.abs(a.getX() - b.getX());
        this.yDiff = Math.abs(a.getY() - b.getY());
        this.zDiff = Math.abs(a.getZ() - b.getZ());
        this.goingUp = this.to.getY() > this.from.getY();
        this.goingDown = this.from.getY() > this.to.getY();
    }

    public Location getTo()
    {
        return this.to;
    }

    public Location getFrom()
    {
        return this.from;
    }

    public double getXDifference()
    {
        return this.xDiff;
    }

    public double getZDifference()
    {
        return this.zDiff;
    }

    public double getYDifference()
    {
        return this.yDiff;
    }

    public boolean isGoingDown()
    {
        return this.goingDown;
    }

    public boolean isGoingUp()
    {
        return this.goingUp;
    }

    public boolean isMovingHorizontally()
    {
        return this.xDiff != 0.0D || this.zDiff != 0.0D;
    }
}