package ac.uk.susx.tag.sampler;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.msgpack.MessagePack;
import org.msgpack.type.ArrayValue;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Unpacker;

public class LumiMsgPackDocumentFormatter {
	
	private static final String BODY = "body";
	private static final String LANG = "language";
	private static final String LANG_CODE = "en";
	private static final String US_LANG_CODE = "en-US";
	private static final String TITLE = "title";
	private static final String URL = "rendered_url";
	private static final String[] URLS = {"nme.com","soundcloud.com","last.fm","pandora.com","allmusic.com","songmeanings.com","pitchfork.com","grooveshark.com","spotify.com","musicovery.com","moodfuse.com",
		"artistdirect.com","mashable.com","music-video","garageband","absolutepunk.net","bandcamp.com","groovera.com","mtv.com","kerrang.com"};

	public File createDocument(File file) {
		MessagePack msgpack = new MessagePack();
		byte[] bytes = null;
		try {
			bytes = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		Unpacker unpacker = msgpack.createUnpacker(in);
		try {
			Value v = unpacker.readValue();
			MapValue mv = v.asMapValue();
			for(Value key : mv.keySet()) {
				ArrayValue aKey = (ArrayValue) key;
				String keyVal = aKey.get(1).toString().replace("\"", "");
				if(keyVal != null && keyVal.equals(LANG) && !mv.get(key).isNilValue()) {
					if(!mv.get(key).toString().equals(null)){
						if(!mv.get(key).asArrayValue().get(1).toString().replace("\"", "").equals(LANG_CODE) && !mv.get(key).asArrayValue().get(1).toString().replace("\"", "").equals(US_LANG_CODE)) {
							return null;
						}
					}
				}
				if(keyVal != null && keyVal.equals(URL) && !mv.get(key).isNilValue()){
					if(!mv.get(key).toString().equals(null)){
						int i = 0;
						while(i < URLS.length){
							if(mv.get(key).asArrayValue().get(1).toString().toLowerCase().contains(URLS[i])) {
								System.out.println(mv.get(key).asArrayValue().get(1).toString().toLowerCase());
								return file;
							}
							i++;
						}
					}
					else{
						return null;
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		LumiMsgPackDocumentFormatter lpdf = new LumiMsgPackDocumentFormatter();
		
		String inLoc = "/Users/jp242/Documents/Projects/Lumi/decompressedFiles";
		String outLoc = "/Users/jp242/Documents/Projects/Lumi/ner-pos-samples/music-files";
		String fileList = "/Users/jp242/Documents/Projects/Lumi/musicFilesList.txt";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new PrintWriter(new File(fileList)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Collection<File> files = null;
		File fileDir = new File(inLoc);
		if(fileDir.exists() && fileDir.isDirectory()) {
			files = FileUtils.listFiles(fileDir, null,true);
		}
		for(File file : files) {
			File pFile = lpdf.createDocument(file);
			if(pFile != null) {
				try {
					FileUtils.copyFile(pFile, new File(outLoc + "/" + pFile.getName()));
					System.out.println("Copied file: " + pFile.getName());
					bw.write(pFile.getName() + "\n");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
