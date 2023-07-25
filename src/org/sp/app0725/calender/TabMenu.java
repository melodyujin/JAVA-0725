package org.sp.app0725.calender;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

//화면의 구석에 숨겨져 있다가, 마우스 이벤트에 반응하여 본 모습을 드러내는
//탭 메뉴 구현
//현재 클래스가 이미 다른 클래스의 자식인 경우, Thread를 재정의하지 못한다.
//왜? 다중상속 금지 원칙에 걸리므로.. 이럴땐 Thread의 run() 메서드만을 보유한
//인터페이스를 구현하면 된다..
public class TabMenu extends JPanel implements Runnable{
	double x;
	double y;
	int width;
	int height;
	double a=0.01;
	int targetX; //목표지점
	
	public TabMenu(Color color, double x, double y, int width, int height) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		
		this.setBackground(color);
		this.setPreferredSize(new Dimension(width, height));
		this.setBounds((int)x, (int)y, width, height);

		//마우스 이벤트 구현
		this.addMouseListener(new MouseAdapter() {
			//마우스 올라가면..
			public void mouseEntered(MouseEvent e) {
				//TabMenu.this.setBounds(-5, y, width, height);
				targetX=-5;
			}
			
			//마우스 내려가면..
			public void mouseExited(MouseEvent e) {
				//TabMenu.this.setBounds(-80, y, width, height);
				targetX=-80;
			}
		});
	}

	//감속도 공식
	public void tick() {
		//x값 = 기존x + 비율계수*(목표x-현재x)
		this.x = this.x + a*(targetX - this.x);
		//System.out.println(this.x);
	}
	public void render() {
		this.setBounds((int)x, (int)y, width, height);
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tick();
			render();
		}
	}
}
