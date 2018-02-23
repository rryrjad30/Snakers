package com.example.user.snakegameuas.engine;

/**
 * Created by USER on 2/19/2018.
 */


import com.example.user.snakegameuas.classes.Coordinate;
import com.example.user.snakegameuas.enums.Direction;
import com.example.user.snakegameuas.enums.GameState;
import com.example.user.snakegameuas.enums.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by USER on 1/30/2018.
 */

public class GameEngine {
    //    menentukan berapa banyak tinggi lingkaran di canvas
    public static final int GameWidth =28;
    public static final int GameHeight = 42;
    //untuk menyimpan list dari class coordinate
    private List<Coordinate> walls = new ArrayList<>();
    private List<Coordinate> snake = new ArrayList<>();
    private List<Coordinate> apples = new ArrayList<>();

    private Random random = new Random();
    private boolean increaseTail = false;

    private Direction currentDirection = Direction.East;

    private GameState currentGameState = GameState.Running;

    private Coordinate getSnakeHead(){
        return snake.get(0);
    }

    public GameEngine(){

    }

    public void initGame(){
        AddSnake();
        AddWalls();
        AddApples();
    }

    public void UpdateDirection (Direction newDirection) {
//      abs untuk membuat nilai mutlak
//      ordinal adalah mengembalikan urutan dari enum
//      supaya tidak bisa swipe up down langsung atau left right
        if(Math.abs(newDirection.ordinal()-currentDirection.ordinal())%2==1){
            currentDirection=newDirection;
        }
    }

    public GameState getCurrentGameState(){
        return currentGameState;
    }
    public void Update(){
        switch (currentDirection){
            case North:
                UpdateSnake(0,-1);
                break;
            case East:
                UpdateSnake(1,0);
                break;
            case South:
                UpdateSnake(0,1);
                break;
            case West:
                UpdateSnake(-1,0);
                break;
        }
//        check for wall collision
        for (Coordinate w : walls){
//            Tidak bisa pakai equals biasa karena membandingkan array of class dan membandingkan isi dari class
            if(snake.get(0).equals(w)){
                currentGameState= GameState.Lost;
                System.out.println(snake.get(0));
                System.out.println(w);
            }
        }

        //untuk memeriksa jika snake menabrak diri sendiri
        for (int i = 1; i < snake.size() ; i++){
            if (getSnakeHead().equals(snake.get(i))){
                currentGameState = GameState.Lost;
                return;
            }
        }

        //untuk memerika apples
        Coordinate appleToRemove = null;
        for (Coordinate apple : apples){
            if (getSnakeHead().equals(apple)){
                appleToRemove = apple;
                increaseTail = true;
            }
        }
        if (appleToRemove != null){
            apples.remove(appleToRemove);
            AddApples();
        }
    }

    public TileType[][] getMap() {
// TileType[GameWidth][GameHeight] ?
        TileType[][] map= new TileType[GameWidth][GameHeight];
        for(int x=0;x<GameWidth;x++){
            for(int y=0;y<GameHeight;y++){
                map[x][y]=TileType.Nothing;
            }
        }
        for (Coordinate a : apples){
            map[a.getX()][a.getY()] = TileType.Apple;
        }

//        mengubah semua bagian snake menjadi snaketail
        for (Coordinate s : snake){
            map[s.getX()][s.getY()] = TileType.SnakeTail;
        }
//        mengubah bagian pertama menjadi head
        map[snake.get(0).getX()][snake.get(0).getY()] = TileType.SnakeHead;

        for(Coordinate wall: walls){
            map[wall.getX()][wall.getY()]=TileType.Wall;
        }
        return map;
    }

    private void AddApples(){
        Coordinate coordinate = null;
        boolean added = false;

        while (!added){
            // agar tidak taruh didinding
            int x = 1 + random.nextInt(GameWidth-5);
            int y = 1 + random.nextInt(GameHeight-2);

            coordinate = new Coordinate(x,y);
            boolean collision = false;
            for (Coordinate s : snake){
                if (s.equals(coordinate)){
                    collision = true;
                }
            }
            for (Coordinate a : apples){
                if (a.equals(coordinate)){
                    collision = true;
                }
            }
            added = !collision;
        }
        apples.add(coordinate);
    }

    private void AddSnake(){
//        remove all elements in snake list
        snake.clear();
        snake.add(new Coordinate(7,7));
        snake.add(new Coordinate(6,7));
        snake.add(new Coordinate(5,7));
        snake.add(new Coordinate(4,7));
        snake.add(new Coordinate(3,7));
        snake.add(new Coordinate(2,7));
    }

    private void AddWalls(){
//        Top and Bottom Walls
        for(int x=0; x< GameWidth;x++){
            walls.add(new Coordinate(x,0));
            walls.add(new Coordinate(x,GameHeight-5));
        }
//        Left and Right Walls
        for (int y=1;y<GameHeight-5;y++){
            walls.add(new Coordinate(0,y));
//            kalau gameWidth saja error index out of bound
            walls.add(new Coordinate(GameWidth-1,y));
        }
    }
    private void UpdateSnake(int x, int y){
        int newX = snake.get(snake.size() -1).getX();
        int newY = snake.get(snake.size() -1).getY();

        //        mengikuti snakehead
        for(int i=snake.size()-1;i>0;i--){
            snake.get(i).setX(snake.get(i-1).getX());
            snake.get(i).setY(snake.get(i-1).getY());
        }
//        membuat snakehead berpindah sesuai arah
        snake.get(0).setX(snake.get(0).getX()+x);
        snake.get(0).setY(snake.get(0).getY()+y);

        if (increaseTail){
            snake.add(new Coordinate(newX,newY));
            increaseTail = false;
        }
    }
}
