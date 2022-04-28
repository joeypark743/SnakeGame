import com.badlogic.gdx.ApplicationAdapter; 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; 
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle; 
import com.badlogic.gdx.math.Circle; 
import com.badlogic.gdx.Input.Keys; 
import com.badlogic.gdx.math.Vector2; 
import com.badlogic.gdx.math.MathUtils; 
import com.badlogic.gdx.math.Intersector; 
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.InputProcessor; 
import com.badlogic.gdx.*; 
import com.badlogic.gdx.utils.Array; 
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Snake extends ApplicationAdapter
{
    private ShapeRenderer renderer;//used to draw textures
    private OrthographicCamera camera;//the camera to our world
    private Viewport viewport;//maintains the ratios of your world 
    private BitmapFont font; //used to draw fonts (text)
    private SpriteBatch batch; //also needed to draw fonts (text)
    private GlyphLayout layout;

    //abtract data types to represent different parts of the game
    private SnakeHead snakeHead;
    private Apple apple;
    private Array<SnakePart> snakeBody;

    private int score;

    //Preferences allows us to save a high score
    private Preferences preferences;
    //random image to use for the snake head, feel free to take this out
    //or add images for the snake body as well
    private Texture snakeHeadPicD; 
    private Texture snakeHeadPicR; 
    private Texture snakeHeadPicU; 
    private Texture snakeHeadPicL; 
    private Texture bodyH;
    private Texture bodyV;
    private Texture tailD;
    private Texture tailR;
    private Texture tailU;
    private Texture tailL;
    private Texture background;
    private Texture app;

    private Texture cornerUR;
    private Texture cornerUL;
    private Texture cornerDR;
    private Texture cornerDL;

    private boolean menu;
    private boolean game;

    private Vector2 mousePos;
    private Circle circleMouse;

    @Override//called once when the game is started (kind of like our constructor)
    public void create(){
        //initialize our instance variables
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera(); 
        circleMouse = new Circle(-1, -1, 1);
        mousePos = new Vector2();

        //This project uses a Constants class to keep track of all the constants
        //They are public so they can be used in any class in our project
        //To access a public constant in the Constants class you use: Constant.YOUR_CONSTANT
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera); 
        font = new BitmapFont();
        batch = new SpriteBatch();

        apple = new Apple();

        snakeHead = new SnakeHead();
        snakeBody = new Array<SnakePart>();
        layout = new GlyphLayout(); 

        score = 0;

        preferences = Gdx.app.getPreferences("High Scores");
        //if there has not been a high score yet set it to 0
        if(!preferences.contains("HighScore"))
        {
            preferences.putInteger("HighScore", 0);
        }
        preferences.flush();//this saves the high score

        //intialize the picture you want to use
        snakeHeadPicD = new Texture(Gdx.files.internal("headD.png"));  
        snakeHeadPicR = new Texture(Gdx.files.internal("headR.png")); 
        snakeHeadPicU = new Texture(Gdx.files.internal("headU.png")); 
        snakeHeadPicL = new Texture(Gdx.files.internal("headL.png")); 

        bodyV = new Texture(Gdx.files.internal("bodyV.png"));  
        bodyH = new Texture(Gdx.files.internal("bodyH.png"));  
        tailU = new Texture(Gdx.files.internal("tailU.png")); 
        tailR = new Texture(Gdx.files.internal("tailR.png")); 
        tailL = new Texture(Gdx.files.internal("tailL.png")); 
        tailD = new Texture(Gdx.files.internal("tailD.png")); 
        background = new Texture(Gdx.files.internal("snakeBackground.png"));
        app = new Texture(Gdx.files.internal("apple.png"));

        cornerUR = new Texture(Gdx.files.internal("cornerUR.png")); 
        cornerUL = new Texture(Gdx.files.internal("cornerUL.png")); 
        cornerDR = new Texture(Gdx.files.internal("cornerDR.png")); 
        cornerDL = new Texture(Gdx.files.internal("cornerDL.png")); 

        menu = true;
        game = false;
    }

    @Override//this is called 60 times a second, all the drawing is in here, or helper
    //methods that are called from here
    public void render(){
        if(menu)
        {
            menuStart();
        }
        if(game)
        {
            gameStart();
        }

    }

    private void getInput()
    {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        mousePos = viewport.unproject(new Vector2(x,y));
        circleMouse.setPosition(mousePos);
        Circle temp = new Circle(Constants.WORLD_WIDTH / 2,Constants.WORLD_HEIGHT / 2 - 10,20);
        if(Intersector.overlaps(temp, circleMouse) && Gdx.input.justTouched())
        {
            menu = false;
            game = true;

        }

    }

    private void menuStart()
    {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(background, 10,Constants.WORLD_HEIGHT / 2,Constants.WORLD_WIDTH - 20, Constants.WORLD_HEIGHT / 2 - 10);
        batch.end();

        getInput();

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.setColor(Color.DARK_GRAY); 
        renderer.begin(ShapeType.Filled);
        renderer.circle(Constants.WORLD_WIDTH / 2,Constants.WORLD_HEIGHT / 2 - 10,20);
        renderer.end();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        font.setColor(Color.WHITE);
        layout.setText(font, "Start");
        font.draw(batch, layout,Constants.WORLD_WIDTH / 2 - 14,Constants.WORLD_HEIGHT / 2 - 5);

        font.setColor(Color.LIME);
        layout.setText(font, "*Use arrow keys to move");
        font.draw(batch, layout, Constants.WORLD_WIDTH/ 4, Constants.WORLD_HEIGHT / 4 + 17);
        layout.setText(font, "*Eat apples to increase your length");
        font.draw(batch, layout, Constants.WORLD_WIDTH/ 4, Constants.WORLD_HEIGHT / 4);
        layout.setText(font, "*Get the highest score!!");
        font.draw(batch, layout, Constants.WORLD_WIDTH/ 4, Constants.WORLD_HEIGHT / 4 - 17);
        batch.end();

    }

    private void gameStart()
    {
        viewport.apply();

        //these two lines wipe and reset the screen so when something action had happened
        //the screen won't have overlapping images
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //update state of our data types
        snakeHead.update();//calls the Update in the SnakeHead class,
        //which checks if an arrow key has been pressed to change the 
        //direction of the snake
        snakeHead.move(snakeBody);//calls the move method in the SnakeHead class
        //and passes in the snakeBody
        checkCollisionApple(); //did the snake collide with an apple
        checkCollisionBody(); //did the snake collide with itself
        checkCollisionWalls();//did the snake collide with a wall
        checkHighScore(); //did we get a new high score

        //AFTER everything is updated, draw everything based on the new state
        //render
        drawGrid(); 

        renderer.setColor(Color.WHITE);
        renderSnakeBody(); 
        renderApple();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Score: " + score, 20, Constants.WORLD_HEIGHT - 20);
        font.draw(batch, "HighScore: " + preferences.getInteger("HighScore"), Constants.WORLD_WIDTH - 110, Constants.WORLD_HEIGHT - 20);
        batch.end();
        turnHead();

    }

    private void turnHead()
    {
        if(snakeHead.getDirection() == Direction.DOWN)
        {
            batch.setProjectionMatrix(camera.combined); 
            batch.begin(); 
            batch.draw(snakeHeadPicD,snakeHead.getX(),snakeHead.getY(),Constants.SIZE,Constants.SIZE);
            batch.end(); 
        }
        if(snakeHead.getDirection() == Direction.RIGHT)
        {
            batch.setProjectionMatrix(camera.combined); 
            batch.begin(); 
            batch.draw(snakeHeadPicR,snakeHead.getX(),snakeHead.getY(),Constants.SIZE,Constants.SIZE);
            batch.end(); 
        }
        if(snakeHead.getDirection() == Direction.UP)
        {
            batch.setProjectionMatrix(camera.combined); 
            batch.begin(); 
            batch.draw(snakeHeadPicU,snakeHead.getX(),snakeHead.getY(),Constants.SIZE,Constants.SIZE);
            batch.end(); 
        }
        if(snakeHead.getDirection() == Direction.LEFT)
        {
            batch.setProjectionMatrix(camera.combined); 
            batch.begin(); 
            batch.draw(snakeHeadPicL,snakeHead.getX(),snakeHead.getY(),Constants.SIZE,Constants.SIZE);
            batch.end(); 
        }
    }

    private void vBody(int i)
    {
        SnakePart s = snakeBody.get(i);
        Rectangle rectangle = s.getRectangle(); 
        batch.setProjectionMatrix(camera.combined); 
        batch.begin(); 
        batch.draw(bodyV, 
            rectangle.x, 
            rectangle.y,
            Constants.SIZE,
            Constants.SIZE); 
        batch.end(); 
    }

    private void hBody(int i)
    {
        SnakePart s = snakeBody.get(i);
        Rectangle rectangle = s.getRectangle(); 
        batch.setProjectionMatrix(camera.combined); 
        batch.begin(); 
        batch.draw(bodyH, 
            rectangle.x, 
            rectangle.y,
            Constants.SIZE,
            Constants.SIZE); 
        batch.end(); 
    }

    private void upLeft(int i)
    {
        SnakePart s = snakeBody.get(i);
        Rectangle rectangle = s.getRectangle(); 
        batch.setProjectionMatrix(camera.combined); 
        batch.begin(); 
        batch.draw(cornerUL,rectangle.x,rectangle.y,Constants.SIZE,Constants.SIZE); 
        batch.end(); 
    }

    private void upRight(int i)
    {
        SnakePart s = snakeBody.get(i);
        Rectangle rectangle = s.getRectangle(); 
        batch.setProjectionMatrix(camera.combined); 
        batch.begin(); 
        batch.draw(cornerUR,rectangle.x,rectangle.y,Constants.SIZE,Constants.SIZE); 
        batch.end(); 
    }

    private void downLeft(int i)
    {
        SnakePart s = snakeBody.get(i);
        Rectangle rectangle = s.getRectangle(); 
        batch.setProjectionMatrix(camera.combined); 
        batch.begin(); 
        batch.draw(cornerDL,rectangle.x,rectangle.y,Constants.SIZE,Constants.SIZE); 
        batch.end(); 
    }

    private void downRight(int i)
    {
        SnakePart s = snakeBody.get(i);
        Rectangle rectangle = s.getRectangle(); 
        batch.setProjectionMatrix(camera.combined); 
        batch.begin(); 
        batch.draw(cornerDR,rectangle.x,rectangle.y,Constants.SIZE,Constants.SIZE); 
        batch.end(); 
    }

    private void corner()
    {
        if(snakeBody.size >= 2)
        {
            for(int i = 0; i < snakeBody.size; i++)
            {
                SnakePart mid = snakeBody.get(i);
                if(i == 0)
                {
                    if((snakeHead.getX() > mid.getX() && snakeBody.get(1).getY() > mid.getY()) || (snakeHead.getY() > mid.getY() && snakeBody.get(1).getX() > mid.getX()))
                    {
                        upRight(i);     //Body right after head
                    }
                    if((snakeHead.getX() < mid.getX() && snakeBody.get(1).getY() > mid.getY()) || (snakeHead.getY() > mid.getY() && snakeBody.get(1).getX() < mid.getX()))
                    {
                        upLeft(i);
                    }
                    if((snakeHead.getX() < mid.getX() && snakeBody.get(1).getY() < mid.getY()) || (snakeHead.getY() < mid.getY() && snakeBody.get(1).getX() < mid.getX()))
                    {
                        downLeft(i);
                    }
                    if((snakeHead.getX() > mid.getX() && snakeBody.get(1).getY() < mid.getY()) || (snakeHead.getY() < mid.getY() && snakeBody.get(1).getX() > mid.getX()))
                    {
                        downRight(i);
                    }
                }
                else if(i != snakeBody.size - 1)        //Make sure its not tail
                {
                    SnakePart left = snakeBody.get(i-1);
                    SnakePart right = snakeBody.get(i+1);
                    if((left.getX() > mid.getX() && right.getY() > mid.getY()) || (left.getY() > mid.getY() && right.getX() > mid.getX()))
                    {
                        upRight(i);     //Body right after head
                    }
                    if((left.getX() < mid.getX() && right.getY() > mid.getY()) || (left.getY() > mid.getY() && right.getX() < mid.getX()))
                    {
                        upLeft(i);
                    }
                    if((left.getX() < mid.getX() && right.getY() < mid.getY()) || (left.getY() < mid.getY() && right.getX() < mid.getX()))
                    {
                        downLeft(i);
                    }
                    if((left.getX() > mid.getX() && right.getY() < mid.getY()) || (left.getY() < mid.getY() && right.getX() > mid.getX()))
                    {
                        downRight(i);
                    }
                }

            }
        }
    }

    private void renderSnakeBody()
    {
        corner();
        for(int i = 0; i < snakeBody.size; i++)
        {
            //Draw each snake body part except the tail

            if(i < snakeBody.size - 1)
            {
                if(i == 0)
                {
                    if(snakeHead.getX() == snakeBody.get(0).getX() && snakeBody.get(1).getX() == snakeHead.getX())
                    {
                        vBody(i);
                    }
                    if(snakeHead.getY() == snakeBody.get(0).getY() && snakeBody.get(1).getY() == snakeHead.getY())
                    {
                        hBody(i);
                    }

                }
                else 
                {
                    SnakePart left = snakeBody.get(i-1);
                    SnakePart mid = snakeBody.get(i);
                    SnakePart right = snakeBody.get(i+1);
                    if(left.getX() == mid.getX() && mid.getX() == right.getX())
                    {
                        vBody(i);
                    }
                    if(left.getY() == mid.getY() && mid.getY() == right.getY())
                    {
                        hBody(i);
                    }
                }
            }
            else if(i == snakeBody.size - 1)
            {
                //Tail
                SnakePart left = null;
                if(snakeBody.size - 2 >= 0)
                {
                    left = snakeBody.get(snakeBody.size - 2);
                }
                else
                {
                    left = snakeBody.get(snakeBody.size - 1);
                }
                SnakePart mid = snakeBody.get(snakeBody.size - 1);
                if((left.getX() == mid.getX() && left.getY() > mid.getY()) || (snakeHead.getX() == mid.getX() && snakeHead.getY() > mid.getY()))
                {
                    batch.setProjectionMatrix(camera.combined); 
                    batch.begin(); 
                    batch.draw(tailU,mid.getX(),mid.getY(),Constants.SIZE,Constants.SIZE); 
                    batch.end(); 
                }
                if((left.getX() == mid.getX() && left.getY() < mid.getY()) || (snakeHead.getX() == mid.getX() && snakeHead.getY() < mid.getY()))
                {
                    batch.setProjectionMatrix(camera.combined); 
                    batch.begin(); 
                    batch.draw(tailD,mid.getX(),mid.getY(),Constants.SIZE,Constants.SIZE); 
                    batch.end(); 
                }
                if((left.getX() > mid.getX() && left.getY() == mid.getY()) || (snakeHead.getX() > mid.getX() && snakeHead.getY() == mid.getY()))
                {
                    batch.setProjectionMatrix(camera.combined); 
                    batch.begin(); 
                    batch.draw(tailR,mid.getX(),mid.getY(),Constants.SIZE,Constants.SIZE); 
                    batch.end(); 
                }
                if((left.getX() < mid.getX() && left.getY() == mid.getY()) || (snakeHead.getX() < mid.getX() && snakeHead.getY() == mid.getY()))
                {
                    batch.setProjectionMatrix(camera.combined); 
                    batch.begin(); 
                    batch.draw(tailL,mid.getX(),mid.getY(),Constants.SIZE,Constants.SIZE); 
                    batch.end(); 
                }

            }

        }  

    }
    private void renderApple()
    {
        //draw the apple
        Rectangle rectangle = apple.getRectangle(); 
        batch.setProjectionMatrix(camera.combined); 
        batch.begin(); 
        batch.draw(app,rectangle.x ,rectangle.y,Constants.SIZE,Constants.SIZE); 
        batch.end(); 
    }

    private boolean checkSpawn()
    {
        if(snakeHead.getRectangle().overlaps(apple.getRectangle()))
        {
            return false;
        }
        for(SnakePart s: snakeBody)
        {
            if(s.getRectangle().overlaps(apple.getRectangle()))
            {
                return false;
            }
        }
        return true;
    }

    private void checkCollisionApple()
    {
        //The Rectangle class in libgdx has an overlaps method
        //to see if two rectangles have overlapped
        if(snakeHead.getRectangle().overlaps(apple.getRectangle()))
        {
            //spawn new apple
            apple.spawnApple();
            while(!checkSpawn())
            {
                apple.spawnApple();
            }

            //add to snakebody 
            //set the last position to either the last snake body part or the snake head, 
            //if there are not snake body parts yet
            Rectangle last; 
            if(snakeBody.size != 0)
                last = snakeBody.get(snakeBody.size - 1).getRectangle(); 
            else
                last = snakeHead.getRectangle(); 

            //depending on the direction of the snake add a snake body part in the correct 
            //position, remember for Rectangle objects the (x,y) position is in the 
            //bottom left
            if(snakeHead.getDirection() == Direction.RIGHT)
                snakeBody.add(new SnakePart(last.x - Constants.SIZE, last.y ));          
            if(snakeHead.getDirection() == Direction.LEFT)
                snakeBody.add(new SnakePart(last.x + Constants.SIZE, last.y));    
            if(snakeHead.getDirection() == Direction.UP)
                snakeBody.add(new SnakePart(last.x, last.y - Constants.SIZE));    
            if(snakeHead.getDirection() == Direction.DOWN)            
                snakeBody.add(new SnakePart(snakeHead.getX(), snakeHead.getY() + Constants.SIZE));

            score++;//don't forget to increase the score
        }
    }

    private void checkCollisionBody()
    {
        //check if the snakeHead has collided with itself
        for(SnakePart p : snakeBody)
        {
            if(p.getRectangle().overlaps(snakeHead.getRectangle()))
            {
                gameOver();//call helper method gameOver() if this has happened
            }
        }
    }

    private void checkCollisionWalls()
    {
        //Check collision with walls, which depends on direction
        if(snakeHead.getDirection() == Direction.RIGHT && snakeHead.getX() + Constants.SIZE > Constants.WORLD_WIDTH)
            gameOver();
        if(snakeHead.getDirection() == Direction.LEFT && snakeHead.getX() < 0)
            gameOver();
        if(snakeHead.getDirection() == Direction.UP && snakeHead.getY() + Constants.SIZE > Constants.WORLD_HEIGHT)
            gameOver();
        if(snakeHead.getDirection() == Direction.DOWN && snakeHead.getY() < 0)
            gameOver();
    }

    private void checkHighScore()
    {
        //do we need to save a new high score
        if(score > preferences.getInteger("HighScore"))
        {
            preferences.putInteger("HighScore", score);
            preferences.flush();
        }
    }

    private void drawGrid()
    {
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.BLUE);

        //draw the horizontal lines
        for(int i = 0; i < Constants.WORLD_HEIGHT; i+= Constants.SIZE)
        {
            renderer.line(0, i, Constants.WORLD_WIDTH, i);
        }
        //draw the vertical lines
        for(int i = 0; i < Constants.WORLD_WIDTH; i+= Constants.SIZE)
        {
            renderer.line(i, 0, i, Constants.WORLD_HEIGHT);
        }
        renderer.end();
    }

    public void gameOver()
    {
        //if the game is over reset the game
        snakeBody.clear();
        score = 0;
        snakeHead.setX(0);
        snakeHead.setY(0);
        snakeHead.setDirection(Direction.RIGHT);
        apple.spawnApple(); 

    }

    @Override
    public void dispose () {
        renderer.dispose();
        batch.dispose(); 
        font.dispose(); 
    }

    @Override
    public void resize (int width, int height) {
        viewport.update(width, height, true); 
    }

}
