package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture dizzyMan;
	Texture[] man;
	BitmapFont bitmapFont;
	int manState;
	int pause;
	float gravity=0.2f;
	float velocity=0;
	int menY=0;
	Rectangle manRectangle;
	ArrayList<Integer> coinXs;
	ArrayList<Integer> coinYs;
	ArrayList<Rectangle>coinsRectangle;
	ArrayList<Rectangle>bombsRectangle;
	Texture coin;
	int coinsCount=0;
	ArrayList<Integer> bombXs;
	ArrayList<Integer> bombYs;
	Texture bomb;
	int bombsCount=0;
	Random random;
	int score=0;
	int gameState;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		coin =new Texture("coin.png");
		bomb=new Texture("bomb.png");
		dizzyMan=new Texture("dizzy-1.png");
		menY=Gdx.graphics.getHeight()/2;
		coinXs=new ArrayList<>();
		coinYs=new ArrayList<>();
		bombXs=new ArrayList<>();
		bombYs=new ArrayList<>();
		coinsRectangle=new ArrayList<>();
		bombsRectangle=new ArrayList<>();
		random=new Random();
		bitmapFont=new BitmapFont();
		bitmapFont.setColor(Color.BLUE);
		bitmapFont.getData().setScale(10);
	}
	public void makebomb(){
		Float hieght =random.nextFloat()*Gdx.graphics.getHeight();
		bombYs.add(hieght.intValue());
		bombXs.add(Gdx.graphics.getWidth());
	}
	public void makeCoin(){
		Float hieght =random.nextFloat()*Gdx.graphics.getHeight();
		coinYs.add(hieght.intValue());
		coinXs.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState==1){
			//live
			//coins
			if(coinsCount<100){
				coinsCount++;
			}else{
				coinsCount=0;
				makeCoin();
			}
			coinsRectangle.clear();
			for (int i=0;i<coinXs.size();i++){
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i)-7);
				coinsRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}
//bombs
			if(bombsCount<300){
				bombsCount++;
			}else{
				bombsCount=0;
				makebomb();
			}
			bombsRectangle.clear();
			for (int i=0;i<bombXs.size();i++){
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombXs.set(i,bombXs.get(i)-12);
				bombsRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
			}
			if(Gdx.input.justTouched()){
				velocity =-10;
			}
			if(pause<4){
				pause++;
			}else {
				pause=0;
				if(manState<3){
					manState++;
				}else{
					manState=0;
				}}

			velocity+=gravity;
			menY-=velocity;

			if(menY<=0){
				menY=0;
			}

		}
		else if(gameState==0){
			//waiting to start
			if(Gdx.input.justTouched()){
				gameState=1;
			}

		}else if(gameState==2){
			//game over
			if(Gdx.input.justTouched()){
				gameState=1;
				menY=Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;
				
				coinXs.clear();
				coinYs.clear();
				coinsRectangle.clear();
				coinsCount=0;

				bombXs.clear();
				bombYs.clear();
				bombsRectangle.clear();
				bombsCount=0;
				
			}


		}

		
		if(gameState==2)
		batch.draw(dizzyMan,Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,menY);
		else
			batch.draw(man[manState],Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,menY);

		manRectangle=new Rectangle(Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,menY,man[manState].getWidth(),man[manState].getHeight());
		for(int i=0;i<coinsRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,coinsRectangle.get(i))){
				coinsRectangle.remove(i);
				coinYs.remove(i);
				coinXs.remove(i);
					score++;
					break;
			}
		}

		for(int i=0;i<bombsRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,bombsRectangle.get(i))){

				gameState=2;
			}
		}
		bitmapFont.draw(batch,String.valueOf(score),100,200);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
