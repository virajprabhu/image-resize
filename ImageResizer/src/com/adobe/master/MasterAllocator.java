package com.adobe.master;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MasterAllocator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long start = System.currentTimeMillis();
		int port = 5000;
		String IDir = args[0];
		String ODir = args[1];
		int m = Integer.parseInt(args[2]);
		int n = Integer.parseInt(args[3]);
		File dir = new File(IDir);
		ArrayList<String> fileList = new ArrayList<String>(Arrays.asList(dir.list()));
		int no_of_processes;
		int numImages = fileList.size();
		if (numImages % m == 0)
			no_of_processes = fileList.size() / m;
		else
			no_of_processes = Math.floorDiv(fileList.size(), m) + 1;

		ProcessBuilder pb = new ProcessBuilder(
				"java", "-classpath", "C:\\Users\\virprabh\\workspace\\ResizeImages\\bin", "com.adobe.slave.ResizeMain",
				Integer.toString(port), IDir, ODir, Integer.toString(n));

		
		Process[] pArr = new Process[no_of_processes];
		ServerSocket server = null;
		Socket connectedSocket;
		try {
			server = new ServerSocket(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Boolean processes_created = true;
		for (int i = 0; i < no_of_processes; i++) {
			try {
				pArr[i] = pb.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				processes_created = false;
				break;
			}
		}
		if (processes_created) {
			int running_processes = no_of_processes;
			while (true) {
				if (running_processes == 0)
					break;
				if (server != null) {
					try {
						connectedSocket = server.accept();
						OutputStream os = connectedSocket.getOutputStream();
//						BufferedOutputStream bos = new BufferedOutputStream(os);
						PrintWriter pw = new PrintWriter(os, true);
						String message = "";
						if (fileList.isEmpty()) {
							message += "0";
							running_processes--;
						} else {
							for (int i = 0; i < 200; i++) {
								if (!fileList.isEmpty()) {
									message += fileList.remove(fileList.size() - 1);
									message += ",";
								} else {
									message = message.substring(0, message.length() - 1);
									break;
								}
							}
							if (message.endsWith(",")) {
								message = message.substring(0, message.length() - 1);
							}
						}

						pw.println(message);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		if (server != null) {
			try {
				server.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("#numImages: " + numImages + " : " + (end-start) + " ms m: " + m + " n: " + n);
		
	}
	
}
