import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
public class PhaseIdentificationEx {
	public static void main(String[] args){
		exvolset("C:\\Users\\mayi\\Desktop\\���Ķ�������-ͼ��\\ʵ������\\55user_voltagedataset_1day.csv",
				"C:\\Users\\mayi\\Desktop\\���Ķ�������-ͼ��\\ʵ������\\55-24h-60m.csv",1,60);
		int [] a0b1c2= {0,1,0,0,0,1,1,2,0,1,1,2,1,0,1,2,2,2,2,0,0,0,1,2,0,1,2,2,0,0,0,2,2,0,1,1,1,1,2,1,1,2,2,1,1,0,2,0,0,1,0,0,1,0,0};
		int [] a0b2c1= {0,2,0,0,0,2,2,1,0,2,2,1,2,0,2,1,1,1,1,0,0,0,2,1,0,2,1,1,0,0,0,1,1,0,2,2,2,2,1,2,2,1,1,2,2,0,1,0,0,2,0,0,2,0,0};
		int [] a1b0c2= {1,0,1,1,1,0,0,2,1,0,0,2,0,1,0,2,2,2,2,1,1,1,0,2,1,0,2,2,1,1,1,2,2,1,0,0,0,0,2,0,0,2,2,0,0,1,2,1,1,0,1,1,0,1,1};
		int [] a1b2c0= {1,2,1,1,1,2,2,0,1,2,2,0,2,1,2,0,0,0,0,1,1,1,2,0,1,2,0,0,1,1,1,0,0,1,2,2,2,2,0,2,2,0,0,2,2,1,0,1,1,2,1,1,2,1,1};
		int [] a2b0c1= {2,0,2,2,2,0,0,1,2,0,0,1,0,2,0,1,1,1,1,2,2,2,0,1,2,0,1,1,2,2,2,1,1,2,0,0,0,0,1,0,0,1,1,0,0,2,1,2,2,0,2,2,0,2,2};
		int [] a2b1c0= {2,1,2,2,2,1,1,0,2,1,1,0,1,2,1,0,0,0,0,2,2,2,1,0,2,1,0,0,2,2,2,0,0,2,1,1,1,1,0,1,1,0,0,1,1,2,0,2,2,1,2,2,1,2,2};
		int[]input={0,2,0,0,0,2,2,1,0,2,2,1,2,0,2,1,1,1,1,0,0,0,2,1,0,2,1,1,0,0,0,1,1,0,2,2,2
				,2,1,2,2,1,1,2,2,0,1,0,0,2,0,0,2,0,0};
		/*int [] acc= {};
		 * 
		 */
		System.out.println(accuracy(a0b2c1,input));
	}
	
	public static double accuracy(int [] source,int [] input) {
		int correctnum=0;
		for(int i=0;i<source.length;i++) {
			if(source[i] == input[i]) {
				correctnum++;
			}
		}
		return correctnum/55.0;
	}
	
