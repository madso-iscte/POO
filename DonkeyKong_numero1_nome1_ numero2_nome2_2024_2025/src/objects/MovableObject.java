package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public interface MovableObject {

	void move(Direction direction);
	Point2D getPosition();
	void setPosition(Point2D position);
}
