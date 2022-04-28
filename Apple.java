
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array; 
/**
 * Created by mengebrits on 6/7/2017.
 */

public class Apple {
    //Apple object encapsulate a Rectangle for its position and size and a spawnApple
    //behavior for when the snake has eaten the apple, getters, and setters
    private Rectangle rectangle;

    public Apple()
    {
        rectangle = new Rectangle(0,0,Constants.SIZE, Constants.SIZE); 
        spawnApple();

    }
    

    public void spawnApple()
    {
        int r = 0;
        int randomR = (int)(Math.random() * (19 - 0 + 1));
        rectangle.x = Constants.SIZE * randomR;
        
        int c = 0;
        int randomC = (int)(Math.random() * (((double)Constants.WORLD_HEIGHT / Constants.SIZE)));
        rectangle.y = Constants.SIZE * randomC;
        
        //15
        //TODO: generate a random x and y that lands on a bottom left corner of the grid
        //and then assign rectangle.x and rectangle.y to those values
       
        //DELETE the 2 lines below once you complete the above
        
        //you can modify this even more to have a parameter of type Array<SnakeBody> 
        //to make sure it does not spawn on the snake

    }

    public Rectangle getRectangle()
    { return rectangle; }

    public void setX(float x)
    { rectangle.x = x; }

    public void setY(float y)
    { rectangle.y = y; }
}
