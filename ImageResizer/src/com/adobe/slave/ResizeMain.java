package com.adobe.slave;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ResizeMain {

	public static void main(String args[]) throws IOException {
		// TODO Auto-generated method stub
		int port = Integer.parseInt(args[0]);
		String iDir = args[1];
		String oDir = args[2];
		int N = Integer.parseInt(args[3]);

		String str = null;
		Socket s = null;
		while(true){
			try{	
				s = new Socket("localhost", port);
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				str = br.readLine();
				if(str.length() == 1 && Integer.parseInt(str) == 0)
					break;
			}
			catch(IOException e){
				System.out.println(e.toString());
			}
			StringTokenizer strtok = new StringTokenizer(str, ",");
			int imQueueSize = strtok.countTokens();

			int poolSize = Math.min(4, (int) Math.ceil((double)imQueueSize/N)); 
			ExecutorService executor = Executors.newFixedThreadPool(poolSize);
			for(int i = 0; i < imQueueSize; i++){
				if( strtok.hasMoreTokens() ){
					Runnable worker = new WorkerThread(iDir, oDir, strtok.nextToken());
					executor.execute(worker);
				}
				else break;
			}
			executor.shutdown();
			while( !executor.isTerminated()){}
			s.close();
		}
	}
}
