package ac.uk.susx.tag.sampler;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class DocConsolidator {
	
	public void consolidateDocs(String[] locs, String outDir) {
		for(String loc : locs) {
			File file = new File(loc);
			if(file.exists() && file.isDirectory()) {
				System.out.println("true");
				String[] files = file.list();
				for(String f : files){
					try {
						FileUtils.copyFile(new File(loc + "/" + f), new File(outDir + "/" + f));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String[] locs = {"/Volumes/External/Datasets/music-ner/pos-ner-samples-non-music/tok-pos-per-loc-org/above-20-tokens","/Volumes/External/Datasets/music-ner/pos-ner-samples-music/above-20-tokens"};
		String outDir = "/Volumes/External/Datasets/music-ner/consolidated-dataset";
		DocConsolidator dc = new DocConsolidator();
		dc.consolidateDocs(locs, outDir);
	}

}