	/**
	 * @param inpath �����ļ���ַ
	 * @param outpath ����ļ���ַ
	 * @param allratio �����������������磺1/2��ʾֻʹ��ԭʼ���ݼ���ǰ1/2
	 * @param sigratio ������������Ϊ1��ʾÿһ��ȡһ�Σ�5��ÿ���ȡһ�Σ�15��ÿ15��ȡһ��
	 */
	public static void exvolset(String inpath,String outpath,double allratio,int sigratio) {
		if(inpath==null || inpath.length()==0 || outpath==null || outpath.length()==0) return;
		if(allratio>1 || sigratio<1) {
			System.err.println(allratio>1?"allratio ���ɴ���1��":"sigratio ����С��1��");
			return;
		}
		try {
			CsvWriter cw=new CsvWriter(new BufferedWriter(new FileWriter(new File(outpath))), ',');
			CsvReader cr=new CsvReader(new FileReader(new File(inpath)));
			while(cr.readRecord()) {
				String line=cr.getRawRecord();
				String [] words=line.split(",");
				if(sigratio != 1) cw.write(words[0]);
				for(int i=sigratio-1;i<words.length*allratio;i+=sigratio) {
					cw.write(words[i]);
				}
				cw.flush();
				cw.endRecord();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ����csv�ļ�����֮���ŷ����þ���<P/>
	 * 
	 * |1��1��    1��2��    1��3��    ��������   1��n�� | <P/>
	 * |2��1��    2��2��    2��3��    ��������   2��n�� | <P/>
	 * |������                                                                            |<P/>
	 * |n��1��    n��2��    n��3��    ��������    n��n��|<P/>
	 * 
	 * @param path �����ļ�·��,����Ҫ��׺��
	 * @param len �����ļ�����
	 */
	public static void caldistEucliden(String path,int dimen,int len) {
		//double [][] distEuclid=new double[97][97];
		double [] arr1=new double [len];
		double [] arr2=new double [len];
		
		String outpath="C:\\Users\\mayi\\Desktop\\���ܵ����ݼ�\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\distEucliden.csv";
		File outfile=new File(outpath);
		
		try {
			CsvWriter cw=new CsvWriter(new BufferedWriter(new FileWriter(outfile)), ',');
			CsvReader cr=new CsvReader(new FileReader(new File(path+".csv")));
			for(int i=0;i<dimen;i++) {
				if(cr.readRecord()) {
					String [] chrs=cr.getRawRecord().split(",");
					for(int k=0;k<len;k++){
						arr1[k]= Double.parseDouble(chrs[k]);
					}
				}
				CsvReader cr2=new CsvReader(new FileReader(new File(path+" - ����.csv")));
				for(int j=i+1;j<dimen;j++) {
					double sum=0;
					if(cr2.readRecord()) {
						String [] chrs=cr2.getRawRecord().split(",");
						for(int k=0;k<len;k++){
							arr2[k]= Double.parseDouble(chrs[k]);
							sum+=Math.pow(arr1[k]-arr2[k], 2);
						}
					}
					cw.write(((Double)Math.sqrt(sum)).toString());
				}
				cw.flush();
				cw.endRecord();
			}
			cw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("д��ɹ���");
	}
	
	/**
	 * �ݹ�
	 * @param path �ļ����ļ��еľ��Ե�ַ
	 * @return ����ļ�����������ļ������ļ����б�
	 */
	public static List<File> getFiles(String path){
		File root=new File(path);
		List<File> files=new ArrayList<File>();
		if(!root.isDirectory()) {
			files.add(root);
		}
		else {
			for(File f:root.listFiles()) {
				files.addAll(getFiles(f.getAbsolutePath()));
			}
		}
		return files;
	}
	
	/**
	 * ��csv�ļ��ָ�
	 */
	public static void cutcsv() {
		
	}
	
	/**
	 * ��ȡ���csv�ļ�����ÿ��csv�ļ�������Ҫ�ĵ����б�Ϊ��д��һ��csv�ļ��С�
	 * ���õ���csv�ļ�������Ӧ�����Ƕ��csv�ļ��ĸ�����ͬ��
	 */
	public static void docsv() {
		
		String outpath="C:\\Users\\mayi\\Desktop\\���ܵ����ݼ�\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\voltageseries.csv";
		
		File outfile=new File(outpath);
		
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(outfile));
			CsvWriter cwriter=new CsvWriter(bw,',');
			List<File> files=getFiles("C:\\Users\\mayi\\Desktop\\���ܵ����ݼ�\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\��ѹ����");
			//ȥ��
			
			for(File infile : files) {
				infile.setReadable(true);
				infile.setWritable(true);
				CsvReader creader=new CsvReader(new FileReader(infile));
				//String line="";
				creader.readHeaders();
				while(creader.readRecord()) {
					creader.getRawRecord();
					cwriter.write(creader.get("V1"));
				}
				cwriter.flush();
				cwriter.endRecord();
				creader.close();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ���exports�ļ�
	 * Export monitor S_1000663-DA1_VI_vs_Time ��ʽ
	 */
	public static void exportexs() {
		String pathname="C:\\Users\\mayi\\Desktop\\���ܵ����ݼ�\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\Services_ckt7 - ����.dss";
		File filename=new File(pathname);
		File writename=new File("C:\\Users\\mayi\\Desktop\\���ܵ����ݼ�\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\exports.dss");
		
		try {
			InputStreamReader reader=new InputStreamReader(new FileInputStream(filename));
			BufferedReader br=new BufferedReader(reader);
			
			writename.createNewFile();
			BufferedWriter bw=new BufferedWriter(new FileWriter(writename));
			
			String line="";
			while((line=br.readLine()) !=null) {
				
				String[] chs=line.split(" ");
				
				if(chs!=null && chs.length>2) {
					String[] subs=chs[2].split("\\.");
					if(subs.length==2) {
						String tmp=chs[1].split("\\.")[1];
						bw.write("Export monitor "+tmp+"_VI_vs_Time\n");
					}
				}
			}
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ���Monitor�ļ�
	 */
	public static void exportMonitor() {
		
		String pathname="C:\\Users\\mayi\\Desktop\\���ܵ����ݼ�\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\Services_ckt7 - ����.dss";
		File filename=new File(pathname);
		File writename=new File("C:\\Users\\mayi\\Desktop\\���ܵ����ݼ�\\electricdss-code-r2386-trunk-Distrib-EPRITestCircuits-ckt7\\tmp.txt");
		
		try {
			InputStreamReader reader=new InputStreamReader(new FileInputStream(filename));
			BufferedReader br=new BufferedReader(reader);
			
			writename.createNewFile();
			BufferedWriter bw=new BufferedWriter(new FileWriter(writename));
			
			String line="";
			while((line=br.readLine()) !=null) {
				
				String[] chs=line.split(" ");
				
				if(chs!=null && chs.length>2) {
					String[] subs=chs[2].split("\\.");
					if(subs.length==2) {
						String tmp=chs[1].split("\\.")[1];
						bw.write("New monitor."+tmp+"-VI_vs_Time Line."+tmp+" 2 Mode=0\n");
						
					}
				}
			}
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}