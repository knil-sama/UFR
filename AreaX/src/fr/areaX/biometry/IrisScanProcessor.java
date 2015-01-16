package fr.areaX.biometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class IrisScanProcessor implements IrisScanInterface {

	private static final String nativeProgramPath = "native/get_histogram";
	private static final String imgRef = "native/cercle.png";
	
	public IrisScanProcessor() {
	}
	
	@Override
	public JSONObject parseImage(String imageUrl, int threshold) {
		try {
			
			File imgFile = new File(imageUrl);
			File refFile = new File(imgRef);
			
			Process cprocess = new ProcessBuilder(nativeProgramPath, 
					refFile.getAbsolutePath(), imgFile.getAbsolutePath(), String.valueOf(threshold)).start();

			InputStream inputStream = cprocess.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream);
			BufferedReader bfr = new BufferedReader(isr);
			
			String line;
			String buffer = "";
			while((line=bfr.readLine())!=null){
				buffer += line;
			}
			return (new JSONObject(buffer));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (new JSONObject("{'reply': 'Java RunTime Exception'}"));
	}
	
	public static void test() {
		IrisScanProcessor iris = new IrisScanProcessor();
		
		JSONObject histo = iris.parseImage("", 50);
		
		System.out.println(histo.toString());
	}

}
