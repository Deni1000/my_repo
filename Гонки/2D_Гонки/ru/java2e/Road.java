package ru.java2e;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;



public class Road extends JPanel implements ActionListener, Runnable{
	Timer mainTimer = new Timer(20, this);
	Image img = new ImageIcon("C:\\Users/User/workspace/F1/res/road_ro.png").getImage();
	
	Player p = new Player();
	Thread enemiesFactory = new Thread(this);
	Thread audioThread = new Thread(new AudioThread());
	List<Enemy> enemies = new ArrayList<Enemy>();
	
	public Road()
	{
		mainTimer.start();
		enemiesFactory.start();
		audioThread.start();
	
	addKeyListener(new MyKeyAdapter ());
	setFocusable(true);
	}
	
	private class MyKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			p.keyPressed(e);
		}
public void keyReleased(KeyEvent e) {
			p.keyReleased(e); 
		}
	}
	public void paint(Graphics g)
	{  g = (Graphics2D) g;
	
	   g.drawImage(img, p.layer1, 0, null);
	   g.drawImage(img, p.layer2, 0, null);
	   g.drawImage(p.img, p.x, p.y, null);
	   
	   double v = (200/Player.MAX_V) * p.v;
	   g.setColor(Color.white);
	   Font font = new Font("Arial", Font.ITALIC, 20);
	   g.setFont(font);
	   g.drawString("��������: " +v + " "+ "��/���", 100, 30);
	   
	   Iterator<Enemy> i = enemies.iterator();
	   while(i.hasNext())
	   {
		    Enemy e = i.next();
		    if (e.x >= 2400 || e.x <= -2400) 
		    {
		    	i.remove();
		    }
		    else
		    {
		    	e.move();
		    g.drawImage(e.img, e.x, e.y, null );
		    }
	   }
	   
	}
	public void actionPerformed(ActionEvent e) 
	{
		p.move();
		repaint();
		testCollisionWithEnemies();
		testWin();
		
	}
	private void testWin() {
		if (p.s >= 10000) {
			JOptionPane.showMessageDialog(null, "�� ��������!!!");
			System.exit(0);
		}
		
	}
	private void testCollisionWithEnemies() {
		
		Iterator<Enemy> i = enemies.iterator();
		while(i.hasNext())
		{
			Enemy e = i.next();
			if (p.getRect().intersects(e.getRect())){
				JOptionPane.showMessageDialog(null, "�� ���������!!!");
			System.exit(0);
			}
	
		}
	}
	@Override
	public void run() {
		while (true)
		{
			Random rand = new Random();
			try {
				Thread.sleep(rand.nextInt(2000));
				enemies.add(new Enemy(1200, rand.nextInt(600), rand.nextInt(60), this));
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
		}
		
		
	}

}
