package ac.uk.susx.tag.sampler;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;


public class Sampler {
	
	private final Random rand;
	private final int sampSize;
	private final String inDir;
	private final String outDir;
	
	public Sampler(String inDir, String outDir, int sampSize){
		this.inDir = inDir;
		this.outDir = outDir;
		this.sampSize = sampSize;
		rand = new Random();
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
			File copyFile = new File(out + "/" + file.getName());
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
				sample.add(files[rand.nextInt(files.length)]);
			}
		}
		return sample;
	}
	
	public static void main(String[] args) {
		String inDir = "/Users/jp242/Documents/Projects/Lumi/tokenised-files/tok/18-03-2014-(3)";
		String outDir = "/Users/jp242/Documents/Projects/Lumi/samples";
		int sampSize = 20000;
		Sampler samp = new Sampler(inDir,outDir,sampSize);
		samp.sample();
	}

}
