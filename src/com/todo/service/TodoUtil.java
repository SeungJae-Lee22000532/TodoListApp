package com.todo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	private static int count;
	
	public static void createItem(TodoList list) {
		String category, title, desc, due_date;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("[항목 추가]\n"
				+ "카테고리 > ");
		category = sc.next();
		
		System.out.print("제목 > ");
		title = sc.next();
		if (list.isDuplicate(title)) {
			System.out.println("제목이 중복됩니다!");
			return;
		}
		sc.nextLine();
		
		System.out.print("내용 > ");
		desc = sc.nextLine().trim();
		
		System.out.print("마감일자(yyyy/MM/dd) > ");
		due_date = sc.next();
		
		TodoItem t = new TodoItem(category, title, desc, due_date);
		list.addItem(t);
		count++;
		System.out.println("추가되었습니다.");
	}

	public static void deleteItem(TodoList l) {
		Scanner sc = new Scanner(System.in);
		String isDelete = "n";
		
		System.out.print("[항목 삭제]\n"
				+ "삭제할 항목의 번호를 입력하시오 > ");
		int num = sc.nextInt();
		
		if (num > count || num < 1) {
			System.out.println("없는 번호입니다!");
			return;
		}
		
		System.out.println(num + ". " + l.getItem(num - 1));
		System.out.print("위 항목을 삭제하시겠습니까? (y/n) > ");
		isDelete = sc.next();
		if(isDelete.equalsIgnoreCase("y")) {
			l.deleteItem(l.getItem(num - 1));
			count--;
			System.out.println("삭제되었습니다.");
		}
	}


	public static void updateItem(TodoList l) {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("[항목 수정]\n"
				+ "수정할 항목의 번호를 입력하시오 > ");
		int num = sc.nextInt();
		
		if (num > count || num < 1) {
			System.out.println("없는 번호입니다!");
			return;
		}
		
		System.out.println(num + ". " + l.getItem(num - 1));

		System.out.print("새 카테고리 > ");
		String new_category = sc.next();
		
		System.out.print("새 제목 > ");
		String new_title = sc.next();
		if (l.isDuplicate(new_title)) {
			System.out.println("제목이 중복됩니다!");
			return;
		}
		
		sc.nextLine();
		System.out.print("새 내용 > ");
		String new_description = sc.nextLine().trim();
		
		System.out.print("새 마감일자(yyyy/MM/dd) > ");
		String new_due_date = sc.next();
		
		l.deleteItem(l.getItem(num - 1));
		TodoItem t = new TodoItem(new_category, new_title, new_description, new_due_date);
		l.addItem(t);
		System.out.println("수정되었습니다.");
	}
	
	public static void find(TodoList l, String keyword) {
		int c = 0;
		for(TodoItem item : l.getList()) {
			if(item.title_contains(keyword) == 1 || item.desc_contains(keyword) == 1) {
				c++;
				System.out.println(l.indexOf(item) + 1 + ". " + item.toString());
			}
		}
		System.out.println("총 " + c + "개의 항목을 찾았습니다.");
	}
	
	public static void find_cate(TodoList l, String keyword) {
		int c = 0;
		for(TodoItem item : l.getList()) {
			if(item.category_contains(keyword) == 1) {
				c++;
				System.out.println(l.indexOf(item) + 1 + ". " + item.toString());
			}
		}
		System.out.println("총 " + c + "개의 항목을 찾았습니다.");
	}

	public static void listAll(TodoList l) {
		System.out.println("[전체 목록, 총 " + count + "개]");
		for(TodoItem item : l.getList()) {
			System.out.println(l.indexOf(item) + 1 + ". " + item.toString());
		}
	}
	
	public static void listCate(TodoList l) {
		int c = 0;
		String category = "";
		for(TodoItem item : l.getList()) {
			String cate = item.getCategory();
			if(category.contains(cate)) {
				continue;
			}
			c++;
			category += cate + " / ";
		}
		category = category.substring(0, category.length() - 3);
		System.out.println(category);
		System.out.println("총 " + c + "개의 카테고리가 등록되어 있습니다.");
	}
	
	public static void saveList(TodoList l, String filename) {
		try {
			Writer w = new FileWriter(filename);
			
			for(TodoItem item : l.getList()) {
				w.write(item.toSaveString());
			}
			w.close();
			
			System.out.println("모든 데이터가 저장되었습니다.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadList(TodoList l, String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			count = 0;
			String oneline;
			while((oneline = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(oneline, "##");
				String category = st.nextToken();
				String title = st.nextToken();
				String desc = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
				
				TodoItem t = new TodoItem(category, title, desc, due_date, current_date);
				count++;
				l.addItem(t);
			}
			br.close();
			System.out.println(count + "개의 항목을 읽었습니다.");
		} catch (FileNotFoundException e) {
			System.out.println(filename + " 파일이 없습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
