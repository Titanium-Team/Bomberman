package bomberman.view.engine.utility;

import bomberman.view.engine.components.ViewComponent;

public class Utility {

	public static boolean viewComponentIsCollidingWithMouse(ViewComponent o1, int mouseX, int mouseY) {
		if (o1 != null) {
			return mouseX >= (o1.getX()) &&
					mouseX <= (o1.getX()) + (o1.getWidth()) &&
					mouseY >= (o1.getY()) &&
					mouseY <= (o1.getY()) + (o1.getHeight());
		} else {
			return false;
		}
	}
}
