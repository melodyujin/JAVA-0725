package org.sp.app0725.calender;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.sp.app0725.util.StringManager;

//다이어리 만들기
public class DiaryMain extends JFrame{
	JPanel p_north;
	JPanel p_west;
	JPanel p_center;
	JPanel p_east;
	JButton bt_prev;
	JButton bt_next;
	JLabel la_title;
	String[] dayTitle= {"Sun","Mon","Tue","Wed","Thur","Fri","Sat"};
	
	Calendar cal; //이전, 다음 버튼등에 의해 조작될 날짜 객체
	int currentYear; //현재 사용자가 보게 될 연도
	int currentMonth; //현재 사용자가 보게 될 월
	
	//탭메뉴 처리
	TabMenu[] tab;
	Thread[] thread; //탭메뉴의 애니메이션을 구현할 쓰레드
	Color[] tabColor = {Color.RED, Color.YELLOW, Color.GREEN, Color.PINK};
	
	//날짜 셀
	NumCell[][] numCells=new NumCell[6][7];
	
	public DiaryMain() {
		//UI 생성하기
		p_north = new JPanel();
		p_west = new JPanel();
		p_center = new JPanel();
		p_east = new JPanel();
		bt_prev = new JButton("◀");
		bt_next = new JButton("▶");
		la_title = new JLabel("1987-11-01");
		
		cal = Calendar.getInstance(); //날짜 객체 생성(디폴트=현재날짜)
		//날짜 객체로부터 연,월 구하기
		currentYear   = cal.get(Calendar.YEAR);
		currentMonth = cal.get(Calendar.MONTH);
		
		//탭메뉴 처리
		tab = new TabMenu[4];
		thread = new Thread[4];
		
		for(int i=0;i<tab.length;i++) {
			tab[i] = new TabMenu(tabColor[i], -95, 30+(i*71), 100, 70);
			thread[i] = new Thread(tab[i]);
			//thread[i].start();
			p_west.add(tab[i]);
		}
		
		//스타일
		la_title.setFont(new Font("Arial Black", Font.BOLD, 45));
		p_west.setPreferredSize(new Dimension(150,750));
		p_center.setPreferredSize(new Dimension(8000,750));
		p_east.setPreferredSize(new Dimension(150,750));
		
		Border border= new LineBorder(Color.LIGHT_GRAY, 1, true);
		
		p_west.setBorder(new TitledBorder(border, "Registration"));
		p_center.setBorder(new TitledBorder(border, "Content"));
		p_east.setBorder(new TitledBorder(border, "Detail"));
		
		//조립
		p_north.add(bt_prev);
		p_north.add(la_title);
		p_north.add(bt_next);
		
		//setBounds로 좌표에 의한 위치를 설정할 경우, 컨테이너엔 레이아웃
		//즉 배치관리자가 적용되지 말아야 한다..
		p_west.setLayout(null); //적용 안하겠다!!
		
		add(p_north, BorderLayout.NORTH);
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_east, BorderLayout.EAST);
		
		createCell();
		printTitle();
		
		setSize(1100, 850);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		//버튼과 리스너 연결
		//내부익명클래스마저도 더욱 줄여서 코드 작성하고 싶다면?
		//객체차원까지 사용할 필요가 있는가? 간단한 함수기반으로 작성하는 방법은 없는가?
		//람다표현식(Lambda) - 함수기반 언어에서 유래..
		//즉 거창하게 객체까지 필요없는 단순한 코드인 경우 함수 수준으로 처리함이 더 
		//코드량이 적음..
		bt_prev.addActionListener((e)-> {
			prev();
		});
		bt_next.addActionListener((e)-> {
			next();
		});

	}
	
	//셀만들기
	public void createCell() {
		
		//요일 셀 만들기
		for(int i=0;i<dayTitle.length;i++) {
			DayCell cell = new DayCell(Color.DARK_GRAY, 100, 45);
			cell.setTitle(dayTitle[i]);
			p_center.add(cell);
		}
		
		//날짜 셀 만들기
		for(int a=0;a<6;a++) { //6층
			for(int i=0;i<7;i++) { //7호수
				NumCell cell= new NumCell(Color.WHITE, 100, 100);
				cell.setTitle("0");
				//한층에 소속된 호수들을 배열에 채우기
				numCells[a][i]=cell;
				p_center.add(cell);
			}
		}
	}
	
	//날짜 제목 출력
	public void printTitle() {
		int year=cal.get(Calendar.YEAR);
		int mm=cal.get(Calendar.MONTH);
		
		//출력시 2자리수 미만인 경우 즉 1자리수 인 경우 숫자앞에 0을 붙이자
		//예) 7인 경우 --> 07 표현
		la_title.setText(year+"-"+StringManager.getNumString(mm+1));
	}
	
	//이전날짜 처리
	public void prev() {
		//이전 월 처리
		int mm=cal.get(Calendar.MONTH);
		cal.set(Calendar.MONTH, mm-1); //조작
		printTitle();
	}
	//다음날짜 처리
	public void next() {
		//다음 월 처리
		int mm=cal.get(Calendar.MONTH);
		cal.set(Calendar.MONTH, mm+1); //조작
		printTitle(); //제목출력
		printNum(); //날짜출력
	}
	
	//해당 월의 시작 요일 구하기
	public void getStartDayOfWeek() {
		//날짜 객체 하나를 해당월의 조작해서, 그 날이 무슨 요일인지 구하자
		Calendar c = Calendar.getInstance(); //조작용 객체 (망가져도 상관없음)
		int mm=cal.get(Calendar.MONTH);
		
		c.set(mm, 1);
		int day=c.get(Calendar.DAY_OF_WEEK); //1일의 요일 구하기

		System.out.println(day);
	}
	
	//날짜 숫자 출력처리
	public void printNum(){
		//기존 셀에 출력된 숫자 모두 지우기
		for(int a=0;a<numCells.length;a++) { //층수
			for(int i=0;i<numCells[a].length;i++) {
				numCells[a][i].setTitle("");
			}
		}
		
		//각셀에 알맞는 숫자 채우기
		getStartDayOfWeek();
	}
	
	
	public static void main(String[] args) {
		new DiaryMain();
	}
}
