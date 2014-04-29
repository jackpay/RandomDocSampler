package ac.uk.susx.tag.sampler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.io.FileUtils;


public class Sampler {
	
	private final Random rand;
	private final int sampSize;
	private final String inDir;
	private final String outDir;
	private final boolean exclude;
	private HashSet<String> excludeList;
	private static final String SUFFIX = ".msg";
	
	public Sampler(String inDir, String outDir, int sampSize, boolean exclude){
		this.inDir = inDir;
		this.outDir = outDir;
		this.sampSize = sampSize;
		rand = new Random();
		this.exclude = exclude;
		if(exclude) {
			excludeList = buildExcludeList();
		}
	}
	
	@SuppressWarnings("resource")
	public HashSet<String> buildExcludeList(){
		HashSet<String> excludeList = new HashSet<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(getClass().getClassLoader().getResource("exclude.txt").getFile()),"UTF-8"));
			String line = br.readLine();
			while(line != null){
				excludeList.add(line.replace("\n", ""));
				line = br.readLine();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Build List");
		return excludeList;
	}
	
	public void sample() {
		File in = new File(inDir);
		if(in.exists() && in.isDirectory()) {
			copyFiles(makeSampleSet(in.listFiles()));
		}
	}
	
	private void copyFiles(HashSet<File> files) {
		File out = new File(outDir + "/" + sampSize);
		out.mkdir();
		for(File file : files) {
			File copyFile = new File(out + "/" + file.getName() + SUFFIX );
			try {
				FileUtils.copyFile(file, copyFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.err.println("Copied file: " + file.getName());
		}
	}
	
	private HashSet<File> makeSampleSet(File[] files) {
		HashSet<File> sample = new HashSet<File>();
		if(sampSize > files.length) {
			sample.addAll(Arrays.asList(files));
		}
		else{
			while(sample.size() < sampSize) {
				int i = rand.nextInt(files.length);
				if(exclude){
					if(!excludeList.contains(files[i].getName() + ".msg")){
						sample.add(files[i]);
					}
					else{
						System.err.println("Found excluded");
					}
				}
				else{
					sample.add(files[i]);
				}
			}
		}
		return sample;
	}
	
	public static void main(String[] args) {
		String inDir = "/Volumes/External/Datasets/PhD-Projects/Lumi/decompressed-files";
		String outDir = "/Users/jackpay/Documents/PhdProjects/Lumi/Experiments/ner-two/non-music-sample";
		int sampSize = 2000000;
		Sampler samp = new Sampler(inDir,outDir,sampSize,false);
		samp.sample();
	}

}
