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
		
		System.out.print("[�׸� �߰�]\n"
				+ "ī�װ� > ");
		category = sc.next();
		
		System.out.print("���� > ");
		title = sc.next();
		if (list.isDuplicate(title)) {
			System.out.println("������ �ߺ��˴ϴ�!");
			return;
		}
		sc.nextLine();
		
		System.out.print("���� > ");
		desc = sc.nextLine().trim();
		
		System.out.print("��������(yyyy/MM/dd) > ");
		due_date = sc.next();
		
		TodoItem t = new TodoItem(category, title, desc, due_date);
		list.addItem(t);
		count++;
		System.out.println("�߰��Ǿ����ϴ�.");
	}

	public static void deleteItem(TodoList l) {
		Scanner sc = new Scanner(System.in);
		String isDelete = "n";
		
		System.out.print("[�׸� ����]\n"
				+ "������ �׸��� ��ȣ�� �Է��Ͻÿ� > ");
		int num = sc.nextInt();
		
		if (num > count || num < 1) {
			System.out.println("���� ��ȣ�Դϴ�!");
			return;
		}
		
		System.out.println(num + ". " + l.getItem(num - 1));
		System.out.print("�� �׸��� �����Ͻðڽ��ϱ�? (y/n) > ");
		isDelete = sc.next();
		if(isDelete.equalsIgnoreCase("y")) {
			l.deleteItem(l.getItem(num - 1));
			count--;
			System.out.println("�����Ǿ����ϴ�.");
		}
	}


	public static void updateItem(TodoList l) {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("[�׸� ����]\n"
				+ "������ �׸��� ��ȣ�� �Է��Ͻÿ� > ");
		int num = sc.nextInt();
		
		if (num > count || num < 1) {
			System.out.println("���� ��ȣ�Դϴ�!");
			return;
		}
		
		System.out.println(num + ". " + l.getItem(num - 1));

		System.out.print("�� ī�װ� > ");
		String new_category = sc.next();
		
		System.out.print("�� ���� > ");
		String new_title = sc.next();
		if (l.isDuplicate(new_title)) {
			System.out.println("������ �ߺ��˴ϴ�!");
			return;
		}
		
		sc.nextLine();
		System.out.print("�� ���� > ");
		String new_description = sc.nextLine().trim();
		
		System.out.print("�� ��������(yyyy/MM/dd) > ");
		String new_due_date = sc.next();
		
		l.deleteItem(l.getItem(num - 1));
		TodoItem t = new TodoItem(new_category, new_title, new_description, new_due_date);
		l.addItem(t);
		System.out.println("�����Ǿ����ϴ�.");
	}
	
	public static void find(TodoList l, String keyword) {
		int c = 0;
		for(TodoItem item : l.getList()) {
			if(item.title_contains(keyword) == 1 || item.desc_contains(keyword) == 1) {
				c++;
				System.out.println(l.indexOf(item) + 1 + ". " + item.toString());
			}
		}
		System.out.println("�� " + c + "���� �׸��� ã�ҽ��ϴ�.");
	}
	
	public static void find_cate(TodoList l, String keyword) {
		int c = 0;
		for(TodoItem item : l.getList()) {
			if(item.category_contains(keyword) == 1) {
				c++;
				System.out.println(l.indexOf(item) + 1 + ". " + item.toString());
			}
		}
		System.out.println("�� " + c + "���� �׸��� ã�ҽ��ϴ�.");
	}

	public static void listAll(TodoList l) {
		System.out.println("[��ü ���, �� " + count + "��]");
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
		System.out.println("�� " + c + "���� ī�װ��� ��ϵǾ� �ֽ��ϴ�.");
	}
	
	public static void saveList(TodoList l, String filename) {
		try {
			Writer w = new FileWriter(filename);
			
			for(TodoItem item : l.getList()) {
				w.write(item.toSaveString());
			}
			w.close();
			
			System.out.println("��� �����Ͱ� ����Ǿ����ϴ�.");
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
			System.out.println(count + "���� �׸��� �о����ϴ�.");
		} catch (FileNotFoundException e) {
			System.out.println(filename + " ������ �����ϴ�.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
