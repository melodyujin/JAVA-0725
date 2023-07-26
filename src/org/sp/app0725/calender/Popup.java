package org.sp.app0725.calender;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

//날짜 셀을 클릭하면, 상세 내용을 입력받을 수 있는 팝업창
public class Popup extends JFrame{
	JLabel la_header; //날짜 제목
	JTextArea area;
	JPanel p_icon; //아이콘이 배치될 패널
	String[] path= {"res/snow.png","res/sunny.png","res/rain.png","res/cloud.png"};
	ArrayList<JLabel> la_icon; //아이콘 이미지를 담게 될 라벨들
	JButton bt;
	JLabel la_selected; //유저가 선택한 라벨
	NumCell numCell; //저장 버튼 누를때, 어떤 셀을 대상으로 아이콘을 반영할지를 알기위함..
	int index; //사용자가 선택한 라벨의 index, 이 index로 아이콘 배열명에 접근할 수 있다.
	
	public Popup() {
		la_header = new JLabel("날짜 나옴");
		area = new JTextArea();
		p_icon = new JPanel();
		la_icon = new ArrayList<JLabel>();
		bt = new JButton("저장");
		
		//스타일
		la_header.setFont(new Font("돋움", Font.BOLD, 28));
		area.setPreferredSize(new Dimension(380, 150));
		p_icon.setPreferredSize(new Dimension(380, 100));
		
		setLayout(new FlowLayout());
		
		add(la_header);
		add(area);
		add(p_icon);
		createIcon();
		add(bt);
		
		setBounds(400, 300, 400, 400);
		//setVisible(true);
		setLocationRelativeTo(null);
		
		//저장버튼과 리스너 연결
		//재정의할 메서드가 1개일 경우 굳이 클래스까지 만들 필요가 있나?.. 람다표현식..
		bt.addActionListener((e)->{
			save();
		});
		
	}
	
	//유저가 선택한 아이콘 및 area의 값을 날짜셀(NumCell)에 반영
	//이때, 아이콘은 NumCell의 iconBox 패널에 부착하자.. 왜? 나중에 아이콘을 지울때
	//iconBox패널의 하위 자식들을 전부 제거하는 방법을 이용하면 편하므로..
	public void save() {
		//라벨을 생성하여 부착하기
		URL url=ClassLoader.getSystemResource(path[index]);
		try {
			BufferedImage buffImg=ImageIO.read(url);
			Image image=buffImg;
			image=image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			JLabel la_icon = new JLabel(new ImageIcon(image));
			numCell.iconBox.add(la_icon);
			numCell.updateUI(); //컴포넌트 새로고침
			this.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//아이콘 생성
	public void createIcon() {
		
		for(int i=0;i<path.length;i++) {
			URL url=ClassLoader.getSystemResource(path[i]);
			try {
				BufferedImage buffImg=ImageIO.read(url);
				Image image=buffImg;
				image=image.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
				
				JLabel la= new JLabel(new ImageIcon(image));
				la_icon.add(la); //리스트에 생성된 라벨 넣기
				p_icon.add(la);
				
				la.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						//System.out.println(e.getSource());
						la_selected=(JLabel)e.getSource(); //이벤트 일으킨 라벨 넘기기
						index=la_icon.indexOf(la_selected); //몇번째 라벨을 선택했는지 알아보기
						System.out.println("당신이 선택한 라벨은 "+index+"번째 에요");
					}
				});
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
		//윈도우 보이고, 날짜 출력, 선택된 셀에 아이콘 등 적용
		public void showPop(NumCell numCell, String header) {
			this.setVisible(true); //보이게
			la_header.setText(header);
			
			//셀에 아이콘 적용하기
			this.numCell = numCell;
			
			//numCell.iconBox.add(la_icon[]);
		}
}
